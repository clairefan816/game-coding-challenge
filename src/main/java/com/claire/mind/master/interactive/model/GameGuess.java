package com.claire.mind.master.interactive.model;

import lombok.Data;

import java.util.List;

@Data
public class GameGuess {
    private String gameId;
    private int[] guess;
}
