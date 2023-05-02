/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.QuestionRepository;
import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.UserQuestionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.xact.assessment.dtos.ContributorQuestionStatus.DRAFT;
import static com.xact.assessment.dtos.ContributorQuestionStatus.PUBLISHED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class QuestionServiceTest {
    private QuestionRepository questionRepository;
    private QuestionService questionService;
    private UserQuestionService userQuestionService;

    @BeforeEach
    public void beforeEach() {
        questionRepository = mock(QuestionRepository.class);
        userQuestionService = mock(UserQuestionService.class);

        questionService = new QuestionService(questionRepository, userQuestionService);

    }

    @Test
    void shouldGetDetailsForQuestionId() {
        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        Optional<Question> actualQuestion = questionService.getQuestion(questionId);
        assertEquals(question.getQuestionId(), actualQuestion.get().getQuestionId());
        assertEquals(question.getQuestionText(), actualQuestion.get().getQuestionText());

    }

    @Test
    void shouldSaveQuestionWhenContributorRoleIsAuthor() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");
        question.setParameter(parameter);
        question.setQuestionStatus(DRAFT);
        when(questionRepository.save(question)).thenReturn(question);
        questionService.createQuestion(Optional.of(ContributorRole.AUTHOR), question);
        verify(questionRepository).save(question);
    }

    @Test
    void shouldSaveAuthorQuestions() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");
        question.setParameter(parameter);
        when(questionRepository.save(question)).thenReturn(question);
        questionService.createQuestion(Optional.of(ContributorRole.AUTHOR), question);
        verify(questionRepository).save(question);
    }

    @Test
    void shouldUpdateQuestions() {
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");
        when(questionRepository.update(question)).thenReturn(question);
        questionService.updateQuestion(question);
        verify(questionRepository).update(question);
    }

    @Test
    void shouldGetAuthorQuestions() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);
        assessmentCategory.setCategoryName("Category");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("Module");
        assessmentModule.setCategory(assessmentCategory);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic");
        assessmentTopic.setModule(assessmentModule);

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter");
        assessmentParameter.setTopic(assessmentTopic);

        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setQuestionText("Question?");
        question.setParameter(assessmentParameter);

        assessmentParameter.setQuestions(Collections.singleton(question));

        when(questionRepository.getAuthorQuestions(assessmentModule.getModuleId())).thenReturn(Collections.singletonList(question));

        ContributorResponse actualContributorResponse = questionService.getContributorResponse(ContributorRole.AUTHOR, Collections.singletonList(assessmentModule));

        ContributorResponse expectedResponse = getExpectedResponse();

        assertEquals(expectedResponse, actualContributorResponse);
    }

    private ContributorResponse getExpectedResponse() {
        ContributorResponse expectedResponse = new ContributorResponse();
        ContributorModuleData contributorModuleData = new ContributorModuleData();
        contributorModuleData.setModuleName("Module");
        contributorModuleData.setCategoryName("Category");
        contributorModuleData.setCategoryId(1);
        contributorModuleData.setModuleId(1);
        ContributorTopicData contributorTopicData = new ContributorTopicData();
        contributorTopicData.setTopicId(1);
        contributorTopicData.setTopicName("Topic");
        ContributorParameterData contributorParameterData = new ContributorParameterData();
        contributorParameterData.setParameterId(1);
        contributorParameterData.setParameterName("Parameter");
        ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
        contributorQuestionData.setQuestionId(1);
        contributorQuestionData.setQuestion("Question?");
        contributorQuestionData.setStatus(ContributorQuestionStatus.DRAFT);
        contributorParameterData.setQuestions(Collections.singletonList(contributorQuestionData));
        contributorTopicData.setParameters(Collections.singletonList(contributorParameterData));
        contributorModuleData.setTopics(Collections.singletonList(contributorTopicData));
        expectedResponse.setContributorModuleData(Collections.singletonList(contributorModuleData));
        return expectedResponse;
    }

    @Test
    void shouldGetReviewerQuestions() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);
        assessmentCategory.setCategoryName("Category");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("Module");
        assessmentModule.setCategory(assessmentCategory);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic");
        assessmentTopic.setModule(assessmentModule);

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter");
        assessmentParameter.setTopic(assessmentTopic);

        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionStatus(PUBLISHED);
        question.setQuestionText("Question?");
        question.setParameter(assessmentParameter);

        assessmentParameter.setQuestions(Collections.singleton(question));

        when(questionRepository.getReviewerQuestions(assessmentModule.getModuleId())).thenReturn(Collections.singletonList(question));

        ContributorResponse actualContributorResponse = questionService.getContributorResponse(ContributorRole.REVIEWER, Collections.singletonList(assessmentModule));

        ContributorResponse expectedResponse = getExpectedResponse();
        expectedResponse.getContributorModuleData().get(0).getTopics().get(0).getParameters().get(0).getQuestions().get(0).setStatus(PUBLISHED);

        assertEquals(expectedResponse, actualContributorResponse);
    }


    @Test
    void shouldSaveUserQuestions() {
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setContributionStatus(false);

        questionService.save(Collections.singletonList(userQuestion));
        verify(userQuestionService).updateUserQuestion(userQuestion);
    }

    @Test
    void shouldGetAllQuestions() {
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);
        when(questionRepository.findAll()).thenReturn(questionList);
        List<Question> questionListResponse = questionService.getAllQuestions();
        assertEquals(questionListResponse, questionList);
    }

    @Test
    void shouldDeleteContributorQuestion() {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setTopic(assessmentTopic);

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentParameter);
        when(questionRepository.findById(1)).thenReturn(Optional.of(question));

        questionService.deleteQuestion(question, Optional.of(ContributorRole.AUTHOR));
        verify(questionRepository).delete(question);


    }

    @Test
    void shouldUpdateContributorQuestion() {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setTopic(assessmentTopic);

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentParameter);
        when(questionRepository.findById(1)).thenReturn(Optional.of(question));
        questionService.updateContributorQuestion(question, "editedQuestion?", Optional.of(ContributorRole.AUTHOR));

        String expectedQuestionText = "editedQuestion?";
        String actualQuestionText = question.getQuestionText();

        assertEquals(expectedQuestionText, actualQuestionText);

    }

    @Test
    void shouldUpdateContributorQuestionStatus() {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        String userEmail = "smss@thoughtworks.com";
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        QuestionStatusUpdateRequest questionStatusUpdateRequest = new QuestionStatusUpdateRequest();
        questionStatusUpdateRequest.setQuestionId(Collections.singletonList(question.getQuestionId()));
        questionStatusUpdateRequest.setComments("comments");

        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));

        QuestionStatusUpdateResponse actualResponse = questionService.updateContributorQuestionsStatus(ContributorQuestionStatus.SENT_FOR_REVIEW, questionStatusUpdateRequest, Optional.of(ContributorRole.AUTHOR));

        Assertions.assertEquals(ContributorQuestionStatus.SENT_FOR_REVIEW, actualResponse.getStatus());

    }

    @Test
    void shouldUpdateContributorQuestionForReviewer() {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setTopic(assessmentTopic);

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentParameter);
        when(questionRepository.findById(1)).thenReturn(Optional.of(question));
        questionService.updateContributorQuestion(question, "editedQuestion?", Optional.of(ContributorRole.REVIEWER));


        assertEquals(PUBLISHED, question.getQuestionStatus());

    }

    @Test
    void shouldSaveQuestion() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");
        question.setParameter(parameter);
        question.setQuestionStatus(DRAFT);
        when(questionRepository.save(question)).thenReturn(question);
        questionService.createQuestion(Optional.of(ContributorRole.REVIEWER), question);

        verify(questionRepository, never()).save(question);
    }

}
