/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class QuestionDto implements Comparable<QuestionDto> {

    private Integer questionId;
    private String questionText;
    private Integer parameter;

    @Override
    public int compareTo(QuestionDto currentQuestion) {
        return questionId - currentQuestion.getQuestionId();
    }
}
