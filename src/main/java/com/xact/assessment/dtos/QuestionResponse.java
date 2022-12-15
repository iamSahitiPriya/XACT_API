package com.xact.assessment.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class QuestionResponse {
    private Integer category;
    private Integer module;
    private Integer parameterId;
    private Integer topic;
    private Integer questionId;
    private String questionText;
}
