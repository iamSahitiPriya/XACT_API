package com.xact.assessment.services;

import com.xact.assessment.models.UserQuestion;
import com.xact.assessment.repositories.UserQuestionRepository;

public class UserQuestionService {
    UserQuestionRepository userQuestionRepository;

    public UserQuestionService(UserQuestionRepository userQuestionRepository) {
        this.userQuestionRepository = userQuestionRepository;
    }

    public void saveUserQuestion(UserQuestion userQuestion) {

        if (userQuestionRepository.existsById(userQuestion.getQuestionId())) {
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
    }

}
