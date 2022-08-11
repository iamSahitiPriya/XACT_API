package com.xact.assessment.services;

import com.xact.assessment.models.Question;
import com.xact.assessment.repositories.QuestionRepository;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    public Optional<Question> getQuestion(Integer questionId){
        return questionRepository.findById(questionId);
    }
    public void createQuestion(Question question){
        questionRepository.save(question);
    }

    public void updateQuestion(Question question) {
        questionRepository.update(question);
    }
}
