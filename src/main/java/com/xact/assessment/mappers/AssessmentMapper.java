/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.mappers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Answer;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.UserQuestion;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

public class AssessmentMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.addMappings(new PropertyMap<Assessment, AssessmentResponse>() {
            @Override
            protected void configure() {
                skip(destination.isOwner());
            }
        });
    }

    public AssessmentResponse map(Assessment assessment, List<Answer> answers, List<UserQuestion> userQuestionList,List<TopicRatingAndRecommendation> topicRecommendationResponses, List<ParameterRatingAndRecommendation> paramRecommendationResponses) {
        AssessmentResponse assessmentResponse = map(assessment);
        assessmentResponse.setAnswerResponseList(mapAnswers(answers));
        assessmentResponse.setUserQuestionResponseList(mapUserQuestions(userQuestionList));
        assessmentResponse.setTopicRatingAndRecommendation(topicRecommendationResponses);
        assessmentResponse.setParameterRatingAndRecommendation(paramRecommendationResponses);
        return assessmentResponse;
    }



    public AssessmentResponse map(Assessment assessment) {
        AssessmentResponse assessmentResponse = modelMapper.map(assessment, AssessmentResponse.class);
        assessmentResponse.setDomain(assessment.getOrganisation().getDomain());
        assessmentResponse.setIndustry(assessment.getOrganisation().getIndustry());
        assessmentResponse.setTeamSize(assessment.getOrganisation().getSize());
        assessmentResponse.setAssessmentState(assessment.getAssessmentState());
        assessmentResponse.setAssessmentPurpose(assessment.getAssessmentPurpose());
        assessmentResponse.setUsers(assessment.getFacilitators());
        return assessmentResponse;
    }

    private List<AnswerResponse> mapAnswers(List<Answer> answerList) {
        List<AnswerResponse> answerResponseList = new ArrayList<>();
        for (Answer eachAnswer : answerList) {
            AnswerResponse eachAnswerResponse = new AnswerResponse();
            QuestionDto eachQuestion = modelMapper.map(eachAnswer.getAnswerId(), QuestionDto.class);
            eachAnswerResponse.setQuestionId(eachQuestion.getQuestionId());
            eachAnswerResponse.setAnswerNote(eachAnswer.getAnswerNote());
            eachAnswerResponse.setRating(eachAnswer.getRating());
            answerResponseList.add(eachAnswerResponse);
        }
        return answerResponseList;
    }
    private List<UserQuestionResponse> mapUserQuestions(List<UserQuestion> userQuestionList) {
        List<UserQuestionResponse> userQuestionResponseList = new ArrayList<>();
        for (UserQuestion eachUserQuestion : userQuestionList){
            UserQuestionResponse userQuestionResponse = new UserQuestionResponse();
            userQuestionResponse.setQuestionId(eachUserQuestion.getQuestionId());
            userQuestionResponse.setParameterId(eachUserQuestion.getParameter().getParameterId());
            userQuestionResponse.setQuestion(eachUserQuestion.getQuestion());
            userQuestionResponse.setAnswer(eachUserQuestion.getAnswer());
            userQuestionResponseList.add(userQuestionResponse);
        }
        return  userQuestionResponseList;
    }


}
