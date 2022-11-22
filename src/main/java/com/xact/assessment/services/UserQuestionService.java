package com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.UserQuestion;
import com.xact.assessment.repositories.UserQuestionRepository;

public class UserQuestionService {
    UserQuestionRepository userQuestionRepository;

    public UserQuestionService(UserQuestionRepository userQuestionRepository) {
        this.userQuestionRepository = userQuestionRepository;
    }

    public void saveUserQuestion(Assessment assessment, AssessmentParameter assessmentParameter, String questionText,String answerText) {
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setAssessment(assessment);
        userQuestion.setQuestion(questionText);
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAnswer(answerText);
        userQuestionRepository.save(userQuestion);
    }
}
