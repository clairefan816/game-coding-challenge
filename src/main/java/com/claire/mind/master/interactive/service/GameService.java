package com.claire.mind.master.interactive.service;

import com.claire.mind.master.interactive.exception.InvalidGameException;
import com.claire.mind.master.interactive.exception.InvalidGuessException;
import com.claire.mind.master.interactive.exception.NotFoundException;
import com.claire.mind.master.interactive.model.*;
import com.claire.mind.master.interactive.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

// Inject the service to controller
@Service
@AllArgsConstructor
public class GameService {
    // Pass playerPreference's information and then create a game
    public Game createGame(PlayerPreference playerPreference) throws IOException, InterruptedException {
        Game game = new Game();
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayerPreference(playerPreference);
        game.setGuesses(new ArrayList<>());
        game.setStepResults(new ArrayList<>());
        // need to be replaced later
        int[] secretNumber = queryNumber("https://www.random.org/integers/?num=4&min=0&max=7&col=4&base=10&format=plain&rnd=new");
        game.setSecretNumber(secretNumber);
        game.setStatus(GameStatus.IN_PROGRESS);

        GameStorage.getInstance().setGame(game);

        return game;
    }

    public int[] queryNumber(String uri) throws IOException, InterruptedException {
        int[] secretNumber = new int[4];
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String[] responseArray = response.body().trim().split("\\s");
        for (int i = 0; i < 4; i++){
            secretNumber[i] = Integer.parseInt(responseArray[i]);
        }
        System.out.println("Response: " + response.body());

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
        if (!checkValidRound(guesses, Constants.MAX_GUESSES)){
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
        if (previousResults.size() == Constants.MAX_GUESSES && !(isWin)){
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
        if (stepResult.getMatchDigitAndPosition() == Constants.GAME_WIDTH){
            return true;
        }
        return false;
    }

}
