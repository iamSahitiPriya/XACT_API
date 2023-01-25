package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.ActivityLogController;
import com.xact.assessment.dtos.ActivityResponse;
import com.xact.assessment.dtos.ActivityType;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.models.User;
import com.xact.assessment.services.ActivityLogService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActivityLogControllerTest {

    private final ActivityLogService activityLogService = mock(ActivityLogService.class);
    private final UserAuthService userAuthService = mock(UserAuthService.class);
    private final ActivityLogController activityLogController = new ActivityLogController(activityLogService,userAuthService);
    private final Authentication authentication = mock(Authentication.class);

    @Test
    void getActivityLogs() {
        ActivityResponse activityResponse = new ActivityResponse(1, ActivityType.ADDITIONAL_QUESTION, "ABC", "text");
        ActivityResponse activityResponse1 = new ActivityResponse(2, ActivityType.TOPIC_RECOMMENDATION, "DEF", "additional text");
        List<ActivityResponse> activityResponseList = new ArrayList<>();
        activityResponseList.add(activityResponse);
        activityResponseList.add(activityResponse1);

        Assessment assessment = new Assessment();
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        when(activityLogService.getAssessment(1)).thenReturn(assessment);
        when(activityLogService.getTopic(1)).thenReturn(assessmentTopic);
        when(activityLogService.getLatestActivityRecords(assessment, assessmentTopic, new User())).thenReturn(activityResponseList);

        StepVerifier.create(activityLogController.getActivityLogs(1, 1, authentication))
                .expectSubscription()
                .thenCancel()
                .verify();
    }
}
