package integration;

import com.xact.assessment.dtos.ActivityType;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ActivityLogRepository;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.Date;

@MicronautTest
public class ActivityLogControllerTest {

    @Client("/")
    @Inject
    HttpClient client;

    @Inject
    ActivityLogRepository activityLogRepository;

    @Inject
    EntityManager entityManager;

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    AssessmentTopicRepository assessmentTopicRepository;

    @AfterEach
    void afterEach() {
        activityLogRepository.deleteAll();
        assessmentRepository.deleteAll();
    }

    //-----Commented because it needs to be corrected-----

//    @Test
//    void getActivityLogs() {
//        String userEmail = "dummy@test.com";
//        Assessment assessment = new Assessment();
//        AssessmentUser assessmentUser = new AssessmentUser();
//        Organisation organisation = new Organisation();
//        organisation.setSize(5);
//        organisation.setIndustry("new");
//        organisation.setDomain("new");
//        organisation.setOrganisationName("testorg");
//        assessment.setAssessmentName("mocked assessment");
//        assessment.setAssessmentPurpose("Client Assessment");
//        assessment.setAssessmentStatus(AssessmentStatus.Completed);
//        assessment.setOrganisation(organisation);
//        UserId userId = new UserId(userEmail, assessment);
//        assessmentUser.setUserId(userId);
//        assessmentUser.setRole(AssessmentRole.Owner);
//        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);
//        ActivityLog activityLog = new ActivityLog(new ActivityId(assessment,userEmail),assessmentTopic,1, ActivityType.ADDITIONAL_QUESTION,new Date());
//
//        assessmentRepository.save(assessment);
//        activityLogRepository.save(activityLog);
//        entityManager.getTransaction().commit();
//        entityManager.clear();
//        entityManager.close();
//
//
//        String response = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessment/"+assessment.getAssessmentId()+"/topic/1/activity").bearerAuth("anything"), String.class);
//    }



//Publisher response = client.eventStream(HttpRequest.GET("/v1/assessment/" + assessment.getAssessmentId() + "/topic/1/activity").bearerAuth("anything"));


//        StepVerifier.create(client.toBlocking().retrieve(HttpRequest.GET("/v1/assessment/"+assessment.getAssessmentId()+"/topic/1/activity").bearerAuth("anything"), Publisher.class))
//                .expectSubscription()
//                .thenCancel()
//                .verify();
// -----Commented because it needs to be corrected-----


}
