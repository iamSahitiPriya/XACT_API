package unit.com.xact.assessment.services;

import com.xact.assessment.models.Question;
import com.xact.assessment.repositories.QuestionRepository;
import com.xact.assessment.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionServiceTest {
    private QuestionRepository questionRepository;
    private QuestionService questionService;

    @BeforeEach
    public void beforeEach() {
        questionRepository = mock(QuestionRepository.class);
        questionService = new QuestionService(questionRepository);

    }

    @Test
    public void shouldGetDetailsForQuestionId() {
        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        Optional<Question> actualQuestion = questionService.getQuestion(questionId);
        assertEquals(question.getQuestionId(), actualQuestion.get().getQuestionId());
        assertEquals(question.getQuestionText(), actualQuestion.get().getQuestionText());

    }

}
