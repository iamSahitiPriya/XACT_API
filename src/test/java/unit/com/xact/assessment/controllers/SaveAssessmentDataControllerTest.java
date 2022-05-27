package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.SaveAssessmentDataController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AnswerService;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.TopicAndParameterLevelAssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SaveAssessmentDataControllerTest {

    private SaveAssessmentDataController saveAssessmentDataController;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private UserAuthService userAuthService;
    private AssessmentService assessmentService;
    private AnswerService answerService;
    private Authentication authentication;

    private ModelMapper mapper = new ModelMapper();


    @BeforeEach
    public void beforeEach() {
        answerService = mock(AnswerService.class);
        userAuthService = mock(UserAuthService.class);
        assessmentService = mock(AssessmentService.class);
        topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
        authentication = Mockito.mock(Authentication.class);

        saveAssessmentDataController = new SaveAssessmentDataController(answerService, topicAndParameterLevelAssessmentService, userAuthService, assessmentService);
    }

    @Test
    void testSaveAssessmentAtTopicLevel() {
        Integer assessmentId = 1;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(RatingDto.ONE);
        topicRatingAndRecommendation.setRecommendation("some text");

        topicLevelAssessmentRequest.setTopicRatingAndRecommendation(topicRatingAndRecommendation);

        List<AnswerRequest> answerRequestList = new ArrayList<>();

        AnswerRequest answerRequest1 = new AnswerRequest(1, "some text");
        AnswerRequest answerRequest2 = new AnswerRequest(2, "some more text");
        answerRequestList.add(answerRequest1);
        answerRequestList.add(answerRequest2);

        ParameterLevelAssessmentRequest parameterLevelAssessmentRequest = new ParameterLevelAssessmentRequest();
        parameterLevelAssessmentRequest.setAnswerRequest(answerRequestList);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(any(), any())).thenReturn(assessmentUsers.getUserId().getAssessment());
        System.out.println(userAuthService.getLoggedInUser(authentication));
        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = saveAssessmentDataController.saveAnswer(assessmentId, topicLevelAssessmentRequest, authentication);

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation((TopicLevelAssessment) any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testSaveAssessmentAtParameterLevel() {
        Integer assessmentId = 1;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        List<AnswerRequest> answerRequestList = new ArrayList<>();

        AnswerRequest answerRequest1 = new AnswerRequest(1, "some text");
        AnswerRequest answerRequest2 = new AnswerRequest(2, "some more text");
        answerRequestList.add(answerRequest1);
        answerRequestList.add(answerRequest2);

        ParameterLevelAssessmentRequest parameterLevelAssessmentRequest = new ParameterLevelAssessmentRequest();
        parameterLevelAssessmentRequest.setAnswerRequest(answerRequestList);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(RatingDto.ONE);
        parameterRatingAndRecommendation.setRecommendation("some text");

        parameterLevelAssessmentRequest.setParameterRatingAndRecommendation(parameterRatingAndRecommendation);

        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessmentUsers.getUserId().getAssessment());

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = saveAssessmentDataController.saveAnswer(assessmentId, topicLevelAssessmentRequest, authentication);

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation((ParameterLevelAssessment) any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
}
