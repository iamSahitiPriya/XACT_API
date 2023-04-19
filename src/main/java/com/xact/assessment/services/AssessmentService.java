/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.config.FeedbackNotificationConfig;
import com.xact.assessment.dtos.*;
import com.xact.assessment.mappers.AssessmentMapper;
import com.xact.assessment.mappers.MasterDataMapper;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.AssessmentStatus.Completed;
import static java.util.stream.Collectors.toSet;


@Singleton
public class AssessmentService {

    private final UsersAssessmentsService usersAssessmentsService;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentMasterDataService assessmentMasterDataService;
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private final FeedbackNotificationConfig feedbackNotificationConfig;
    private final AssessmentMapper assessmentMapper = new AssessmentMapper();
    private static final ModelMapper modelMapper = new ModelMapper();

    private final MasterDataMapper masterDataMapper = new MasterDataMapper();

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    ModelMapper mapper = new ModelMapper();

    public AssessmentService(AssessmentRepository assessmentRepository, UsersAssessmentsService usersAssessmentsService, AssessmentMasterDataService assessmentMasterDataService, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, FeedbackNotificationConfig feedbackNotificationConfig) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.feedbackNotificationConfig = feedbackNotificationConfig;
    }

    public Assessment createAssessment(AssessmentRequest assessmentRequest, User user) {
        Assessment assessment = mapper.map(assessmentRequest, Assessment.class);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        Organisation organisation = mapper.map(assessmentRequest, Organisation.class);
        assessment.setOrganisation(organisation);

        Set<AssessmentUser> assessmentUserSet = getAssessmentUsers(assessmentRequest, user, assessment);
        createAssessment(assessment);

        usersAssessmentsService.saveUsersInAssessment(assessmentUserSet);
        assessment.setAssessmentUsers(assessmentUserSet);

        return assessment;
    }


    private void createAssessment(Assessment assessment) {
        assessmentRepository.save(assessment);
    }

    public Set<AssessmentUser> getAssessmentUsers(AssessmentRequest assessmentRequest, User loggedInUser, Assessment assessment) {
        List<UserDto> users = assessmentRequest.getUsers();


        Optional<AssessmentUser> assessmentOwner = Optional.empty();
        if (assessment.getAssessmentId() != null) {
            assessmentOwner = assessment.getOwner();
        }

        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentOwner.ifPresent(assessmentUsers::add);
        for (UserDto user : users) {
            if (!user.getEmail().isBlank()) {
                user.setRole(UserRole.Facilitator);
                if (assessmentOwner.isEmpty()) {
                    if (loggedInUser.getUserEmail().equals(user.getEmail())) {
                        user.setRole(UserRole.Owner);
                    }
                } else {
                    if (user.getEmail().equals(assessmentOwner.get().getUserId().getUserEmail())) {
                        continue;
                    }
                }
                AssessmentUser assessmentUser = mapper.map(user, AssessmentUser.class);
                assessmentUser.setUserId(new UserId(user.getEmail(), assessment));
                assessmentUsers.add(assessmentUser);
            }
        }
        return assessmentUsers;
    }

    public Set<String> getNewlyAddedUser(Set<AssessmentUser> existingUsers, Set<AssessmentUser> newUsers) {
        Set<AssessmentUser> newUsersSet = new HashSet<>(newUsers);
        return getUpdatedUsers(existingUsers, newUsersSet);
    }

    public Set<String> getDeletedUser(Set<AssessmentUser> existingUsers, Set<AssessmentUser> deletedUsers) {
        Set<AssessmentUser> newUserSet = new HashSet<>(deletedUsers);
        return getUpdatedUsers(newUserSet, existingUsers);
    }


    public Set<AssessmentUser> getAssessmentFacilitators(Assessment assessment) {
        return usersAssessmentsService.getAssessmentFacilitators(assessment);
    }

    private Set<String> getUpdatedUsers(Set<AssessmentUser> assessmentUsers, Set<AssessmentUser> assessmentUsersSet) {
        assessmentUsersSet.removeAll(assessmentUsers);
        Set<String> users = new HashSet<>();
        for (AssessmentUser user : assessmentUsersSet) {
            if (user.getRole() == AssessmentRole.Facilitator) {
                users.add(user.getUserId().getUserEmail());
            }
        }
        return users;
    }


    public Assessment getAssessment(Integer assessmentId, User user) {
        return usersAssessmentsService.getAssessment(assessmentId, user);
    }


    public Assessment finishAssessment(Assessment assessment) {
        assessment.setAssessmentStatus(Completed);
        assessment.setUpdatedAt(new Date());
        return assessmentRepository.update(assessment);
    }

    public Assessment reopenAssessment(Assessment assessment) {
        assessment.setAssessmentStatus(Active);
        assessment.setUpdatedAt(new Date());
        return assessmentRepository.update(assessment);
    }


    public void updateAssessmentAndUsers(Assessment assessment, Set<AssessmentUser> assessmentUsers) {
        usersAssessmentsService.updateUsersInAssessment(assessmentUsers, assessment.getAssessmentId());
        assessment.setAssessmentUsers(assessmentUsers);
        updateAssessment(assessment);
    }

    public void updateAssessment(Assessment assessment) {
        assessment.setUpdatedAt(new Date());
        assessmentRepository.update(assessment);
    }
    public List<Assessment> getTotalAssessments(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        Date startDateTime = simpleDateFormat.parse(startDate);
        Date endDateTime = simpleDateFormat.parse(endDate);
        return assessmentRepository.totalAssessments(startDateTime, endDateTime);
    }

    public List<Assessment> getAdminAssessmentsData(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return assessmentRepository.totalAssessments(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
    }

    public void saveAssessmentModules(List<ModuleRequest> moduleRequests, Assessment assessment) {
        usersAssessmentsService.saveAssessmentModules(moduleRequests, assessment);
    }

    public void updateAssessmentModules(List<ModuleRequest> moduleRequest, Assessment assessment) {
        usersAssessmentsService.updateAssessmentModules(moduleRequest, assessment);
    }

    public void softDeleteAssessment(Assessment assessment) {
        assessment.setDeleted(true);
        assessment.setUpdatedAt(new Date());
        updateAssessment(assessment);
    }

    public Assessment getAssessmentById(Integer assessmentId) {
        return assessmentRepository.findByAssessmentId(assessmentId);
    }

    public List<Assessment> findAssessments(String userEmail) {
        return usersAssessmentsService.findAssessments(userEmail);
    }

    public List<Answer> getAnswers(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getAnswers(assessmentId);
    }

    public void saveAnswer(UpdateAnswerRequest answerRequest, Assessment assessment) {
        topicAndParameterLevelAssessmentService.saveAnswer(answerRequest, assessment);
    }

    public Optional<AssessmentParameter> getParameter(Integer parameterId) {
        return topicAndParameterLevelAssessmentService.getParameter(parameterId);
    }

    public List<TopicLevelRating> getTopicLevelRatings(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getTopicRatings(assessmentId);
    }

    public List<TopicLevelRecommendation> getTopicRecommendations(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getTopicRecommendations(assessmentId);
    }

    public List<ParameterLevelRating> getParameterLevelRatings(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getParameterRatings(assessmentId);

    }

    public List<ParameterLevelRecommendation> getParameterRecommendations(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getParameterRecommendations(assessmentId);
    }

    public void deleteTopicRecommendation(Integer recommendationId) {
        topicAndParameterLevelAssessmentService.deleteTopicRecommendation(recommendationId);
    }

    public void deleteParameterRecommendation(Integer recommendationId) {
        topicAndParameterLevelAssessmentService.deleteParameterRecommendation(recommendationId);
    }

    public Optional<TopicLevelRating> searchTopicRating(TopicLevelId topicLevelId) {
        return topicAndParameterLevelAssessmentService.searchTopicRating(topicLevelId);
    }

    public void saveTopicRating(Integer topicId, Assessment assessment,String rating ) {
        AssessmentTopic assessmentTopic = getTopic(topicId).orElseThrow();
        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        TopicLevelRating topicLevelRating = searchTopicRating(topicLevelId).orElse(new TopicLevelRating());
        topicLevelRating.setTopicLevelId(topicLevelId);
        Integer topicRating = rating != null ? Integer.valueOf(rating) : null;
        topicLevelRating.setRating(topicRating);
        topicAndParameterLevelAssessmentService.saveTopicRating(topicLevelRating);
    }

    public Optional<ParameterLevelRating> searchParameterRating(ParameterLevelId parameterLevelId) {
        return topicAndParameterLevelAssessmentService.searchParameterRating(parameterLevelId);

    }

    public void saveParameterRating(Integer parameterId, Assessment assessment, String rating) {
        AssessmentParameter assessmentParameter = getParameter(parameterId).orElseThrow();
        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        ParameterLevelRating parameterLevelRating =searchParameterRating(parameterLevelId).orElse(new ParameterLevelRating());
        parameterLevelRating.setParameterLevelId(parameterLevelId);
        Integer parameterRating = rating != null ? Integer.valueOf(rating) : null;
        parameterLevelRating.setRating(parameterRating);
        topicAndParameterLevelAssessmentService.saveParameterRating(parameterLevelRating);
    }


    public AssessmentTopic getTopicByQuestionId(Integer questionId) {
        return topicAndParameterLevelAssessmentService.getTopicByQuestionId(questionId);
    }

    public List<AssessmentCategoryDto> getAllCategories() {
        List<AssessmentCategory> assessmentCategoryList =  assessmentMasterDataService.getAllCategories();
        List<AssessmentCategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(assessmentCategoryList)) {
            assessmentCategoryList.forEach(assessmentCategory -> assessmentCategoriesResponse.add(masterDataMapper.mapTillModuleOnly(assessmentCategory)));
        }
        return assessmentCategoriesResponse;
    }

    public List<AssessmentCategoryDto> getUserAssessmentCategories(Integer assessmentId) {
        List<AssessmentCategory> userAssessmentCategories =  assessmentMasterDataService.getUserAssessmentCategories(assessmentId);
        List<AssessmentCategoryDto> userAssessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(userAssessmentCategories)) {
            userAssessmentCategories.forEach(assessmentCategory -> userAssessmentCategoriesResponse.add(masterDataMapper.mapTillModuleOnly(assessmentCategory)));

        }
        return userAssessmentCategoriesResponse;
    }

    public List<UserQuestion> getUserQuestions(Integer assessmentId) {
        return usersAssessmentsService.getUserQuestions(assessmentId);
    }

    public UserQuestionResponse saveUserQuestion(Assessment assessment, Integer parameterId, String userQuestion) {
        UserQuestion savedQuestion = usersAssessmentsService.saveUserQuestion(assessment, parameterId, userQuestion);
        return modelMapper.map(savedQuestion, UserQuestionResponse.class);

    }

    public void saveUserAnswer(Integer questionId, String answer) {
        usersAssessmentsService.saveUserAnswer(questionId, answer);
    }

    public void updateUserQuestion(Integer questionId, String updatedQuestion) {
        usersAssessmentsService.updateUserQuestion(questionId, updatedQuestion);
    }

    public Optional<UserQuestion> searchUserQuestion(Integer questionId) {
        return usersAssessmentsService.searchUserQuestion(questionId);
    }

    public void deleteUserQuestion(Integer questionId) {
        usersAssessmentsService.deleteUserQuestion(questionId);
    }

    public Optional<AssessmentTopic> getTopic(Integer topicId) {
        return topicAndParameterLevelAssessmentService.getTopic(topicId);
    }


    public List<Assessment> getFinishedAssessments() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -feedbackNotificationConfig.getDurationInDays());
        Date completedDate = calendar.getTime();
        return assessmentRepository.findByCompletedStatus(completedDate);
    }

    public TopicLevelRecommendation updateTopicRecommendation(RecommendationRequest recommendationRequest) {
        return topicAndParameterLevelAssessmentService.updateTopicRecommendation(recommendationRequest);
    }

    public TopicLevelRecommendation saveTopicRecommendation(RecommendationRequest recommendationRequest, Assessment assessment, Integer topicId) {
        return topicAndParameterLevelAssessmentService.saveTopicRecommendation(recommendationRequest, assessment, topicId);
    }

    public ParameterLevelRecommendation updateParameterRecommendation(RecommendationRequest parameterLevelRecommendationRequest) {
        return topicAndParameterLevelAssessmentService.updateParameterRecommendation(parameterLevelRecommendationRequest);
    }

    public ParameterLevelRecommendation saveParameterRecommendation(RecommendationRequest parameterLevelRecommendationRequest, Assessment assessment, Integer parameterId) {
        return topicAndParameterLevelAssessmentService.saveParameterRecommendation(parameterLevelRecommendationRequest, assessment, parameterId);
    }

    public AssessmentResponse getAssessmentResponse(Assessment assessment) {

        List<Answer> answerResponse = getAnswers(assessment.getAssessmentId());

        List<UserQuestion> userQuestionList = getUserQuestions(assessment.getAssessmentId());

        List<TopicLevelRating> topicLevelRatingList = getTopicLevelRatings(assessment.getAssessmentId());
        List<TopicLevelRecommendation> topicLevelRecommendationList = getTopicRecommendations(assessment.getAssessmentId());
        List<TopicRatingAndRecommendation> topicRecommendationResponses = mergeTopicRatingAndRecommendation(topicLevelRatingList, topicLevelRecommendationList);
        List<ParameterLevelRating> parameterLevelRatingList = getParameterLevelRatings(assessment.getAssessmentId());

        List<ParameterLevelRecommendation> parameterLevelRecommendationList = getParameterRecommendations(assessment.getAssessmentId());
        List<ParameterRatingAndRecommendation> paramRecommendationResponses = mergeParamRatingAndRecommendation(parameterLevelRatingList, parameterLevelRecommendationList);


        return assessmentMapper.map(assessment, answerResponse, userQuestionList, topicRecommendationResponses, paramRecommendationResponses);
    }

    private List<ParameterRatingAndRecommendation> mergeParamRatingAndRecommendation(List<ParameterLevelRating> parameterLevelRatingList, List<ParameterLevelRecommendation> parameterLevelRecommendationList) {
        List<ParameterRatingAndRecommendation> parameterRatingAndRecommendationsResponse = new ArrayList<>();
        Set<Integer> processedParams = new HashSet<>();

        for (ParameterLevelRating paramLevelAssessment : parameterLevelRatingList) {
            Integer parameterId = paramLevelAssessment.getParameterLevelId().getParameter().getParameterId();
            processedParams.add(parameterId);
            ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
            parameterRatingAndRecommendation.setParameterId(parameterId);
            parameterRatingAndRecommendation.setRating(paramLevelAssessment.getRating());
            parameterRatingAndRecommendation.setParameterLevelRecommendationRequest(getParameterRecommendation(parameterLevelRecommendationList, parameterId));
            parameterRatingAndRecommendationsResponse.add(parameterRatingAndRecommendation);
        }

        Set<Integer> parameterIds = parameterLevelRecommendationList.stream()
                .map(paramLevelRecommendation -> paramLevelRecommendation.getParameter().getParameterId())
                .collect(toSet());

        for (Integer paramId : parameterIds) {
            if (!processedParams.contains(paramId)) {
                processedParams.add(paramId);
                ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();

                parameterRatingAndRecommendation.setParameterId(paramId);
                parameterRatingAndRecommendation.setParameterLevelRecommendationRequest(getParameterRecommendation(parameterLevelRecommendationList, paramId));
                parameterRatingAndRecommendationsResponse.add(parameterRatingAndRecommendation);
            }
        }
        return parameterRatingAndRecommendationsResponse;
    }

    private List<TopicRatingAndRecommendation> mergeTopicRatingAndRecommendation(List<TopicLevelRating> topicLevelRatingList, List<TopicLevelRecommendation> topicLevelRecommendationList) {
        List<TopicRatingAndRecommendation> topicRatingAndRecommendationsResponse = new ArrayList<>();
        Set<Integer> processedTopics = new HashSet<>();

        for (TopicLevelRating topicLevelRating : topicLevelRatingList) {
            Integer topicId = topicLevelRating.getTopicLevelId().getTopic().getTopicId();
            processedTopics.add(topicId);
            TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
            topicRatingAndRecommendation.setTopicId(topicId);
            topicRatingAndRecommendation.setRating(topicLevelRating.getRating());
            topicRatingAndRecommendation.setRecommendationRequest(getTopicRecommendation(topicLevelRecommendationList, topicId));
            topicRatingAndRecommendationsResponse.add(topicRatingAndRecommendation);
        }

        Set<Integer> topicIds = topicLevelRecommendationList.stream()
                .map(topicLevelRecommendation -> topicLevelRecommendation.getTopic().getTopicId())
                .collect(toSet());

        for (Integer topicId : topicIds) {
            if (!processedTopics.contains(topicId)) {
                processedTopics.add(topicId);
                TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
                topicRatingAndRecommendation.setTopicId(topicId);
                topicRatingAndRecommendation.setRecommendationRequest(getTopicRecommendation(topicLevelRecommendationList, topicId));
                topicRatingAndRecommendationsResponse.add(topicRatingAndRecommendation);
            }
        }
        return topicRatingAndRecommendationsResponse;
    }


    private List<RecommendationRequest> getTopicRecommendation(List<TopicLevelRecommendation> topicLevelRecommendationList, Integer topicId) {
        List<RecommendationRequest> recommendationRequests = new ArrayList<>();
        List<TopicLevelRecommendation> matchingList = topicLevelRecommendationList.stream().filter(topicLevelRecommendation -> topicId.equals(topicLevelRecommendation.getTopic().getTopicId())).toList();
        for (TopicLevelRecommendation topicLevelRecommendation : matchingList) {
            RecommendationRequest recommendationRequest = modelMapper.map(topicLevelRecommendation, RecommendationRequest.class);
            recommendationRequests.add(recommendationRequest);
        }
        return recommendationRequests;
    }

    private List<RecommendationRequest> getParameterRecommendation(List<ParameterLevelRecommendation> parameterLevelRecommendationList, Integer parameterId) {
        List<RecommendationRequest> parameterLevelRecommendationRequests = new ArrayList<>();
        List<ParameterLevelRecommendation> matchingList = parameterLevelRecommendationList.stream().filter(parameterLevelRecommendation -> parameterId.equals(parameterLevelRecommendation.getParameter().getParameterId())).toList();
        for (ParameterLevelRecommendation parameterLevelRecommendation : matchingList) {
            RecommendationRequest parameterLevelRecommendationRequest = modelMapper.map(parameterLevelRecommendation, RecommendationRequest.class);
            parameterLevelRecommendationRequests.add(parameterLevelRecommendationRequest);
        }
        return parameterLevelRecommendationRequests;
    }


    public List<AssessmentResponse> getAssessments(String userEmail) {
        List<Assessment> assessments = findAssessments(userEmail);
        List<AssessmentResponse> assessmentResponses = new ArrayList<>();
        if (Objects.nonNull(assessments))
            assessments.forEach(assessment ->
            {
                AssessmentResponse assessmentResponse = assessmentMapper.map(assessment);
                assessmentResponse.setAssessmentDescription(assessment.getAssessmentDescription());
                assessmentResponse.setOwner(userEmail.equals(assessment.getOwnerEmail()));
                assessmentResponses.add(assessmentResponse);
            });
        return assessmentResponses;
    }

    public Assessment setAssessment(Assessment assessment, AssessmentRequest assessmentRequest) {
        assessment.setAssessmentName(assessmentRequest.getAssessmentName());
        assessment.getOrganisation().setOrganisationName(assessmentRequest.getOrganisationName());
        assessment.getOrganisation().setDomain(assessmentRequest.getDomain());
        assessment.getOrganisation().setIndustry(assessmentRequest.getIndustry());
        assessment.getOrganisation().setSize(assessmentRequest.getTeamSize());
        assessment.setAssessmentPurpose(assessmentRequest.getAssessmentPurpose());
        assessment.setAssessmentDescription(assessmentRequest.getAssessmentDescription());
        return  assessment;
    }
}
