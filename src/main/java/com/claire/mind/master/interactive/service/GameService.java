package com.claire.mind.master.interactive.service;

import com.claire.mind.master.interactive.exception.*;
import com.claire.mind.master.interactive.model.*;
import com.claire.mind.master.interactive.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;



/**
 * Inject the service to controller
 */
@Service
@AllArgsConstructor
public class GameService {
    /**
     * Create a new game
     * @param playerPreference String Indicate Easy or Hard game user selected
     * @return Game Object with gameID and secret generated, along with all other info
     * @throws IOException
     * @throws InterruptedException
     */
    public Game createGame(PlayerPreference playerPreference) throws IOException, InterruptedException, NoResponseException {
        // Create new Game object
        Game game = new Game();
        // Generate UUID as gameId
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayerPreference(playerPreference);
        game.setGuesses(new ArrayList<>());
        game.setStepResults(new ArrayList<>());


        String gameLevel = playerPreference.name();
        int[] secretNumber = new int[Constants.Num_Of_Digits_One_Round];
        if (gameLevel.equals("EASY")){
            secretNumber = queryNumber(Constants.EASY_GAME_PATTERN_QUERY);
        } else if (gameLevel.equals("HARD")){
            secretNumber = queryNumber(Constants.HARD_GAME_PATTERN_QUERY);
        }

        game.setSecretNumber(secretNumber);
        game.setStatus(GameStatus.IN_PROGRESS);

        // store the game information to the GameStorage
        GameStorage.getInstance().setGame(game);

        return game;
    }


    /**
     * Send request and receive response with HttpClient
     * @param uri String The address of the target web service
     * @return int[] Four digits int array
     * @throws IOException
     * @throws InterruptedException
     * @throws InterruptedException
     */
    public int[] queryNumber(String uri) throws IOException, InterruptedException, NoResponseException {
        int[] secretNumber = new int[Constants.Num_Of_Digits_One_Round];
        // Create a request to the target URI
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();

        // Create a client
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
                .authenticator(Authenticator.getDefault())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200){
            throw new NoResponseException(("Failed to get secret number from web service, the " +
                    "returned statusCode is: " + response.statusCode()));
        }

        String[] responseArray = response.body().trim().split("\\s");
        for (int i = 0; i < Constants.Num_Of_Digits_One_Round; i++){
            secretNumber[i] = Integer.parseInt(responseArray[i]);
        }
        return secretNumber;
    }


    /**
     * Play the game, check the game status
     * @param gameGuess Object Pass in by user with gameID and the guess number
     * @return Game object
     * @throws NotFoundException
     * @throws InvalidGameException
     * @throws InvalidGuessException
     */
    public Game playGame(GameGuess gameGuess) throws NotFoundException, InvalidGameException, InvalidGuessException {
        if (!GameStorage.getInstance().getGames().containsKey(gameGuess.getGameId())){
            throw new NotFoundException("Game not found");
        }

        // retrieve the game information from GameStorage
        Game game = GameStorage.getInstance().getGames().get(gameGuess.getGameId());

        // check the current status of the game
        if (!game.getStatus().equals(GameStatus.IN_PROGRESS)){
            throw new InvalidGameException("Not a valid game");
        }

        // add the newGuess to the game
        List<int[]> guesses = game.getGuesses();
        if (!checkValidRound(guesses)){
            throw new InvalidGuessException("Exceed the Guess number limit");
        }

        // add the new guess into the guesses
        int[] newGuess = gameGuess.getGuess();
        guesses.add(newGuess);
        game.setGuesses(guesses);


        // compare and get the result
        StepResult stepResult = checkStepResult(game.getSecretNumber(), newGuess);
        // add the step result to the whole result
        List<StepResult> previousResults = game.getStepResults();
        previousResults.add(stepResult);
        game.setStepResults(previousResults);

        boolean isWin = checkWin(stepResult);
        if (isWin){
            game.setStatus(GameStatus.PLAYER_VICTORY);
        }
        if (previousResults.size() == Constants.MAX_Rounds_Of_GUESSES && !(isWin)){
            game.setStatus(GameStatus.PLAYER_LOST);
        }

        // save back to storage
        GameStorage.getInstance().setGame(game);
        return game;
    }

    /**
     * Check if it is a valid round
     * @param oldGuesses
     * @return
     */
    private boolean checkValidRound(List<int[]> oldGuesses) {
        int round = oldGuesses.size();
        if (round + 1 > Constants.MAX_Rounds_Of_GUESSES){
            return false;
        }
        return true;
    }

    /**
     * Compare two int array and return the compared result
     * @param secretNumber int[]
     * @param newGuess int[]
     * @return Object
     */
    public StepResult checkStepResult(int[] secretNumber, int[] newGuess){
        StepResult stepResult = new StepResult();
        int matchDigitAndPosition = 0;
        int matchDigit = 0;
        int len = Constants.Num_Of_Digits_One_Round;

        int[] mapSecretNumber = new int[10];
        int[] mapNewGuess = new int[10];
        for (int i = 0; i < len; i++){
            if (mapSecretNumber[i] == mapNewGuess[i]){
                matchDigitAndPosition++;
            } else {
                mapSecretNumber[mapSecretNumber[i]]++;
                mapNewGuess[mapNewGuess[i]]--;
            }
        }
        for (int j = 0; j < len; j++){
            matchDigit += Math.min(mapSecretNumber[j], mapNewGuess[j]);
        }

        stepResult.setA(matchDigitAndPosition);
        stepResult.setB(matchDigit);
        return stepResult;
    }

    /**
     * Check if player win the game or not
     * @param stepResult Object
     * @return true if 4A
     */
    public boolean checkWin(StepResult stepResult){
        if (stepResult.getA() == Constants.Num_Of_Digits_One_Round){
            return true;
        }
        return false;
    }

    /**
     * Retrieve the game by gameId
     * @param gameId String
     * @return Object Game
     * @throws InvalidParamException
     */
    public Game retrieveGame(String gameId) throws InvalidParamException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)){
            throw new InvalidParamException("Game with provided gameId doesn't exist");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);
        return game;
    }
}
