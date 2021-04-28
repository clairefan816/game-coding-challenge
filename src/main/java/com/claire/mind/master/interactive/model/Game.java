package com.claire.mind.master.interactive.model;

import lombok.Value;
import lombok.With;

import java.util.List;

/**
 * class Game include all info needed for playing the game
 */
@Value
public class Game {
    private String gameId;
    private PlayerPreference playerPreference;
    @With private GameStatus status;
    private int[] secretNumber;
    @With private List<int[]> guesses;
    @With private List<StepResult> stepResults;
}
