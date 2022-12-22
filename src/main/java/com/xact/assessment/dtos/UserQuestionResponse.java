package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserQuestionResponse {
    Integer questionId;
    Integer parameterId;
    String question;
    String answer;
}