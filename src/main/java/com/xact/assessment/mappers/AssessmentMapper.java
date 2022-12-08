/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.mappers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Answer;
import com.xact.assessment.models.Assessment;
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

    public AssessmentResponse map(Assessment assessment, List<Answer> answers, List<TopicRatingAndRecommendation> topicRecommendationResponses, List<ParameterRatingAndRecommendation> paramRecommendationResponses) {
        AssessmentResponse assessmentResponse = map(assessment);
        assessmentResponse.setAnswerResponseList(mapAnswers(answers));
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
            answerResponseList.add(eachAnswerResponse);
        }
        return answerResponseList;
    }


}
