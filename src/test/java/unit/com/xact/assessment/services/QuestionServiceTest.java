package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.QuestionRepository;
import com.xact.assessment.services.ModuleContributorService;
import com.xact.assessment.services.ModuleService;
import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.UserQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    void shouldSaveQuestions() {
        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");
        when(questionRepository.save(question)).thenReturn(question);
        questionService.createQuestion(question);
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
        question.setQuestionStatus(ContributorQuestionStatus.Idle);
        question.setQuestionText("Question?");
        question.setParameter(assessmentParameter);

        assessmentParameter.setQuestions(Collections.singleton(question));

        when(moduleContributorService.getModuleByRole("email@thoughtworks.com", ContributorRole.Author)).thenReturn(Collections.singletonList(assessmentModule));
        when(questionRepository.getAuthorQuestions()).thenReturn(Collections.singletonList(question));

        ContributorDataResponse actualContributorDataResponse = questionService.getContributorQuestions(ContributorRole.Author,"email@thoughtworks.com");

        ContributorDataResponse expectedResponse = getAuthorExpectedResponse();

        assertEquals(expectedResponse,actualContributorDataResponse);
    }

    private ContributorDataResponse getAuthorExpectedResponse() {
        ContributorDataResponse expectedResponse = new ContributorDataResponse();
        ContributorCategoryData contributorCategoryData  = new ContributorCategoryData();
        contributorCategoryData.setCategoryName("Category");
        ContributorModuleData contributorModuleData = new ContributorModuleData();
        contributorModuleData.setModuleName("Module");
        ContributorTopicData contributorTopicData = new ContributorTopicData();
        contributorTopicData.setTopicName("Topic");
        ContributorParameterData contributorParameterData = new ContributorParameterData();
        contributorParameterData.setParameterName("Parameter");
        ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
        contributorQuestionData.setQuestionId(1);
        contributorQuestionData.setQuestion("Question?");
        contributorQuestionData.setContributorQuestionStatus(ContributorQuestionStatus.Idle);
        contributorParameterData.setContributorQuestionDataList(Collections.singletonList(contributorQuestionData));
        contributorTopicData.setContributorParameterDataList(Collections.singletonList(contributorParameterData));
        contributorModuleData.setContributorTopicDataList(Collections.singletonList(contributorTopicData));
        contributorCategoryData.setContributorModuleData(Collections.singletonList(contributorModuleData));
        expectedResponse.setContributorCategoryDataList(Collections.singletonList(contributorCategoryData));
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
        question.setQuestionStatus(ContributorQuestionStatus.Approved);
        question.setQuestionText("Question?");
        question.setParameter(assessmentParameter);

        assessmentParameter.setQuestions(Collections.singleton(question));

        when(moduleContributorService.getModuleByRole("email@thoughtworks.com", ContributorRole.Reviewer)).thenReturn(Collections.singletonList(assessmentModule));
        when(questionRepository.getReviewerQuestions()).thenReturn(Collections.singletonList(question));

        ContributorDataResponse actualContributorDataResponse = questionService.getContributorQuestions(ContributorRole.Reviewer,"email@thoughtworks.com");

        ContributorDataResponse expectedResponse = getReviewerExpectedResponse();

        assertEquals(expectedResponse,actualContributorDataResponse);
    }

    private ContributorDataResponse getReviewerExpectedResponse() {
        ContributorDataResponse expectedResponse = new ContributorDataResponse();
        ContributorCategoryData contributorCategoryData  = new ContributorCategoryData();
        contributorCategoryData.setCategoryName("Category");
        ContributorModuleData contributorModuleData = new ContributorModuleData();
        contributorModuleData.setModuleName("Module");
        ContributorTopicData contributorTopicData = new ContributorTopicData();
        contributorTopicData.setTopicName("Topic");
        ContributorParameterData contributorParameterData = new ContributorParameterData();
        contributorParameterData.setParameterName("Parameter");
        ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
        contributorQuestionData.setQuestionId(1);
        contributorQuestionData.setQuestion("Question?");
        contributorQuestionData.setContributorQuestionStatus(ContributorQuestionStatus.Approved);
        contributorParameterData.setContributorQuestionDataList(Collections.singletonList(contributorQuestionData));
        contributorTopicData.setContributorParameterDataList(Collections.singletonList(contributorParameterData));
        contributorModuleData.setContributorTopicDataList(Collections.singletonList(contributorTopicData));
        contributorCategoryData.setContributorModuleData(Collections.singletonList(contributorModuleData));
        expectedResponse.setContributorCategoryDataList(Collections.singletonList(contributorCategoryData));
        return expectedResponse;
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
}