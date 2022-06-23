package com.xact.assessment.services;

import com.xact.assessment.models.Question;
import com.xact.assessment.repositories.QuestionRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.Optional;

@Singleton
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    public Optional<Question> getQuestion(Integer questionId){
        return questionRepository.findById(questionId);
    }
}
