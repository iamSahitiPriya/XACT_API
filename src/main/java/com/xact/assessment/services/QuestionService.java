/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.models.Question;
import com.xact.assessment.repositories.QuestionRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.List;
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
    public void createQuestion(Question question){
        questionRepository.save(question);
    }

    public void updateQuestion(Question question) {
        questionRepository.update(question);
    }

    public List<Question> getAllQuestion() {
        return (List<Question>) questionRepository.findAll();
    }

    public AssessmentTopic getTopicByQuestionId(Integer questionId) {
        return getQuestion(questionId).orElseThrow().getParameter().getTopic();
    }
}
