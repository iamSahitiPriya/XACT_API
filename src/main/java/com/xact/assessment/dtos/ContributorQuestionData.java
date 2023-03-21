package com.xact.assessment.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter

public class ContributorQuestionData {
    private Integer questionId;
    private String question;
    private String comments;
    private ContributorQuestionStatus status;
}
