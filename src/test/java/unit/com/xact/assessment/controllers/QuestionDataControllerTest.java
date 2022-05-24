package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.QuestionDataController;
import com.xact.assessment.dtos.AnswerDto;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.services.AnswerService;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class QuestionDataControllerTest {

    private QuestionDataController questionDataController;
    private AnswerService answerService;
    private ModelMapper mapper = new ModelMapper();


    @BeforeEach
    public void beforeEach() {
        answerService = mock(AnswerService.class);
        questionDataController = new QuestionDataController(answerService);
    }

    @Test
    void testSaveAssessmentNotes() {
        Integer assessmentId = 1;
        List<AnswerDto> answerDtoList = new ArrayList<>(List.of(new AnswerDto()));

        AnswerDto answerDto1 = new AnswerDto(1, "some text");
        answerDtoList.add(answerDto1);

        AnswerDto answerDto2 = new AnswerDto(2, "some more text");
        answerDtoList.add(answerDto2);

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
//        List<Answer> answers = new ArrayList<>();

        HttpResponse<AnswerDto> actualResponse = questionDataController.saveAnswer(assessmentId, answerDtoList);

//        for (AnswerDto answerDto : answerDtoList) {
//            assessment.setAssessmentId(assessmentId);
//            AnswerId answerId = mapper.map(answerDto, AnswerId.class);
//            answerId.setAssessment(assessment);
//            Answer answer = mapper.map(answerDto, Answer.class);
//            answer.setAnswerId(answerId);
//            when(answerService.saveAnswer(answer)).thenReturn(answer);
//            answers.add(answer);
//        }

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
}
