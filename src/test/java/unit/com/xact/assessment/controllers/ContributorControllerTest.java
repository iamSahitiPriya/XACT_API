package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.ContributorController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Question;
import com.xact.assessment.services.QuestionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static com.xact.assessment.dtos.ContributorRole.Author;
import static org.mockito.Mockito.*;

class ContributorControllerTest {

    private final QuestionService questionService = mock(QuestionService.class);
    private final ContributorController contributorController = new ContributorController(questionService);

    private final Authentication authentication = Mockito.mock(Authentication.class);


    @Test
    void shouldGetContributorQuestions() {

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
        contributorQuestionData.setStatus(ContributorQuestionStatus.Draft);
        contributorParameterData.setQuestions(Collections.singletonList(contributorQuestionData));
        contributorTopicData.setParameters(Collections.singletonList(contributorParameterData));
        contributorModuleData.setTopics(Collections.singletonList(contributorTopicData));

        when(questionService.getContributorResponse(Author, "abc@thoughtworks.com")).thenReturn(contributorResponse);

        HttpResponse<ContributorResponse> actualResponse = contributorController.getContributorQuestions(Author, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void shouldUpdateContributorQuestionStatus() {
        Integer questionId =1;
        QuestionStatusUpdateRequest questionStatusUpdateRequest=new QuestionStatusUpdateRequest();
        questionStatusUpdateRequest.setQuestionId(Collections.singletonList(questionId));
        questionStatusUpdateRequest.setComments("comments");
        QuestionStatusUpdateResponse questionStatusUpdateResponse=new QuestionStatusUpdateResponse();
        questionStatusUpdateResponse.setQuestionId(questionStatusUpdateRequest.getQuestionId());
        questionStatusUpdateResponse.setStatus(ContributorQuestionStatus.Sent_For_Review);
        questionStatusUpdateResponse.setComments("comments");

        when(questionService.updateContributorQuestionsStatus(1,ContributorQuestionStatus.Sent_For_Review,questionStatusUpdateRequest,"abc@thoughtworks.com")).thenReturn(questionStatusUpdateResponse);

        HttpResponse<QuestionStatusUpdateResponse> actualResponse=contributorController.updateContributorQuestionsStatus(1,ContributorQuestionStatus.Sent_For_Review,questionStatusUpdateRequest,authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());
    }

    @Test
    void shouldDeleteQuestion() {
        Integer questionId=1;

        doNothing().when(questionService).deleteQuestion(questionId,"abc@thoughtworks.com");

        HttpResponse<Question> actualResponse=contributorController.deleteQuestion(questionId,authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

    }

    @Test
    void shouldUpdateQuestionText() {
        Integer questionId=1;
        String questionText="new question";

        doNothing().when(questionService).updateContributorQuestion(questionId,questionText,"abc@thoughtworks.com");

        HttpResponse<Question> actualResponse=contributorController.updateQuestion(questionId,questionText,authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

    }
}
