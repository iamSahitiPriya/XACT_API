package com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.UserQuestion;
import com.xact.assessment.repositories.UserQuestionRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
@Transactional
public class UserQuestionService {
    UserQuestionRepository userQuestionRepository;
    ParameterService parameterService;

    public UserQuestionService(UserQuestionRepository userQuestionRepository, ParameterService parameterService) {
        this.userQuestionRepository = userQuestionRepository;
        this.parameterService = parameterService;
    }

    public UserQuestion saveUserQuestion(UserQuestion userQuestion) {

        if (userQuestion.getQuestionId() != null) {
            if (userQuestion.hasQuestion()) {
                userQuestionRepository.update(userQuestion);
            }
        } else {
            if (userQuestion.hasQuestion()) {
                userQuestionRepository.save(userQuestion);
            }
        }
        return userQuestion;
    }

    public List<UserQuestion> findAllUserQuestion(Integer assessmentId) {
        return userQuestionRepository.findByAssessmentId(assessmentId);
    }

    public Optional<UserQuestion> searchUserQuestion(Integer questionId) {
        return userQuestionRepository.findById(questionId);
    }

    public void deleteUserQuestion(Integer questionId) {
        userQuestionRepository.deleteById(questionId);
    }

    public UserQuestion saveUserQuestion(Assessment assessment, Integer parameterId, String additionalQuestion) {
        AssessmentParameter parameter = parameterService.getParameter(parameterId).orElseThrow();
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setAssessment(assessment);
        userQuestion.setQuestion(additionalQuestion);
        userQuestion.setParameter(parameter);
        userQuestionRepository.save(userQuestion);
        return userQuestion;
    }

    public UserQuestion saveUserAnswer(Integer questionId, String additionalAnswer) {
        UserQuestion userQuestion = userQuestionRepository.findById(questionId).orElseThrow();
        userQuestion.setAnswer(additionalAnswer);
        userQuestionRepository.update(userQuestion);
        return userQuestion;

    }

    public UserQuestion updateUserQuestion(Integer questionId, String updatedQuestion) {
        UserQuestion userQuestion = userQuestionRepository.findById(questionId).orElseThrow();
        userQuestion.setQuestion(updatedQuestion);
        userQuestionRepository.update(userQuestion);
        return userQuestion;
    }
}
