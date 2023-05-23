/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.ContributorController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.ModuleContributorService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static com.xact.assessment.dtos.ContributorRole.AUTHOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ContributorControllerTest {

    private final UserAuthService userAuthService = mock(UserAuthService.class);
    private final ModuleContributorService contributorService = mock(ModuleContributorService.class);
    private final ContributorController contributorController = new ContributorController(userAuthService, contributorService);
    private final Authentication authentication = Mockito.mock(Authentication.class);

    private final  User user = new User();
    @BeforeEach
    public void beforeEach() {

        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);;
    }

    @Test
    void shouldGetContributorQuestions() {
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);
        ContributorResponse contributorResponse = new ContributorResponse();
        ContributorModuleData contributorModuleData = new ContributorModuleData();
        contributorModuleData.setModuleName("Module");
        ContributorTopicData contributorTopicData = new ContributorTopicData();
        contributorTopicData.setTopicName("Topic");
        ContributorParameterData contributorParameterData = new ContributorParameterData();
        contributorParameterData.setParameterName("Parameter");
        ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
        contributorQuestionData.setQuestionId(1);
        contributorQuestionData.setQuestion("Question?");
        contributorQuestionData.setStatus(ContributorQuestionStatus.DRAFT);
        contributorParameterData.setQuestions(Collections.singletonList(contributorQuestionData));
        contributorTopicData.setParameters(Collections.singletonList(contributorParameterData));
        contributorModuleData.setTopics(Collections.singletonList(contributorTopicData));

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        when(contributorService.getContributorResponse(AUTHOR, userEmail)).thenReturn(contributorResponse);

        HttpResponse<ContributorResponse> actualResponse = contributorController.getContributorQuestions(AUTHOR, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void shouldUpdateContributorQuestionStatus() {
        Integer questionId = 1;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);
        QuestionStatusUpdateRequest questionStatusUpdateRequest = new QuestionStatusUpdateRequest();
        questionStatusUpdateRequest.setQuestionId(Collections.singletonList(questionId));
        questionStatusUpdateRequest.setComments("comments");
        QuestionStatusUpdateResponse questionStatusUpdateResponse = new QuestionStatusUpdateResponse();
        questionStatusUpdateResponse.setQuestionId(questionStatusUpdateRequest.getQuestionId());
        questionStatusUpdateResponse.setStatus(ContributorQuestionStatus.SENT_FOR_REVIEW);
        questionStatusUpdateResponse.setComments("comments");

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        when(contributorService.updateContributorQuestionsStatus(1, ContributorQuestionStatus.SENT_FOR_REVIEW, questionStatusUpdateRequest, userEmail)).thenReturn(questionStatusUpdateResponse);

        HttpResponse<QuestionStatusUpdateResponse> actualResponse = contributorController.updateContributorQuestionsStatus(1, ContributorQuestionStatus.SENT_FOR_REVIEW, questionStatusUpdateRequest, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldDeleteQuestion() {
        Integer questionId = 1;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        doNothing().when(contributorService).deleteQuestion(questionId, userEmail);

        HttpResponse<Question> actualResponse = contributorController.deleteQuestion(questionId, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void shouldSaveQuestion() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("question?");
        questionRequest.setParameter(1);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(1);
        category.setCategoryName("Category");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("Module");
        assessmentModule.setCategory(category);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic");
        assessmentTopic.setModule(assessmentModule);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter");
        assessmentParameter.setTopic(assessmentTopic);

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("question?");
        question.setParameter(assessmentParameter);

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);

        when(contributorService.createAssessmentQuestion(userEmail, questionRequest)).thenReturn(question);

        HttpResponse<QuestionResponse> actualQuestionResponse = contributorController.createQuestion(questionRequest, authentication);
        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualQuestionResponse.getStatus());


    }

    @Test
    void shouldUpdateQuestionText() {
        Integer questionId = 1;
        String questionText = "new question";
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setQuestionText(questionText);
        question.setParameter(parameter);


        when(contributorService.updateContributorQuestion(questionId, questionText, userEmail)).thenReturn(question);
        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);

        HttpResponse<QuestionDto> actualResponse = contributorController.updateQuestion(questionId, questionText, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void createAssessmentTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("Hello this is a topic");
        topicRequest.setActive(false);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Hello this is a topic");
        assessmentTopic.setActive(false);
        assessmentTopic.setModule(new AssessmentModule("hello", new AssessmentCategory("hello", false, ""), false, ""));

        when(contributorService.createAssessmentTopics(topicRequest)).thenReturn(assessmentTopic);
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);
        HttpResponse<TopicResponse> actualResponse = contributorController.createTopic(topicRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledCreateAssessmentTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("Hello this is a topic");
        topicRequest.setActive(false);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Hello this is a topic");
        assessmentTopic.setActive(false);
        assessmentTopic.setModule(new AssessmentModule("hello", new AssessmentCategory("hello", false, ""), false, ""));

        when(contributorService.createAssessmentTopics(topicRequest)).thenReturn(assessmentTopic);
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);
        HttpResponse<TopicResponse> actualResponse = contributorController.createTopic(topicRequest, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Parameter");
        parameterRequest.setActive(false);

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        when(contributorService.createAssessmentParameter(parameterRequest)).thenReturn(assessmentParameter);
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse<ParameterResponse> actualResponse = contributorController.createParameter(parameterRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledCreateAssessmentParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Parameter");
        parameterRequest.setActive(false);

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        when(contributorService.createAssessmentParameter(parameterRequest)).thenReturn(assessmentParameter);
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse<ParameterResponse> actualResponse = contributorController.createParameter(parameterRequest, authentication);
        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }


    @Test
    void createAssessmentTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("references");
        referencesRequest.setRating(Rating.FIVE);
        referencesRequest.setTopic(1);

        when(contributorService.createAssessmentTopicReference(referencesRequest)).thenReturn(new AssessmentTopicReference( Rating.FIVE, "reference",new AssessmentTopic()));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse<AssessmentTopicReferenceDto> actualResponse = contributorController.createTopicReference(referencesRequest, authentication);
        assertEquals(actualResponse.getStatus(), HttpResponse.ok().getStatus());
    }
    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledCreateAssessmentTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("references");
        referencesRequest.setRating(Rating.FIVE);
        referencesRequest.setTopic(1);

        when(contributorService.createAssessmentTopicReference(referencesRequest)).thenReturn(new AssessmentTopicReference( Rating.FIVE, "reference",new AssessmentTopic()));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse<AssessmentTopicReferenceDto> actualResponse = contributorController.createTopicReference(referencesRequest, authentication);
        assertEquals( HttpResponse.unauthorized().getStatus(),actualResponse.getStatus());
    }

    @Test
    void createParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("References");
        referencesRequest.setRating(Rating.FIVE);
        referencesRequest.setParameter(1);

        when(contributorService.createAssessmentParameterReference(referencesRequest)).thenReturn(new AssessmentParameterReference( Rating.FIVE, "reference",new AssessmentParameter()));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse<AssessmentParameterReferenceDto> actualResponse = contributorController.createParameterReference(referencesRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
    @Test
    void ShouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledCreateParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("References");
        referencesRequest.setRating(Rating.FIVE);
        referencesRequest.setParameter(1);

        when(contributorService.createAssessmentParameterReference(referencesRequest)).thenReturn(new AssessmentParameterReference( Rating.FIVE, "reference",new AssessmentParameter()));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse<AssessmentParameterReferenceDto> actualResponse = contributorController.createParameterReference(referencesRequest, authentication);
        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createQuestionReferences() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = Question.builder().questionId(1).questionText("question").parameter(parameter).build();

        QuestionReferenceRequest referencesRequest = new QuestionReferenceRequest();
        referencesRequest.setReference("References");
        referencesRequest.setRating(Rating.FIVE);
        referencesRequest.setQuestion(1);

        when(contributorService.getQuestionById(question.getQuestionId())).thenReturn(question);
        when(contributorService.createAssessmentQuestionReference(referencesRequest)).thenReturn(new AssessmentQuestionReference( Rating.FIVE, "reference",question));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse<AssessmentQuestionReferenceDto> actualResponse = contributorController.createQuestionReference(referencesRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledCreateQuestionReferences() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = Question.builder().questionId(1).questionText("question").parameter(parameter).build();

        QuestionReferenceRequest referencesRequest = new QuestionReferenceRequest();
        referencesRequest.setReference("References");
        referencesRequest.setRating(Rating.FIVE);
        referencesRequest.setQuestion(1);

        when(contributorService.getQuestionById(question.getQuestionId())).thenReturn(question);
        when(contributorService.createAssessmentQuestionReference(referencesRequest)).thenReturn(new AssessmentQuestionReference( Rating.FIVE, "reference",question));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse<AssessmentQuestionReferenceDto> actualResponse = contributorController.createQuestionReference(referencesRequest, authentication);
        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }
    @Test
    void shouldUpdateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("Module");
        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("this is a module");
        assessmentTopic.setModule(new AssessmentModule("hello", new AssessmentCategory("hello", false, ""), false, ""));


        when(contributorService.updateTopic(1, topicRequest)).thenReturn(assessmentTopic);
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse actualResponse = contributorController.updateTopic(topicId, topicRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledUpdateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("Module");
        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("this is a module");
        assessmentTopic.setModule(new AssessmentModule("hello", new AssessmentCategory("hello", false, ""), false, ""));


        when(contributorService.updateTopic(1, topicRequest)).thenReturn(assessmentTopic);
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse actualResponse = contributorController.updateTopic(topicId, topicRequest, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Module");

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).comments("").build();


        when(contributorService.updateParameter(assessmentParameter.getParameterId(), parameterRequest)).thenReturn(assessmentParameter);
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse actualResponse = contributorController.updateParameter(assessmentParameter.getParameterId(), parameterRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledUpdateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Module");

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).comments("").build();


        when(contributorService.updateParameter(assessmentParameter.getParameterId(), parameterRequest)).thenReturn(assessmentParameter);
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse actualResponse = contributorController.updateParameter(assessmentParameter.getParameterId(), parameterRequest, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateQuestion() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("question");
        Integer questionId = 1;

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = new Question("Text", parameter);

        when(contributorService.updateQuestion(questionId, questionRequest)).thenReturn(question);

        HttpResponse<QuestionResponse> actualResponse = contributorController.updateQuestion(questionId, questionRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentTopicReference topicReference = new AssessmentTopicReference();
        topicReference.setReference("Hello");

        when(contributorService.updateTopicReference(referenceId, referencesRequest)).thenReturn(new AssessmentTopicReference( Rating.FIVE, "reference",new AssessmentTopic()));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse actualResponse = contributorController.updateTopicReference(referenceId, referencesRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledUpdateTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentTopicReference topicReference = new AssessmentTopicReference();
        topicReference.setReference("Hello");

        when(contributorService.updateTopicReference(referenceId, referencesRequest)).thenReturn(new AssessmentTopicReference( Rating.FIVE, "reference",new AssessmentTopic()));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse actualResponse = contributorController.updateTopicReference(referenceId, referencesRequest, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }
    @Test
    void shouldUpdateParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentParameterReference parameterReference = new AssessmentParameterReference();
        parameterReference.setReference("Hello");

        when(contributorService.updateParameterReference(referenceId, referencesRequest)).thenReturn(new AssessmentParameterReference( Rating.FIVE, "reference",new AssessmentParameter()));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse actualResponse = contributorController.updateParameterReference(referenceId, referencesRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledUpdateParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentParameterReference parameterReference = new AssessmentParameterReference();
        parameterReference.setReference("Hello");

        when(contributorService.updateParameterReference(referenceId, referencesRequest)).thenReturn(new AssessmentParameterReference( Rating.FIVE, "reference",new AssessmentParameter()));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse actualResponse = contributorController.updateParameterReference(referenceId, referencesRequest, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateQuestionReferences() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = Question.builder().questionId(1).questionText("question").parameter(parameter).build();

        QuestionReferenceRequest questionReferenceRequest = new QuestionReferenceRequest();
        questionReferenceRequest.setReference("reference");
        questionReferenceRequest.setQuestion(question.getQuestionId());
        Integer referenceId = 1;
        AssessmentQuestionReference assessmentQuestionReference=new AssessmentQuestionReference();
        assessmentQuestionReference.setReference("reference of last question");

        when(contributorService.getQuestionById(question.getQuestionId())).thenReturn(question);
        when(contributorService.updateQuestionReference(referenceId, questionReferenceRequest,question)).thenReturn(new AssessmentQuestionReference( Rating.FIVE, "reference",question));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse<AssessmentQuestionReferenceDto> actualResponse = contributorController.updateQuestionReference(referenceId, questionReferenceRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledUpdateQuestionReferences() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = Question.builder().questionId(1).questionText("question").parameter(parameter).build();

        QuestionReferenceRequest questionReferenceRequest = new QuestionReferenceRequest();
        questionReferenceRequest.setReference("reference");
        questionReferenceRequest.setQuestion(question.getQuestionId());
        Integer referenceId = 1;
        AssessmentQuestionReference assessmentQuestionReference=new AssessmentQuestionReference();
        assessmentQuestionReference.setReference("reference of last question");

        when(contributorService.getQuestionById(question.getQuestionId())).thenReturn(question);
        when(contributorService.updateQuestionReference(referenceId, questionReferenceRequest,question)).thenReturn(new AssessmentQuestionReference( Rating.FIVE, "reference",question));
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse<AssessmentQuestionReferenceDto> actualResponse = contributorController.updateQuestionReference(referenceId, questionReferenceRequest, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }
    @Test
    void shouldDeleteTopicReference() {
        Integer referenceId = 10;
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);

        HttpResponse actualResponse = contributorController.deleteTopicReference(referenceId, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        verify(contributorService).deleteTopicReference(referenceId);
    }

    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledDeleteTopicReference() {
        Integer referenceId = 10;
        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);

        HttpResponse actualResponse = contributorController.deleteTopicReference(referenceId, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldDeleteParameterReference() {
        Integer referenceId = 10;

        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);


        HttpResponse actualResponse = contributorController.deleteParameterReference(referenceId, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        verify(contributorService).deleteParameterReference(referenceId);
    }
    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledDeleteParameterReference() {
        Integer referenceId = 10;

        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);
        when(contributorService.getModuleByParameterReference(referenceId)).thenReturn(new AssessmentModule());

        HttpResponse<ParameterReferencesRequest> actualResponse = contributorController.deleteParameterReference(referenceId, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }
    @Test
    void shouldDeleteQuestionReference() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = Question.builder().questionId(1).questionText("question").parameter(parameter).build();

        Integer referenceId = 1;

        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(true);
        when(contributorService.getModuleByQuestionReference(referenceId)).thenReturn(assessmentModule);

        HttpResponse<QuestionReferenceRequest> actualResponse = contributorController.deleteQuestionReference(referenceId, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        verify(contributorService).deleteQuestionReference(referenceId);
    }

    @Test
    void shouldReturnUnAuthorizedResponseWhenInvalidContributorIsCalledDelete() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = Question.builder().questionId(1).questionText("question").parameter(parameter).build();

        Integer referenceId = 1;

        when(contributorService.validate(any(User.class),any(AssessmentModule.class))).thenReturn(false);
        when(contributorService.getModuleByQuestionReference(referenceId)).thenReturn(assessmentModule);

        HttpResponse<QuestionReferenceRequest> actualResponse = contributorController.deleteQuestionReference(referenceId, authentication);

        assertEquals(HttpResponse.unauthorized().getStatus(), actualResponse.getStatus());
    }
}
