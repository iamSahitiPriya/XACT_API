package unit.com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AnswerRepository;
import com.xact.assessment.repositories.UserQuestionRepository;
import com.xact.assessment.services.AnswerService;
import com.xact.assessment.services.UserQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserQuestionServiceTest {

    UserQuestionService userQuestionService;
    UserQuestionRepository userQuestionRepository;

    @BeforeEach
    public void beforeEach() {
        userQuestionRepository = mock(UserQuestionRepository.class);
        userQuestionService = new UserQuestionService(userQuestionRepository);
    }
    @Test
    void shouldBeAbleToSaveQuestionText(){
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
        userQuestion.setQuestionId("1");
        userQuestion.setQuestion(questionText);
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");
        when(userQuestionRepository.save(userQuestion)).thenReturn(userQuestion);
        userQuestionService.saveUserQuestion(assessment,assessmentParameter,questionText,"answer");
        UserQuestion expectedUserQuestion = new UserQuestion("1",assessment,assessmentParameter,"new question ?" , "answer",created1,updated1);

        assertEquals(expectedUserQuestion.getQuestion(),userQuestion.getQuestion());


    }
}
