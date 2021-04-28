package com.claire.mind.master.interactive.model;

import lombok.Data;

import java.util.List;

/**
 * Class GameGuess represents one round of guess
 */
@Data
public class GameGuess {
    private String gameId;
    private int[] guess;
}
