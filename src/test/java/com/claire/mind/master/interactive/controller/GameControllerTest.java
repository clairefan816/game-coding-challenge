package com.claire.mind.master.interactive.controller;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void startNewGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/game");
        MvcResult results = mvc.perform(request).andReturn();
        assertEquals(("", results.getResponse().getContentAsString()));
    }

    @Test
    public void gamePlay() {
    }
}