package com.xact.assessment.services;

import com.xact.assessment.models.UserQuestion;
import com.xact.assessment.repositories.UserQuestionRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class UserQuestionService {
    UserQuestionRepository userQuestionRepository;

    public UserQuestionService(UserQuestionRepository userQuestionRepository) {
        this.userQuestionRepository = userQuestionRepository;
    }

    public UserQuestion saveUserQuestion(UserQuestion userQuestion) {

        if (userQuestion.getQuestionId()!=null) {
            if (userQuestion.hasQuestion()) {
                userQuestionRepository.update(userQuestion);
            } else {
                userQuestionRepository.delete(userQuestion);
            }
        } else {
            if (userQuestion.hasQuestion()) {
                userQuestionRepository.save(userQuestion);
            }
        }
        return  userQuestion;
    }
    public List<UserQuestion> findAllUserQuestion(Integer assessmentId){
        return userQuestionRepository.findByAssessment(assessmentId);
    }

    public Optional<UserQuestion> searchUserQuestion(Integer questionId) {
        return  userQuestionRepository.findById(questionId);
    }

    public void deleteUserQuestion(Integer questionId) {
        userQuestionRepository.deleteById(questionId);
    }
}
