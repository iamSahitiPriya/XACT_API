/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.UpdateAnswerRequest;
import com.xact.assessment.models.Answer;
import com.xact.assessment.models.AnswerId;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.Question;
import com.xact.assessment.repositories.AnswerRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    public AnswerService(AnswerRepository answerRepository, QuestionService questionService) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
    }

    public Answer saveAnswer(Answer answer) {

        if (answerRepository.existsById(answer.getAnswerId())) {
            if (answer.hasNotes()) {
                answerRepository.update(answer);
            } else {
                answerRepository.delete(answer);
            }
        } else {
            if (answer.hasNotes()) {
                answerRepository.save(answer);
            }
        }

        return answer;
    }
    public void saveAnswer(UpdateAnswerRequest answerRequest, Assessment assessment) {
        Question question = questionService.getQuestion(answerRequest.getQuestionId()).orElseThrow();
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = getAnswer(answerId).orElse(new Answer());
        answer.setAnswerId(answerId);
        answer.setAnswerNote(answerRequest.getAnswer());
        saveAnswer(answer);
    }

    public List<Answer> getAnswers(Integer assessmentId) {
        return answerRepository.findByAssessment(assessmentId);
    }


    public Optional<Answer> getAnswer(AnswerId answerId) {
        return answerRepository.findById(answerId);
    }
}
