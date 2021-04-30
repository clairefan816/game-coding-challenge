package com.claire.mind.master.interactive.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Collected constants of general utility.
 */
public class Constants {

    /**Max round of guesses*/
    public static final int MAX_Rounds_Of_GUESSES = 10;

    /**Number of digits in one round of guess*/
    public static final int Num_Of_Digits_One_Round = 4;

    /**url of querying a secret number for easy game*/
    public static final String EASY_GAME_PATTERN_QUERY = "https://www.random.org/integers/?num=4&min=0&max=7&col=4&base=10&format=plain&rnd=new";

    /**url of querying a secret number for hard game*/
    public static final String HARD_GAME_PATTERN_QUERY = "https://www.random.org/integers/?num=4&min=0&max=7&col=4&base=10&format=plain&rnd=new";

    public static final List<String> ALLOWED_LEVELS = new ArrayList<>();

    static {
        ALLOWED_LEVELS.add("EASY");
        ALLOWED_LEVELS.add("HARD");
    }
}
