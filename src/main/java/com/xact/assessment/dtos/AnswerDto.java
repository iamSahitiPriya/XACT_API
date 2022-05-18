package com.xact.assessment.dtos;


import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Introspected
@NoArgsConstructor
@Getter
@Setter
public class AnswerDto {
    private Integer assessmentId;
    private Integer questionId;
    private String questionText;
}
