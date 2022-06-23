package com.xact.assessment.services;

import com.xact.assessment.models.Answer;
import com.xact.assessment.models.AnswerId;
import com.xact.assessment.repositories.AnswerRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

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

    public List<Answer> getAnswers(Integer assessmentId) {
        return answerRepository.findByAssessment(assessmentId);
    }
    public Optional<Answer> getAnswer(AnswerId answerId ){
        return answerRepository.findById(answerId);
    }
}
