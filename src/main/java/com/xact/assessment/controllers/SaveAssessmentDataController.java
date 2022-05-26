package com.xact.assessment.controllers;

import com.xact.assessment.dtos.AnswerRequest;
import com.xact.assessment.dtos.ParameterLevelAssessmentRequest;
import com.xact.assessment.dtos.TopicLevelAssessmentRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AnswerService;
import com.xact.assessment.services.TopicAndParameterLevelAssessmentService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;

@Controller("/v1/notes")
public class SaveAssessmentDataController {
    private AnswerService answerService;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;

    public SaveAssessmentDataController(AnswerService answerService, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService) {
        this.answerService = answerService;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
    }

    public SaveAssessmentDataController() {
    }

    private ModelMapper mapper = new ModelMapper();


    @Post(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessmentRequest> saveAnswer(@PathVariable("assessmentId") Integer assessmentId, @Body TopicLevelAssessmentRequest topicLevelAssessmentRequests) {

        //TODO Get assessment from service based on role
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        System.out.println(topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList());

        if (topicLevelAssessmentRequests.isRatedAtTopicLevel()) {
            TopicLevelId topicLevelId = mapper.map(topicLevelAssessmentRequests.getTopicRatingAndRecommendation(), TopicLevelId.class);
            topicLevelId.setAssessment(assessment);
            TopicLevelAssessment topicLevelAssessment = mapper.map(topicLevelAssessmentRequests.getTopicRatingAndRecommendation(), TopicLevelAssessment.class);
            topicLevelAssessment.setTopicLevelId(topicLevelId);
            topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);
        } else {
            for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest : topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList()) {
                ParameterLevelId parameterLevelId = mapper.map(parameterLevelAssessmentRequest.getParameterRatingAndRecommendation(), ParameterLevelId.class);
                parameterLevelId.setAssessment(assessment);
                ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterLevelAssessmentRequest.getParameterRatingAndRecommendation(), ParameterLevelAssessment.class);
                parameterLevelAssessment.setParameterLevelId(parameterLevelId);
                topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);
            }
        }

        for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest : topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList()) {
            for (AnswerRequest answerRequest : parameterLevelAssessmentRequest.getAnswerRequest()) {
                assessment.setAssessmentId(assessmentId);
                AnswerId answerId = mapper.map(answerRequest, AnswerId.class);
                answerId.setAssessment(assessment);
                Answer answer = mapper.map(answerRequest, Answer.class);
                answer.setAnswerId(answerId);
                answerService.saveAnswer(answer);
            }
        }
        return HttpResponse.ok();
    }
}
