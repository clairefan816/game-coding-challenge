package com.claire.mind.master.interactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Class GameGuess represents one round of guess
 */
@Data
@AllArgsConstructor
public class GameGuess {
    private String gameId;
    private int[] guess;
}
