package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ActivityResponse;
import com.xact.assessment.dtos.ActivityType;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ActivityLogRepository;
import com.xact.assessment.services.*;
import io.micronaut.data.exceptions.EmptyResultException;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ActivityLogServiceTest {
    ActivityLogRepository activityLogRepository = mock(ActivityLogRepository.class);
    AssessmentService assessmentService = mock(AssessmentService.class);
    AnswerService answerService = mock(AnswerService.class);
    UserQuestionService userQuestionService = mock(UserQuestionService.class);
    UserAuthService userAuthService = mock(UserAuthService.class);
    TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
    TopicService topicService = mock(TopicService.class);
    Authentication authentication = mock(Authentication.class);

    ActivityLogService activityLogService;

    public ActivityLogServiceTest() { this.activityLogService = new ActivityLogService(assessmentService,activityLogRepository,answerService,userQuestionService,userAuthService,topicAndParameterLevelAssessmentService,topicService);}

    @Test
    void shouldUpdateActivityLog() {
        Profile profile = new Profile();
        profile.setEmail("abc");
        User user = new User("1",profile,"true");
        String loggedInUser = "abc";

        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);
        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        ActivityId activityId = new ActivityId(assessment,loggedInUser);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(activityLogRepository.existsById(activityId)).thenReturn(true);
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityId(activityId);


        activityLogService.saveActivityLog(assessment,authentication,1,assessmentTopic, ActivityType.ADDITIONAL_QUESTION);

        verify(activityLogRepository).update(activityLog);
    }

    @Test
    void shouldReturnAssessment() {
        Assessment assessment = new Assessment();
        when(assessmentService.getAssessmentById(1)).thenReturn(assessment);

        Assessment assessment1 = activityLogService.getAssessment(1);

        assertEquals(assessment,assessment1);
    }

    @Test
    void shouldReturnTopic() {
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        when(topicService.getTopicById(1)).thenReturn(assessmentTopic);

        AssessmentTopic assessmentTopic1 = activityLogService.getTopic(1);

        assertEquals(assessmentTopic, assessmentTopic1);
    }

    @Test
    void shouldReturnListOfActivityRecords() {
        Profile profile = new Profile();
        profile.setEmail("abc");
        User user = new User("1",profile,"true");
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        String loggedInUser = "abc";
        ActivityId activityId = new ActivityId(assessment,loggedInUser);
        List<ActivityLog> activityLogs = new ArrayList<>();
        activityLogs.add(new ActivityLog(activityId,assessmentTopic,1, ActivityType.ADDITIONAL_QUESTION,new Date()));
        activityLogs.add(new ActivityLog(activityId,assessmentTopic,1, ActivityType.DEFAULT_QUESTION,new Date()));
        activityLogs.add(new ActivityLog(activityId,assessmentTopic,1, ActivityType.TOPIC_RECOMMENDATION,new Date()));
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(activityLogRepository.getLatestRecords(any(Date.class),any(Date.class),eq(assessment),eq(assessmentTopic),eq(loggedInUser))).thenReturn(activityLogs);

        List<ActivityResponse> activityResponseList = activityLogService.getLatestActivityRecords(assessment,assessmentTopic,authentication);

        assertEquals(3,activityResponseList.size());
    }

    @Test
    void shouldSaveActivityLog() {
        Profile profile = new Profile();
        profile.setEmail("abc");
        User user = new User("1",profile,"true");
        String loggedInUser = "abc";

        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);
        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        ActivityId activityId = new ActivityId(assessment,loggedInUser);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(activityLogRepository.existsById(activityId)).thenReturn(false);
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityId(activityId);


        activityLogService.saveActivityLog(assessment,authentication,1,assessmentTopic, ActivityType.ADDITIONAL_QUESTION);

        verify(activityLogRepository).save(activityLog);
    }

    @Test
    void shouldHandleTheExceptionWhenThereIsNoActivityRecord() {
        Profile profile = new Profile();
        profile.setEmail("abc");
        User user = new User("1",profile,"true");
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        String loggedInUser = "abc";
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(activityLogRepository.getLatestRecords(any(Date.class),any(Date.class),eq(assessment),eq(assessmentTopic),eq(loggedInUser))).thenThrow(EmptyResultException.class);

        List<ActivityResponse> activityResponseList = activityLogService.getLatestActivityRecords(assessment,assessmentTopic,authentication);

        assertEquals(0,activityResponseList.size());
    }


}
