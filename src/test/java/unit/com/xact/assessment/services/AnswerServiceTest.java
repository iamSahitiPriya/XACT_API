package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AnswerRequest;
import com.xact.assessment.models.Answer;
import com.xact.assessment.models.AnswerId;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.repositories.AnswerRepository;
import com.xact.assessment.services.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnswerServiceTest {

    private AnswerRepository answerRepository;
    private AnswerService answerService;
    private ModelMapper mapper = new ModelMapper();


    @BeforeEach
    public void beforeEach() {
        answerRepository = mock(AnswerRepository.class);
        answerService = new AnswerService(answerRepository);
    }

    @Test
    void shouldSaveAssessmentNotes() {

        Integer assessmentId = 1;
        AnswerRequest answerRequest = new AnswerRequest(1, "some text");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        AnswerId answerId = mapper.map(answerRequest, AnswerId.class);
        answerId.setAssessment(assessment);
        Answer answer = mapper.map(answerRequest, Answer.class);
        answer.setAnswerId(answerId);

        when(answerRepository.save(answer)).thenReturn(answer);
        Answer actualAnswer = answerService.saveAnswer(answer);

        assertEquals(answer.getAnswer(), actualAnswer.getAnswer());
        assertEquals(answer.getAnswerId(), actualAnswer.getAnswerId());

    }

    @Test
    void shouldUpdateAssessmentNotes() {

        Integer assessmentId1 = 1;
        AnswerRequest answerRequest1 = new AnswerRequest(1, "some text");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        AnswerId answerId1 = mapper.map(answerRequest1, AnswerId.class);
        answerId1.setAssessment(assessment1);
        Answer answer1 = mapper.map(answerRequest1, Answer.class);
        answer1.setAnswerId(answerId1);

        answerRepository.save(answer1);

        Integer assessmentId2 = 1;
        AnswerRequest answerRequest2 = new AnswerRequest(1, "updated text");

        Assessment assessment2 = new Assessment();
        assessment2.setAssessmentId(assessmentId2);

        AnswerId answerId2 = mapper.map(answerRequest2, AnswerId.class);
        answerId2.setAssessment(assessment2);
        Answer answer2 = mapper.map(answerRequest2, Answer.class);
        answer2.setAnswerId(answerId2);

        when(answerRepository.update(answer2)).thenReturn(answer2);
        Answer actualAnswer = answerService.saveAnswer(answer2);

        assertEquals(answer2.getAnswer(), actualAnswer.getAnswer());
        assertEquals(answer2.getAnswerId(), actualAnswer.getAnswerId());

    }

    @Test
    void shouldUpdateAnswer() {

        Integer assessmentId = 1;
        AnswerRequest answerRequest = new AnswerRequest(1, "some text");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        AnswerId answerId = mapper.map(answerRequest, AnswerId.class);
        answerId.setAssessment(assessment);
        Answer answer = mapper.map(answerRequest, Answer.class);
        answer.setAnswerId(answerId);

        answerRepository.save(answer);

        answer.setAnswer("new Answer");
        when(answerRepository.existsById(answerId)).thenReturn(true);
        when(answerRepository.update(answer)).thenReturn(answer);
        Answer actualAnswer = answerService.saveAnswer(answer);

        assertEquals(answer.getAnswer(), actualAnswer.getAnswer());
        assertEquals(answer.getAnswerId(), actualAnswer.getAnswerId());
        System.out.println(answer.getAnswer());
    }
}
