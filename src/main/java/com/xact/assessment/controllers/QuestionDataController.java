package com.xact.assessment.controllers;

import com.xact.assessment.dtos.AnswerDto;
import com.xact.assessment.dtos.ParameterLevelAssessmentRequest;
import com.xact.assessment.dtos.RatingAndRecommendationDto;
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

import java.util.List;

@Controller("/v1/notes")
public class QuestionDataController {
    private AnswerService answerService;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;

    public QuestionDataController(AnswerService answerService, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService) {
        this.answerService = answerService;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
    }

    public QuestionDataController() {
    }

    private ModelMapper mapper = new ModelMapper();


    @Post(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AnswerDto> saveAnswer(@PathVariable("assessmentId") Integer assessmentId, @Body List<TopicLevelAssessmentRequest> topicLevelAssessmentRequests) {

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        RatingAndRecommendationDto ratingAndRecommendationDto1 = mapper.map(topicLevelAssessmentRequests, RatingAndRecommendationDto.class);
        TopicLevelId topicLevelId = mapper.map(ratingAndRecommendationDto1, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = mapper.map(ratingAndRecommendationDto1, TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);

        List<ParameterLevelAssessmentRequest> parameterLevelAssessmentRequestList = (List<ParameterLevelAssessmentRequest>) mapper.map(topicLevelAssessment, ParameterLevelAssessmentRequest.class);


        for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest : parameterLevelAssessmentRequestList) {
            List<AnswerDto> answerDtoList = (List<AnswerDto>) mapper.map(parameterLevelAssessmentRequest, AnswerDto.class);
            for (AnswerDto answerDto : answerDtoList) {
                assessment.setAssessmentId(assessmentId);
                AnswerId answerId = mapper.map(answerDto, AnswerId.class);
                answerId.setAssessment(assessment);
                Answer answer = mapper.map(answerDto, Answer.class);
                answer.setAnswerId(answerId);
                answerService.saveAnswer(answer);
            }

            assessment.setAssessmentId(assessmentId);
            RatingAndRecommendationDto ratingAndRecommendationDto2 = mapper.map(parameterLevelAssessmentRequest, RatingAndRecommendationDto.class);
            ParameterLevelId parameterLevelId = mapper.map(ratingAndRecommendationDto2, ParameterLevelId.class);
            parameterLevelId.setAssessment(assessment);
            ParameterLevelAssessment parameterLevelAssessment = mapper.map(ratingAndRecommendationDto2, ParameterLevelAssessment.class);
            parameterLevelAssessment.setParameterLevelId(parameterLevelId);
            topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);


        }
        return HttpResponse.ok();
    }
}
