package com.claire.mind.master.interactive.model;

import lombok.Data;

import java.util.List;

@Data
public class Game {
    private String gameId;
    private Player player;
    private int[] secretNumber;
    private List<int[]> guesses;
    private List<StepResult> stepResults;
    private GameStatus status;
}