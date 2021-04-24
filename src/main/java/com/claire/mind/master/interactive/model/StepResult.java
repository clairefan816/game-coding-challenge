package com.claire.mind.master.interactive.model;

import lombok.Data;

/**
 * The result of each guess is encoded in xAyB format. A means the count of
 * numbers that guessed with both digit and position correctly. B means the
 * count of numbers that guessed with digit correct but position wrong.
 */
@Data
public class StepResult {
    private int matchDigitAndPosition;
    private int matchDigit;
}
