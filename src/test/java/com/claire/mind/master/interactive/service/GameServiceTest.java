package com.claire.mind.master.interactive.service;

import com.claire.mind.master.interactive.MindMasterInteractiveApplication;
import com.claire.mind.master.interactive.exception.*;
import com.claire.mind.master.interactive.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = MindMasterInteractiveApplication.class)
public class GameServiceTest {

    @Mock HttpClientProvider httpClientProvider;
    @Mock HttpClient httpClient;
    GameService gameService;

    @Before
    public void setUp() throws Exception {

        // fake response
        HttpResponse<String> response = new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return "1 2 3 4";
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };

        Mockito.when(httpClient.<String>send(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(response
                );
        Mockito.when(httpClientProvider.get()).thenReturn(httpClient);
        gameService = new GameService(httpClientProvider);
    }


    @Test
    public void createNewGame_successCreated() throws IOException, NoResponseException,
            InterruptedException {
        PlayerPreference playerPreference = new PlayerPreference();
        playerPreference.setPreference("EASY");
        Game game = gameService.createGame(playerPreference);
        assertNotNull(game.getGameId());
        assertArrayEquals(game.getSecretNumber(), new int[]{1, 2, 3, 4});
        assertEquals("IN_PROGRESS", game.getStatus().toString());
    }

    @Test
    public void checkPlayGme_twoGuessRounds() throws IOException,
            NoResponseException,
            InterruptedException,
            InvalidGameException, NotFoundException, InvalidGuessException {
        PlayerPreference playerPreference = new PlayerPreference();
        playerPreference.setPreference("HARD");
        Game testGame = gameService.createGame(playerPreference);
        String testGameId = testGame.getGameId();
        int[] testGuess1 = {1, 1, 1, 1};
        int[] testGuess2 = {1, 2, 3, 3};
        GameGuess gameGuess1 = new GameGuess(testGameId, testGuess1);
        GameGuess gameGuess2 = new GameGuess(testGameId, testGuess2);

        Game newGame1 = gameService.playGame(gameGuess1);
        Game newGame2 = gameService.playGame(gameGuess2);
        System.out.println(newGame1);
        System.out.println(newGame2);
        List<StepResult> list1 = newGame1.getStepResults();
        StepResult step1 = list1.get(0);
        List<StepResult> list2 = newGame2.getStepResults();
        StepResult step2 = list2.get(1);

        assertEquals(1, step1.getA());
        assertEquals(0, step1.getB());
        assertEquals(3, step2.getA());
        assertEquals(0, step2.getB());
    }

    @Test
    public void checkWinPlay() throws IOException, NoResponseException, InterruptedException,
            InvalidGameException, NotFoundException, InvalidGuessException {
        PlayerPreference playerPreference = new PlayerPreference();
        playerPreference.setPreference("HARD");
        Game checkRightResultGame = gameService.createGame(playerPreference);
        String testRightGameId = checkRightResultGame.getGameId();
        int[] testRightGuess = {1, 2, 3, 4};
        GameGuess rightGuess = new GameGuess(testRightGameId, testRightGuess);
        Game rightResultGame = gameService.playGame(rightGuess);
        assertEquals(GameStatus.PLAYER_VICTORY, rightResultGame.getStatus());
    }


    @Test
    public void checkLostPlay() throws IOException, NoResponseException, InterruptedException,
            InvalidGameException, NotFoundException, InvalidGuessException, InvalidParamException {
        PlayerPreference playerPreference = new PlayerPreference();
        playerPreference.setPreference("HARD");
        Game checkLostResultGame = gameService.createGame(playerPreference);
        String testLostGameId = checkLostResultGame.getGameId();
        int[] oneGuess = {1, 1, 1, 1};
        GameGuess wrongGuess = new GameGuess(testLostGameId, oneGuess);

        for (int i = 0; i < 10; i++){
            gameService.playGame(wrongGuess);
        }
        assertEquals(GameStatus.PLAYER_LOST, gameService.retrieveGame(testLostGameId).getStatus());
    }

    @Test
    public void checkInvalidRound(){
        List<int[]> oldGuesses = new ArrayList<>();
        int[] guess = new int[]{};
        for (int i = 0; i < 10; i++){
            oldGuesses.add(guess);
        }
        boolean isValid = gameService.checkValidRound(oldGuesses);
        assertFalse(isValid);
    }

    @Test
    public void checkValidRound(){
        List<int[]> oldGuesses = new ArrayList<>();
        int[] guess = new int[]{};
        oldGuesses.add(guess);
        boolean isValid = gameService.checkValidRound(oldGuesses);
        assertTrue(isValid);
    }

    @Test
    public void successRetrieveGame() throws IOException, NoResponseException, InterruptedException, InvalidParamException {
        PlayerPreference playerPreference = new PlayerPreference();
        playerPreference.setPreference("EASY");
        Game needGame = gameService.createGame(playerPreference);
        String needGameId = needGame.getGameId();
        String status = gameService.retrieveGame(needGameId).getStatus().name();
        assertEquals("IN_PROGRESS", status);
    }

    @Test(expected = InvalidParamException.class)
    public void failRetrieveGame() throws IOException, NoResponseException, InterruptedException,
            InvalidParamException {
        String noGameId = "";
        gameService.retrieveGame(noGameId).getStatus().name();
    }
}