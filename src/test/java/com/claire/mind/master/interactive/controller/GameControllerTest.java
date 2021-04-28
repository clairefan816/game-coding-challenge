package com.claire.mind.master.interactive.controller;

import com.claire.mind.master.interactive.model.PlayerPreference;
import com.claire.mind.master.interactive.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

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
    public void hello() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/v1/mindmaster/hello");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("hello, World", result.getResponse().getContentAsString());
    }
    @Test
    public void startNewGame() throws Exception {
        PlayerPreference playerPreference = PlayerPreference.EASY;
        RequestBuilder request = MockMvcRequestBuilders.post("/game", playerPreference);
        this.mvc.perform(request).andExpect(status().isOk()).andReturn();;
    }

    @Test
    public void gamePlay() {
    }
}