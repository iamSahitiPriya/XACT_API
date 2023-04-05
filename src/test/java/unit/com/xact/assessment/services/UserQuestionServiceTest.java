package unit.com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.UserQuestionRepository;
import com.xact.assessment.services.ParameterService;
import com.xact.assessment.services.UserQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserQuestionServiceTest {

    UserQuestionService userQuestionService;
    ParameterService parameterService;
    UserQuestionRepository userQuestionRepository;

    @BeforeEach
    public void beforeEach() {
        userQuestionRepository = mock(UserQuestionRepository.class);
        parameterService = mock(ParameterService.class);
        userQuestionService = new UserQuestionService(userQuestionRepository,parameterService);
    }

    @Test
    void shouldBeAbleToSaveWholeUserQuestion() {
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");


        Organisation organisation = new Organisation();
        organisation.setOrganisationId(1);
        organisation.setIndustry("new");
        organisation.setOrganisationName("org");
        organisation.setDomain("domain");

        assessment.setOrganisation(organisation);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        String questionText = "new question ?";
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion(questionText);
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");
        when(userQuestionRepository.save(userQuestion)).thenReturn(userQuestion);
        userQuestionService.saveUserQuestion(userQuestion);
        UserQuestion expectedUserQuestion = new UserQuestion(1, assessment, assessmentParameter, "new question ?", "answer", created1, updated1,false);

        assertEquals(expectedUserQuestion.getQuestion(), userQuestion.getQuestion());
    }

    @Test
    void shouldBeAbleToUpdateWholeUserQuestion() {
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");


        Organisation organisation = new Organisation();
        organisation.setOrganisationId(1);
        organisation.setIndustry("new");
        organisation.setOrganisationName("org");
        organisation.setDomain("domain");

        assessment.setOrganisation(organisation);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        String questionText = "new question ?";
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion(questionText);
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");
        userQuestionService.saveUserQuestion(userQuestion);

        UserQuestion userQuestion1 = new UserQuestion();
        userQuestion1.setQuestionId(1);
        userQuestion1.setQuestion("update?");
        userQuestion1.setParameter(assessmentParameter);
        userQuestion1.setAssessment(assessment);
        userQuestion1.setAnswer("answer");
        when(userQuestionRepository.save(userQuestion1)).thenReturn(userQuestion1);
        userQuestionService.saveUserQuestion(userQuestion1);
        UserQuestion expectedUserQuestion = new UserQuestion(1, assessment, assessmentParameter, "update?", "answer", created1, updated1,false);

        assertEquals(expectedUserQuestion.getQuestion(), userQuestion1.getQuestion());


    }

    @Test
    void shouldBeAbleToGetAllUSerAddedQuestions(){
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question Text?");
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");

        when(userQuestionRepository.findByAssessmentId(1)).thenReturn(Collections.singletonList(userQuestion));

        List<UserQuestion> userQuestionList= userQuestionService.getUserQuestions(assessment.getAssessmentId());

        assertEquals(1,userQuestionList.size());

    }
    @Test
    void shouldDeleteUserAddedQuestionAndAnswer() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question Text?");
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");
        userQuestionRepository.save(userQuestion);

        userQuestionService.deleteUserQuestion(userQuestion.getQuestionId());

        verify(userQuestionRepository).deleteById(userQuestion.getQuestionId());

    }

    @Test
    void shouldBeAbleToSaveUserQuestionAtTimeOfCreation() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question Text?");
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");

        when(userQuestionRepository.save(userQuestion)).thenReturn(userQuestion);
        when(parameterService.getParameter(1)).thenReturn(Optional.of(assessmentParameter));

        UserQuestion expectedUserQuestionResponse = userQuestionService.saveUserQuestion(assessment,assessmentParameter.getParameterId(),"question Text?");

        assertEquals(expectedUserQuestionResponse.getQuestion(),userQuestion.getQuestion());

    }

    @Test
    void shouldBeAbleToUpdateUserQuestionText() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question Text?");
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");

        when(userQuestionRepository.findById(userQuestion.getQuestionId())).thenReturn(Optional.of(userQuestion));

        userQuestion.setQuestion("updated question Text?");
        when(userQuestionRepository.update(userQuestion)).thenReturn(userQuestion);

        UserQuestion expectedUserQuestionResponse = userQuestionService.updateUserQuestion(userQuestion.getQuestionId(),"updated question Text?");

        assertEquals(expectedUserQuestionResponse.getQuestion(),userQuestion.getQuestion());

    }

    @Test
    void shouldBeAbleToUpdateUserAnswerText() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question Text?");
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);

        when(userQuestionRepository.findById(userQuestion.getQuestionId())).thenReturn(Optional.of(userQuestion));

        userQuestion.setAnswer("answer Text?");
        when(userQuestionRepository.update(userQuestion)).thenReturn(userQuestion);

        UserQuestion expectedUserQuestionResponse = userQuestionService.saveUserAnswer(userQuestion.getQuestionId(),"answer Text");

        assertEquals(expectedUserQuestionResponse.getAnswer(),userQuestion.getAnswer());

    }


}
