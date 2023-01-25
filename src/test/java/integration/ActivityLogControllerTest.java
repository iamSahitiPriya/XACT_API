package integration;

import com.xact.assessment.repositories.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.sse.SseClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;

@MicronautTest
class ActivityLogControllerTest {

    @Client("/")
    @Inject
    SseClient client;

    @Inject
    ActivityLogRepository activityLogRepository;

    @Inject
    EntityManager entityManager;

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    AssessmentTopicRepository assessmentTopicRepository;

    @Inject
    AssessmentParameterRepository assessmentParameterRepository;

    @Inject
    UserQuestionRepository userQuestionRepository;

    @AfterEach
    void afterEach() {
        userQuestionRepository.deleteAll();
        activityLogRepository.deleteAll();
        assessmentRepository.deleteAll();
    }


    @Test
    void getActivityLogs() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();
        Organisation organisation = new Organisation();
        AssessmentParameter parameter = assessmentParameterRepository.findByParameterId(1);
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestion("This is a question");
        userQuestion.setAssessment(assessment);
        userQuestion.setParameter(parameter);
        userQuestion.setAnswer("Hello");
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("testorg");
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        assessment.setOrganisation(organisation);
        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);
        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityId(new ActivityId(assessment, "123" + userEmail));
        activityLog.setActivityType(ActivityType.ADDITIONAL_QUESTION);
        activityLog.setTopic(assessmentTopic);

        assessmentRepository.save(assessment);
        userQuestionRepository.save(userQuestion);

        activityLog.setIdentifier(userQuestion.getQuestionId());
        activityLogRepository.save(activityLog);


        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String expectedResponse = "[{\"identifier\":" + userQuestion.getQuestionId() + ",\"activityType\":\"ADDITIONAL_QUESTION\",\"firstName\":\"123dummy@test.com\",\"email\":\"123dummy@test.com\",\"inputText\":\"Hello\"}]";

        List<Event<String>> response = Flowable.fromPublisher(client.eventStream(HttpRequest.GET("/v1/assessments/" + assessment.getAssessmentId() + "/topics/1/activities").bearerAuth("anything"), String.class)
        ).take(1).toList().blockingGet();

        assertEquals(expectedResponse,response.get(0).getData());

    }


}
