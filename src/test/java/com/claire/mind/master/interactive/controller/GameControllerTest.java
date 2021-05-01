package com.claire.mind.master.interactive.controller;

import com.claire.mind.master.interactive.exception.NoResponseException;
import com.claire.mind.master.interactive.model.Game;
import com.claire.mind.master.interactive.model.GameGuess;
import com.claire.mind.master.interactive.model.GameStatus;
import com.claire.mind.master.interactive.model.PlayerPreference;
import com.claire.mind.master.interactive.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GameService gameService;

    @Test
    public void givePlayerPreference_whenGenerateNewGame_thenStatus200() throws Exception {
        PlayerPreference playerPreference = new PlayerPreference();
        playerPreference.setPreference("EASY");
        playerPreference.setWithColor(true);
        playerPreference.setWithNumber(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String preference = objectMapper.writeValueAsString(playerPreference);
        RequestBuilder request =
                MockMvcRequestBuilders.post("/v1/mindmaster/game").contentType(MediaType.APPLICATION_JSON)
                .content(preference);
        this.mvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void givePlayerPreference_whenGenerateNewGame_thenStatus400() throws Exception {
        PlayerPreference playerPreference = new PlayerPreference();
        playerPreference.setPreference("");
        playerPreference.setWithColor(true);
        playerPreference.setWithNumber(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String preference = objectMapper.writeValueAsString(playerPreference);
        RequestBuilder request =
                MockMvcRequestBuilders.post("/v1/mindmaster/game").contentType(MediaType.APPLICATION_JSON)
                        .content(preference);
        this.mvc.perform(request).andExpect(status().isBadRequest()).andReturn();
    }
}