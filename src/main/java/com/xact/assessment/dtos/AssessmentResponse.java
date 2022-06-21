/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentResponse {

    private Integer assessmentId;

    private String assessmentName;

    private String organisationName;

    private AssessmentStatusDto assessmentStatus;

    private Date updatedAt;

    private Integer teamSize;

    private String domain;

    private String industry;

    private List<String> users;

    private List<AnswerResponse> answerResponseList;

    private List<ParameterRatingAndRecommendation> parameterRatingAndRecommendation;

    private List<TopicRatingAndRecommendation> topicRatingAndRecommendation;

}
