package com.claire.mind.master.interactive.model;

/**
 * Three game status that can be used
 * {@link #IN_PROGRESS}
 * {@link #PLAYER_VICTORY}
 * {@link #PLAYER_LOST}
 */
public enum GameStatus {
    /**
     * Game is in progress
     */
    IN_PROGRESS,
    /**
     * Player win this round of game
     */
    PLAYER_VICTORY,
    /**
     * Player lost this round of game
     */
    PLAYER_LOST
}
