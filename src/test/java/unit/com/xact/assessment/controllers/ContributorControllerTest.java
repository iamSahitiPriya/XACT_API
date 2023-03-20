package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.ContributorController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.services.QuestionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static com.xact.assessment.dtos.ContributorRole.Author;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContributorControllerTest {

    private final QuestionService questionService = mock(QuestionService.class);
    private final ContributorController contributorController = new ContributorController(questionService);

    private final Authentication authentication = Mockito.mock(Authentication.class);


    @Test
    void shouldGetContributorQuestions() {

        ContributorDataResponse contributorDataResponse = new ContributorDataResponse();
        ContributorCategoryData contributorCategoryData = new ContributorCategoryData();
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
        contributorDataResponse.setContributorCategoryDataList(Collections.singletonList(contributorCategoryData));

        when(questionService.getContributorQuestions(Author,"abc@thoughtworks.com")).thenReturn(contributorDataResponse);

        HttpResponse<ContributorDataResponse> actualResponse = contributorController.getContributorQuestions(Author,authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

    }
}
