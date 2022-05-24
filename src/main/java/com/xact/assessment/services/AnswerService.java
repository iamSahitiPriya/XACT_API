package com.xact.assessment.services;

import com.xact.assessment.models.Answer;
import com.xact.assessment.repositories.AnswerRepository;
import jakarta.inject.Singleton;

@Singleton
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer saveAnswer(Answer answer) {

        if (answerRepository.existsById(answer.getAnswerId())) {
            answerRepository.update(answer);
        } else {
            answerRepository.save(answer);
        }

        return answer;
    }
}
