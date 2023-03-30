package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.QuestionRepository;
import com.xact.assessment.services.ModuleContributorService;
import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.UserQuestionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.xact.assessment.dtos.ContributorQuestionStatus.Published;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class QuestionServiceTest {
    private QuestionRepository questionRepository;
    private QuestionService questionService;
    private UserQuestionService userQuestionService;
    private ModuleContributorService moduleContributorService;

    @BeforeEach
    public void beforeEach() {
        questionRepository = mock(QuestionRepository.class);
        userQuestionService = mock(UserQuestionService.class);
        moduleContributorService = mock(ModuleContributorService.class);

        questionService = new QuestionService(questionRepository, userQuestionService, moduleContributorService);

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
    void shouldSaveAdminQuestion() {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");
        question.setParameter(parameter);
        when(questionRepository.save(question)).thenReturn(question);
        when(moduleContributorService.getRole(1,"hello@thoughtworks.com")).thenReturn(Optional.empty());
        questionService.createQuestion("hello@thoughtworks.com",question);
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
        when(moduleContributorService.getRole(1,"hello@thoughtworks.com")).thenReturn(Optional.of(ContributorRole.Author));
        questionService.createQuestion("hello@thoughtworks.com",question);
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
        question.setQuestionStatus(ContributorQuestionStatus.Draft);
        question.setQuestionText("Question?");
        question.setParameter(assessmentParameter);

        assessmentParameter.setQuestions(Collections.singleton(question));

        when(moduleContributorService.getModuleByRole("email@thoughtworks.com", ContributorRole.Author)).thenReturn(Collections.singletonList(assessmentModule));
        when(questionRepository.getAuthorQuestions(assessmentModule.getModuleId())).thenReturn(Collections.singletonList(question));

        ContributorResponse actualContributorResponse = questionService.getContributorResponse(ContributorRole.Author,"email@thoughtworks.com");

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
        contributorQuestionData.setStatus(ContributorQuestionStatus.Draft);
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
        question.setQuestionStatus(Published);
        question.setQuestionText("Question?");
        question.setParameter(assessmentParameter);

        assessmentParameter.setQuestions(Collections.singleton(question));

        when(moduleContributorService.getModuleByRole("email@thoughtworks.com", ContributorRole.Reviewer)).thenReturn(Collections.singletonList(assessmentModule));
        when(questionRepository.getReviewerQuestions(assessmentModule.getModuleId())).thenReturn(Collections.singletonList(question));

        ContributorResponse actualContributorResponse = questionService.getContributorResponse(ContributorRole.Reviewer,"email@thoughtworks.com");

        ContributorResponse expectedResponse = getExpectedResponse();
        expectedResponse.getContributorModuleData().get(0).getTopics().get(0).getParameters().get(0).getQuestions().get(0).setStatus(Published);

        assertEquals(expectedResponse, actualContributorResponse);
    }


    @Test
    void shouldSaveUserQuestions() {
        UserQuestion userQuestion=new UserQuestion();
        userQuestion.setContributionStatus(false);

        questionService.save(Collections.singletonList(userQuestion));
        verify(userQuestionService).updateUserQuestion(userQuestion);
    }

    @Test
    void shouldGetAllQuestions() {
        Question question=new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        List<Question> questionList=new ArrayList<>();
        questionList.add(question);
        when(questionRepository.findAll()).thenReturn(questionList);
        List<Question> questionListResponse=questionService.getAllQuestion();
        assertEquals(questionListResponse,questionList);
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
        question.setQuestionStatus(ContributorQuestionStatus.Draft);
        question.setParameter(assessmentParameter);
        when(moduleContributorService.getRole(1,"hello@thoughtworks.com")).thenReturn(Optional.of(ContributorRole.Author));
        when(questionRepository.findById(1)).thenReturn(Optional.of(question));

        questionService.deleteQuestion(1,"hello@thoughtworks.com");
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

        Question question=new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        question.setQuestionStatus(ContributorQuestionStatus.Draft);
        question.setParameter(assessmentParameter);
        when(questionRepository.findById(1)).thenReturn(Optional.of(question));
        when(moduleContributorService.getRole(1,"hello@thoughtworks.com")).thenReturn(Optional.of(ContributorRole.Author));
        questionService.updateContributorQuestion(1,"editedQuestion?","hello@thoughtworks.com");

        String expectedQuestionText = "editedQuestion?";
        String actualQuestionText = question.getQuestionText();

        assertEquals(expectedQuestionText,actualQuestionText);

    }

    @Test
    void shouldUpdateContributorQuestionStatus() {
        AssessmentModule assessmentModule=new AssessmentModule();
        assessmentModule.setModuleId(1);
        String userEmail="smss@thoughtworks.com";
        Question question=new Question();
        question.setQuestionId(1);
        question.setQuestionStatus(ContributorQuestionStatus.Draft);
        QuestionStatusUpdateRequest questionStatusUpdateRequest=new QuestionStatusUpdateRequest();
        questionStatusUpdateRequest.setQuestionId(Collections.singletonList(question.getQuestionId()));
        questionStatusUpdateRequest.setComments("comments");

        when(moduleContributorService.getRole(assessmentModule.getModuleId(),userEmail)).thenReturn(Optional.of(ContributorRole.Author));
        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));

        QuestionStatusUpdateResponse actualResponse = questionService.updateContributorQuestionsStatus(assessmentModule.getModuleId(),ContributorQuestionStatus.Sent_For_Review,questionStatusUpdateRequest,userEmail);

        Assertions.assertEquals(ContributorQuestionStatus.Sent_For_Review,actualResponse.getStatus());

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

        Question question=new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        question.setQuestionStatus(ContributorQuestionStatus.Draft);
        question.setParameter(assessmentParameter);
        when(questionRepository.findById(1)).thenReturn(Optional.of(question));
        when(moduleContributorService.getRole(1,"hello@thoughtworks.com")).thenReturn(Optional.of(ContributorRole.Reviewer));
        questionService.updateContributorQuestion(1,"editedQuestion?","hello@thoughtworks.com");


        assertEquals(Published,question.getQuestionStatus());

    }
}
