/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.util.*;


@Controller("/v1/assessments")
public class AssessmentController {
    private final AnswerService answerService;
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private final UsersAssessmentsService usersAssessmentsService;
    private final AssessmentService assessmentService;
    private final UserAuthService userAuthService;
    private final ParameterService parameterService;
    private final TopicService topicService;
    private final QuestionService questionService;


    @Value("${validation.email:^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$}")
    private String emailPattern = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$";


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
        assessmentRequest.validate(emailPattern);

        Assessment assessment = assessmentService.createAssessment(assessmentRequest, loggedInUser);
        AssessmentResponse assessmentResponse = modelMapper.map(assessment, AssessmentResponse.class);

        return HttpResponse.created(assessmentResponse);
    }

    @Put(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse updateAssessment(@PathVariable("assessmentId") Integer assessmentId, @Valid @Body AssessmentRequest assessmentRequest, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        assessmentRequest.validate(emailPattern);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessment.setAssessmentName(assessmentRequest.getAssessmentName());
            assessment.getOrganisation().setOrganisationName(assessmentRequest.getOrganisationName());
            assessment.getOrganisation().setDomain(assessmentRequest.getDomain());
            assessment.getOrganisation().setIndustry(assessmentRequest.getIndustry());
            assessment.getOrganisation().setSize(assessmentRequest.getTeamSize());

            Set<AssessmentUsers> assessmentUsers = assessmentService.getAssessmentUsers(assessmentRequest, loggedInUser, assessment);

            UserId userID = new UserId();
            userID.setAssessment(assessment);
            assessmentService.updateAssessment(assessment, assessmentUsers);
        }
        return HttpResponse.ok();
    }


    @Put(value = "/{assessmentId}/statuses/open", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse reopenAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);


        Assessment finishedAssessment = assessmentService.reopenAssessment(assessment);
        AssessmentResponse assessmentResponse = modelMapper.map(finishedAssessment, AssessmentResponse.class);

        return HttpResponse.ok(assessmentResponse);
    }

    @Put(value = "/{assessmentId}/statuses/finish", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> finishAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);

        Assessment finishedAssessment = assessmentService.finishAssessment(assessment);
        AssessmentResponse assessmentResponse = modelMapper.map(finishedAssessment, AssessmentResponse.class);

        return HttpResponse.ok(assessmentResponse);
    }


    @Get(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> getAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);

        List<Answer> answerResponse = answerService.getAnswers(assessment.getAssessmentId());
        List<AnswerResponse> answerResponseList = getAnswerResponseList(answerResponse);

        List<String> users = assessmentService.getAssessmentUsers(assessmentId);

        List<TopicLevelAssessment> topicLevelAssessmentList = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessment.getAssessmentId());
        List<TopicRatingAndRecommendation> topicRatingAndRecommendationsResponseList = getTopicRatingAndRecommendationList(topicLevelAssessmentList);

        List<ParameterLevelAssessment> parameterLevelAssessmentList = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessment.getAssessmentId());
        List<ParameterRatingAndRecommendation> parameterRatingAndRecommendationsResponseList = getParameterRatingAndRecommendationList(parameterLevelAssessmentList);

        AssessmentResponse assessmentResponse = modelMapper.map(assessment, AssessmentResponse.class);
        assessmentResponse.setAnswerResponseList(answerResponseList);
        assessmentResponse.setTopicRatingAndRecommendation(topicRatingAndRecommendationsResponseList);
        assessmentResponse.setParameterRatingAndRecommendation(parameterRatingAndRecommendationsResponseList);
        assessmentResponse.setDomain(assessment.getOrganisation().getDomain());
        assessmentResponse.setIndustry(assessment.getOrganisation().getIndustry());
        assessmentResponse.setTeamSize(assessment.getOrganisation().getSize());
        assessmentResponse.setUsers(users);

        return HttpResponse.ok(assessmentResponse);
    }

    @Post(value = "/notes/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessmentRequest> saveAnswer(@PathVariable("assessmentId") Integer assessmentId, @Body  TopicLevelAssessmentRequest topicLevelAssessmentRequests, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);

        if (assessment.isEditable()) {
            List<Answer> answerList = setAnswerListToSave(topicLevelAssessmentRequests, assessment);
            if (topicLevelAssessmentRequests.isRatedAtTopicLevel()) {
                TopicLevelAssessment topicLevelRatingAndRecommendation = setTopicLevelRating(topicLevelAssessmentRequests, assessment);
                List<TopicLevelRecommendation> topicLevelRecommendationList=setTopicLevelRecommendation(topicLevelAssessmentRequests,assessment);
                topicAndParameterLevelAssessmentService.saveTopicLevelAssessment(topicLevelRatingAndRecommendation,answerList);
            } else {
                List<ParameterLevelAssessment> parameterLevelAssessmentList = setParameterLevelRatingAndResommendationList(topicLevelAssessmentRequests, assessment);
                topicAndParameterLevelAssessmentService.saveParameterLevelAssessment(parameterLevelAssessmentList, answerList);
            }
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }


    @Patch(value = "/answers/{assessmentId}/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessmentRequest> saveNotesAnswer(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("questionId") Integer questionId, @Body @Nullable String notes, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            Question question = questionService.getQuestion(questionId).orElseThrow();
            AnswerId answerId = new AnswerId(assessment, question);
            Answer answer = answerService.getAnswer(answerId).orElse(new Answer());
            answer.setAnswerId(answerId);
            answer.setAnswer(notes);
            answerService.saveAnswer(answer);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }

    @Patch(value = "/parameterRecommendation/{assessmentId}/{parameterId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessmentRequest> saveParameterRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body @Nullable String recommendation, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            AssessmentParameter assessmentParameter = parameterService.getParameter(parameterId).orElseThrow();
            ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
            ParameterLevelAssessment parameterLevelAssessment = topicAndParameterLevelAssessmentService.searchParameter(parameterLevelId).orElse(new ParameterLevelAssessment());
            parameterLevelAssessment.setParameterLevelId(parameterLevelId);
            parameterLevelAssessment.setRecommendation(recommendation);
            topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }

    @Patch(value = "/topicRecommendation/{assessmentId}/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessmentRequest> saveTopicRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer topicId, @Body @Nullable String recommendation, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            AssessmentTopic assessmentTopic = topicService.getTopic(topicId).orElseThrow();
            TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
            TopicLevelAssessment topicLevelAssessment = topicAndParameterLevelAssessmentService.searchTopic(topicLevelId).orElse(new TopicLevelAssessment());
            topicLevelAssessment.setTopicLevelId(topicLevelId);
//            topicLevelAssessment.setRecommendation(recommendation);
            topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }

    @Patch(value = "/topicRating/{assessmentId}/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessmentRequest> saveTopicRating(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer topicId, @Body @Nullable String rating, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            AssessmentTopic assessmentTopic = topicService.getTopic(topicId).orElseThrow();
            TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
            TopicLevelAssessment topicLevelAssessment = topicAndParameterLevelAssessmentService.searchTopic(topicLevelId).orElse(new TopicLevelAssessment());
            topicLevelAssessment.setTopicLevelId(topicLevelId);
            Integer topicRating = rating != null ? Integer.valueOf(rating) : null;
            topicLevelAssessment.setRating(topicRating);
            topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }

    @Patch(value = "/parameterRating/{assessmentId}/{parameterId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessmentRequest> saveParameterRating(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body @Nullable String rating, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            AssessmentParameter assessmentParameter = parameterService.getParameter(parameterId).orElseThrow();
            ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
            ParameterLevelAssessment parameterLevelAssessment = topicAndParameterLevelAssessmentService.searchParameter(parameterLevelId).orElse(new ParameterLevelAssessment());
            parameterLevelAssessment.setParameterLevelId(parameterLevelId);
            Integer parameterRating = rating != null ? Integer.valueOf(rating) : null;
            parameterLevelAssessment.setRating(parameterRating);
            topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }


    private ModelMapper modelMapper = new ModelMapper();

    public AssessmentController(UsersAssessmentsService usersAssessmentsService, UserAuthService userAuthService, AssessmentService assessmentService, AnswerService answerService, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, ParameterService parameterService, TopicService topicService, QuestionService questionService) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.userAuthService = userAuthService;
        this.assessmentService = assessmentService;
        this.answerService = answerService;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.parameterService = parameterService;
        this.topicService = topicService;
        this.questionService = questionService;
    }

    private Assessment getAuthenticatedAssessment(Integer assessmentId, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        return assessmentService.getAssessment(assessmentId, loggedInUser);
    }

    private TopicLevelAssessment setTopicLevelRating(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        TopicLevelId topicLevelId = modelMapper.map(topicLevelAssessmentRequests.getTopicRatingAndRecommendation(), TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = modelMapper.map(topicLevelAssessmentRequests.getTopicRatingAndRecommendation(), TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        return topicLevelAssessment;
    }

    private List<TopicLevelRecommendation> setTopicLevelRecommendation(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        for(TopicLevelRecommendationRequest topicLevelRecommendationRequest:topicLevelAssessmentRequests.getTopicRatingAndRecommendation().getTopicLevelRecommendationRequest()){
           TopicLevelRecommendation topicLevelRecommendation = modelMapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
           topicLevelRecommendationList.add(topicLevelRecommendation);
        }
        return topicLevelRecommendationList;
    }


    private List<ParameterLevelAssessment> setParameterLevelRatingAndResommendationList(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        List<ParameterLevelAssessment> parameterLevelAssessmentList = new ArrayList<>();
        for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest : topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList()) {
            ParameterLevelId parameterLevelId = modelMapper.map(parameterLevelAssessmentRequest.getParameterRatingAndRecommendation(), ParameterLevelId.class);
            parameterLevelId.setAssessment(assessment);
            ParameterLevelAssessment parameterLevelAssessment = modelMapper.map(parameterLevelAssessmentRequest.getParameterRatingAndRecommendation(), ParameterLevelAssessment.class);
            parameterLevelAssessment.setParameterLevelId(parameterLevelId);
            parameterLevelAssessmentList.add(parameterLevelAssessment);
        }
        return parameterLevelAssessmentList;
    }

    private List<Answer> setAnswerListToSave(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        List<Answer> answerList = new ArrayList<>();
        for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest : topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList()) {
            for (AnswerRequest answerRequest : parameterLevelAssessmentRequest.getAnswerRequest()) {
                AnswerId answerId = modelMapper.map(answerRequest, AnswerId.class);
                answerId.setAssessment(assessment);
                Answer answer = modelMapper.map(answerRequest, Answer.class);
                answer.setAnswerId(answerId);
                answerList.add(answer);
            }
        }
        return answerList;
    }

    private List<AnswerResponse> getAnswerResponseList(List<Answer> answerList) {
        List<AnswerResponse> answerResponseList = new ArrayList<>();
        for (Answer eachAnswer : answerList) {
            AnswerResponse eachAnswerResponse = new AnswerResponse();
            QuestionDto eachQuestion = modelMapper.map(eachAnswer.getAnswerId(), QuestionDto.class);
            eachAnswerResponse.setQuestionId(eachQuestion.getQuestionId());
            eachAnswerResponse.setAnswer(eachAnswer.getAnswer());
            answerResponseList.add(eachAnswerResponse);
        }
        return answerResponseList;
    }

    private List<TopicRatingAndRecommendation> getTopicRatingAndRecommendationList(List<TopicLevelAssessment> topicLevelAssessmentList) {
        List<TopicRatingAndRecommendation> topicRatingAndRecommendationsResponseList = new ArrayList<>();
        for (TopicLevelAssessment eachTopic : topicLevelAssessmentList) {
            TopicRatingAndRecommendation eachTopicRatingAndRecommendation = new TopicRatingAndRecommendation();
            AssessmentTopicDto eachTopicDto = modelMapper.map(eachTopic.getTopicLevelId(), AssessmentTopicDto.class);
            eachTopicRatingAndRecommendation.setTopicId(eachTopicDto.getTopicId());
            eachTopicRatingAndRecommendation.setRating(eachTopic.getRating());
//            eachTopicRatingAndRecommendation.setRecommendation(eachTopic.getRecommendation());
            topicRatingAndRecommendationsResponseList.add(eachTopicRatingAndRecommendation);
        }
        return topicRatingAndRecommendationsResponseList;
    }

    private List<ParameterRatingAndRecommendation> getParameterRatingAndRecommendationList(List<ParameterLevelAssessment> parameterLevelAssessmentList) {
        List<ParameterRatingAndRecommendation> parameterRatingAndRecommendationsResponseList = new ArrayList<>();

        for (ParameterLevelAssessment eachParameter : parameterLevelAssessmentList) {
            ParameterRatingAndRecommendation eachParameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
            AssessmentParameterDto eachParameterDto = modelMapper.map(eachParameter.getParameterLevelId(), AssessmentParameterDto.class);
            eachParameterRatingAndRecommendation.setParameterId(eachParameterDto.getParameterId());
            eachParameterRatingAndRecommendation.setRating(eachParameter.getRating());
            eachParameterRatingAndRecommendation.setRecommendation(eachParameter.getRecommendation());
            parameterRatingAndRecommendationsResponseList.add(eachParameterRatingAndRecommendation);
        }
        return parameterRatingAndRecommendationsResponseList;
    }

    private void updateAssessment(Assessment assessment) {
        assessment.setUpdatedAt(new Date());
        assessmentService.updateAssessment(assessment);
    }
}
