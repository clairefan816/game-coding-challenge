package com.claire.mind.master.interactive.service;

import com.claire.mind.master.interactive.MindMasterInteractiveApplication;
import com.claire.mind.master.interactive.exception.InvalidGameException;
import com.claire.mind.master.interactive.exception.InvalidGuessException;
import com.claire.mind.master.interactive.exception.NoResponseException;
import com.claire.mind.master.interactive.exception.NotFoundException;
import com.claire.mind.master.interactive.model.Game;
import com.claire.mind.master.interactive.model.GameGuess;
import com.claire.mind.master.interactive.model.PlayerPreference;
import com.claire.mind.master.interactive.model.StepResult;
import com.claire.mind.master.interactive.storage.GameStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        Game game = gameService.createGame(PlayerPreference.EASY);
        assertNotNull(game.getGameId());
        assertArrayEquals(game.getSecretNumber(), new int[]{1, 2, 3, 4});
        assertEquals("IN_PROGRESS", game.getStatus().toString());
    }

    @Test
    public void queryNumber() throws IOException, NoResponseException, InterruptedException, InvalidGameException, NotFoundException, InvalidGuessException {
        Game testGame = gameService.createGame(PlayerPreference.HARD);
        String testGameId = testGame.getGameId();
        int[] testGuess1 = {1, 1, 1, 1};
        int[] testGuess2 = {1, 2, 3, 3};
        GameGuess gameGuess1 = new GameGuess();
        GameGuess gameGuess2 = new GameGuess();
        gameGuess1.setGameId(testGameId);
        gameGuess1.setGuess(testGuess1);

        gameGuess2.setGameId(testGameId);
        gameGuess2.setGuess(testGuess2);

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
}