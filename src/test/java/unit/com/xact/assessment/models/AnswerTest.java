/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.models.Answer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnswerTest {

    @Test
    void hasNullNotes() {
        Answer answer = new Answer();
        assertEquals(false, answer.hasNotes());
    }

    @Test
    void hasEmptyNotes() {
        Answer answer = new Answer();
        answer.setAnswerNote("");
        assertEquals(false, answer.hasNotes());
    }

    @Test
    void hasBlankNotes() {
        Answer answer = new Answer();
        answer.setAnswerNote("     ");
        assertEquals(false, answer.hasNotes());
    }

    @Test
    void hasValidNotes() {
        Answer answer = new Answer();
        answer.setAnswerNote("Hello");
        assertEquals(true, answer.hasNotes());
    }
}
