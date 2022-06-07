/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller("/v1/assessments")
public class AssessmentController {
    private final AnswerService answerService;
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private final UsersAssessmentsService usersAssessmentsService;
    private final AssessmentService assessmentService;
    private final UserAuthService userAuthService;

    private ModelMapper modelMapper = new ModelMapper();

    public AssessmentController(UsersAssessmentsService usersAssessmentsService, UserAuthService userAuthService, AssessmentService assessmentService, AnswerService answerService, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.userAuthService = userAuthService;
        this.assessmentService = assessmentService;
        this.answerService = answerService;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<AssessmentResponse>> getAssessments(Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);

        List<Assessment> assessments = usersAssessmentsService.findAssessments(loggedInUser.getUserEmail());
        List<AssessmentResponse> assessmentResponses = new ArrayList<>();
        if (Objects.nonNull(assessments))
            assessments.forEach(assessment -> assessmentResponses.add(modelMapper.map(assessment, AssessmentResponse.class)));
        return HttpResponse.ok(assessmentResponses);
    }

    @Post(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> createAssessment(@Valid @Body AssessmentRequest assessmentRequest, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);

        Assessment assessment = assessmentService.createAssessment(assessmentRequest, loggedInUser);
        AssessmentResponse assessmentResponse = modelMapper.map(assessment, AssessmentResponse.class);

        return HttpResponse.created(assessmentResponse);
    }

    @Put(value = "/{assessmentId}/statuses/open", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse reopenAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        Assessment assessment = assessmentService.getAssessment(assessmentId, loggedInUser);

        Assessment finishedAssessment = assessmentService.reopenAssessment(assessment);
        AssessmentResponse assessmentResponse = modelMapper.map(finishedAssessment, AssessmentResponse.class);

        return HttpResponse.ok(assessmentResponse);
    }

    @Put(value = "/{assessmentId}/statuses/finish", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> finishAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        Assessment assessment = assessmentService.getAssessment(assessmentId, loggedInUser);

        Assessment finishedAssessment = assessmentService.finishAssessment(assessment);
        AssessmentResponse assessmentResponse = modelMapper.map(finishedAssessment, AssessmentResponse.class);

        return HttpResponse.ok(assessmentResponse);
    }


    @Get(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> getAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        Assessment assessment = assessmentService.getAssessment(assessmentId, loggedInUser);
        List<Answer> answerResponse = answerService.getAnswers(assessment.getAssessmentId());
        List<AnswerResponse> answerList = new ArrayList<>();
        for (Answer eachAnswer : answerResponse) {
            AnswerResponse eachAnswerResponse = new AnswerResponse();
            QuestionDto eachQuestion = modelMapper.map(eachAnswer.getAnswerId(), QuestionDto.class);
            eachAnswerResponse.setQuestionId(eachQuestion.getQuestionId());
            eachAnswerResponse.setAnswer(eachAnswer.getAnswer());
            answerList.add(eachAnswerResponse);
        }
        List<TopicLevelAssessment> topicRatingAndRecommendations = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessment.getAssessmentId());
        List<TopicRatingAndRecommendation> topicRatingAndRecommendationsResponseList = new ArrayList<>();

        for (TopicLevelAssessment eachTopic : topicRatingAndRecommendations) {
            TopicRatingAndRecommendation eachTopicRatingAndRecommendation = new TopicRatingAndRecommendation();
            AssessmentTopicDto eachTopicDto = modelMapper.map(eachTopic.getTopicLevelId(), AssessmentTopicDto.class);
            eachTopicRatingAndRecommendation.setTopicId(eachTopicDto.getTopicId());
            eachTopicRatingAndRecommendation.setRating(eachTopic.getRating());
            eachTopicRatingAndRecommendation.setRecommendation(eachTopic.getRecommendation());
            topicRatingAndRecommendationsResponseList.add(eachTopicRatingAndRecommendation);
        }
        List<ParameterLevelAssessment> parameterRatingAndRecommendations = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessment.getAssessmentId());
        List<ParameterRatingAndRecommendation> parameterRatingAndRecommendationsResponseList = new ArrayList<>();

        for (ParameterLevelAssessment eachParameter : parameterRatingAndRecommendations) {
            ParameterRatingAndRecommendation eachParameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
            AssessmentParameterDto eachParameterDto = modelMapper.map(eachParameter.getParameterLevelId(), AssessmentParameterDto.class);
            eachParameterRatingAndRecommendation.setParameterId(eachParameterDto.getParameterId());
            eachParameterRatingAndRecommendation.setRating(eachParameter.getRating());
            eachParameterRatingAndRecommendation.setRecommendation(eachParameter.getRecommendation());
            parameterRatingAndRecommendationsResponseList.add(eachParameterRatingAndRecommendation);
        }

        AssessmentResponse assessmentResponse = modelMapper.map(assessment, AssessmentResponse.class);

        assessmentResponse.setAnswerResponseList(answerList);
        assessmentResponse.setTopicRatingAndRecommendation(topicRatingAndRecommendationsResponseList);
        assessmentResponse.setParameterRatingAndRecommendation(parameterRatingAndRecommendationsResponseList);
        return HttpResponse.ok(assessmentResponse);
    }

}
