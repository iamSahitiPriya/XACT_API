package com.xact.assessment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserQuestionRequest {
    Integer questionId;
    Integer parameterId;
    String question;
    String answer;
}
