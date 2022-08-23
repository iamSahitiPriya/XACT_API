package unit.com.xact.assessment.services;

import com.xact.assessment.models.Question;
import com.xact.assessment.repositories.QuestionRepository;
import com.xact.assessment.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
<<<<<<< HEAD
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
=======
import static org.mockito.Mockito.*;
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148

class QuestionServiceTest {
    private QuestionRepository questionRepository;
    private QuestionService questionService;

    @BeforeEach
    public void beforeEach() {
        questionRepository = mock(QuestionRepository.class);
        questionService = new QuestionService(questionRepository);

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

<<<<<<< HEAD
=======
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
        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("Question?");
        when(questionRepository.update(question)).thenReturn(question);
        questionService.updateQuestion(question);
        verify(questionRepository).update(question);
    }
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
}
