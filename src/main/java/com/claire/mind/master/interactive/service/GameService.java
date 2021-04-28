package com.claire.mind.master.interactive.service;

import com.claire.mind.master.interactive.exception.InvalidGameException;
import com.claire.mind.master.interactive.exception.InvalidGuessException;
import com.claire.mind.master.interactive.exception.NoResponseException;
import com.claire.mind.master.interactive.exception.NotFoundException;
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
        int[] secretNumber = new int[4];
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
        for (int i = 0; i < 4; i++){
            secretNumber[i] = Integer.parseInt(responseArray[i]);
        }
        return secretNumber;
    }

    // Pass in GameId, Location, and the number
    // Make a move
    // Find the game, put the number in the location
    public Game playGame(GameGuess gameGuess) throws NotFoundException, InvalidGameException, InvalidGuessException {
        if (!GameStorage.getInstance().getGames().containsKey(gameGuess.getGameId())){
            throw new NotFoundException("Game not found");
        }
        Game game = GameStorage.getInstance().getGames().get(gameGuess.getGameId());
        if (!game.getStatus().equals(GameStatus.IN_PROGRESS)){
            throw new InvalidGameException("Not a valid game");
        }

        // add the newGuess to the game
        List<int[]> guesses = game.getGuesses();
        if (!checkValidRound(guesses, Constants.MAX_Rounds_Of_GUESSES)){
            throw new InvalidGuessException("Exceed the Guess number limit");
        }
        int[] newGuess = gameGuess.getGuess();
        guesses.add(newGuess);
        game.setGuesses(guesses);


        // compare and get the result
        StepResult stepResult = checkStepResult(game.getSecretNumber(), newGuess);
        // add the step result to the whole result
        List<StepResult> previousResults = game.getStepResults();
        previousResults.add(stepResult);
        game.setStepResults(previousResults);

        Boolean isWin = checkWin(stepResult);
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

    private boolean checkValidRound(List<int[]> oldGuesses, int maxLimit) {
        int round = oldGuesses.size();
        if (round + 1 > maxLimit){
            return false;
        }
        return true;
    }

    public StepResult checkStepResult(int[] secretNumber, int[] newGuess){
        StepResult stepResult = new StepResult();
        int matchDigitAndPosition = 0;
        int matchDigit = 0;
        int len = secretNumber.length;
        // Key is the Digit, Value is the frequency
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++){
            map.put(secretNumber[i], map.getOrDefault(secretNumber[i], 0) + 1);
        }

        for (int i = 0; i < len; i++){
            if (secretNumber[i] == newGuess[i]){
                matchDigitAndPosition++;
                map.put(secretNumber[i], map.get(secretNumber[i]) - 1);
            } else if (map.containsKey(newGuess[i]) && map.get(newGuess[i]) != 0){
                matchDigit++;
                map.put(newGuess[i], map.get(newGuess[i]) - 1);
            }
        }
        stepResult.setMatchDigitAndPosition(matchDigitAndPosition);
        stepResult.setMatchDigit(matchDigit);
        return stepResult;
    }

    public boolean checkWin(StepResult stepResult){
        if (stepResult.getMatchDigitAndPosition() == Constants.Num_Of_Digits_One_Round){
            return true;
        }
        return false;
    }

}
