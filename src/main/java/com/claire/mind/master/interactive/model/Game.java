package com.claire.mind.master.interactive.model;

import lombok.Data;

import java.util.List;

/**
 * class Game include all info needed for playing the game
 */
@Data
public class Game {
    private String gameId;
    private PlayerPreference playerPreference;
    private GameStatus status;
    private int[] secretNumber;
    private List<int[]> guesses;
    private List<StepResult> stepResults;
}
