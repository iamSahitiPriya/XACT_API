/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import com.xact.assessment.models.AssessmentQuestionReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.SortedSet;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class QuestionDto implements Comparable<QuestionDto> {

    private Integer questionId;
    private String questionText;
    private Integer parameter;
    private ContributorQuestionStatus status;
    private String comments;
    private SortedSet<AssessmentQuestionReferenceDto> references;

    @Override
    public int compareTo(QuestionDto currentQuestion) {
        return questionId - currentQuestion.getQuestionId();
    }
}
