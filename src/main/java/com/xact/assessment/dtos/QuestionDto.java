package com.xact.assessment.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class QuestionDto {

    private Integer questionId;
    private String questionText;
    private Integer parameter;

}
