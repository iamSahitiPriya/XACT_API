/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import com.xact.assessment.utils.ResourceFileUtil;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static com.xact.assessment.dtos.RecommendationDeliveryHorizon.LATER;
import static com.xact.assessment.dtos.RecommendationEffort.HIGH;
import static com.xact.assessment.models.AssessmentStatus.Active;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class AssessmentControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @Inject
    UsersAssessmentsRepository usersAssessmentsRepository;
    @Inject
    AssessmentRepository assessmentRepository;
    @Inject
    AnswerRepository answerRepository;
    @Inject
    ParameterLevelRatingRepository parameterLevelRatingRepository;
    @Inject
    TopicLevelRatingRepository topicLevelRatingRepository;
    @Inject
    QuestionRepository questionRepository;
    @Inject
    AssessmentParameterRepository assessmentParameterRepository;
    @Inject
    AssessmentTopicRepository assessmentTopicRepository;

    @Inject
    TopicLevelRecommendationRepository topicLevelRecommendationRepository;

    @Inject
    ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;

    @Inject
    AccessControlRepository accessControlRepository;

    @Inject
    UserQuestionRepository userQuestionRepository;

    @Inject
    ActivityLogRepository activityLogRepository;

    @Inject
    EntityManager entityManager;

    @Inject
    UserAssessmentModuleRepository userAssessmentModuleRepository;

    Assessment assessment = new Assessment();

    Organisation organisation = new Organisation();

    String userEmail = "dummy@test.com";

    @BeforeEach
    public void beforeEach() {
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentDescription("description");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("testorg");
        assessment.setOrganisation(organisation);
    }


    @AfterEach
    public void afterEach() throws InterruptedException {
        Thread.sleep(1000);
        accessControlRepository.deleteAll();
        answerRepository.deleteAll();
        parameterLevelRatingRepository.deleteAll();
        topicLevelRatingRepository.deleteAll();
        parameterLevelRecommendationRepository.deleteAll();
        topicLevelRecommendationRepository.deleteAll();
        userAssessmentModuleRepository.deleteAll();
        userQuestionRepository.deleteAll();
        usersAssessmentsRepository.deleteAll();
        activityLogRepository.deleteAll();
        assessmentRepository.deleteAll();
    }

    @Test
    void testGetAssessmentsResponse() {

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments").bearerAuth("anything"), String.class);


        String expectedResponse = "[{" + "\"assessmentId\"" + ":" + assessment.getAssessmentId() + "," + "\"assessmentPurpose\"" + ":" + "\"Client Assessment\"" + "," + "\"assessmentName\"" + ":" + "\"mocked assessment\"" + "," + "\"assessmentDescription\"" + ":" + "\"description\"" + "," + "\"organisationName\"" + ":" + "\"testorg\"" + "," + "\"assessmentStatus\"" + ":" + "\"Completed\"" + "," + "\"updatedAt\"" + ":" + assessment.getUpdatedAt().getTime() + "," + "\"teamSize\"" + ":" + organisation.getSize() + "," + "\"domain\"" + ":" + "\"new\"" + "," + "\"industry\"" + ":" + "\"new\"" + "," + "\"assessmentState\"" + ":" + "\"Draft\",\"owner\":true}]";

        assertEquals(expectedResponse, assessmentResponse);

    }

    @Test
    void testGetAssessmentResponse() {

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);


        List<Question> questions = (List<Question>) questionRepository.findAll();
        Question question = questions.get(0);


        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswerNote("answer");

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setAssessment(assessment);
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question?");
        userQuestion.setAnswer("answer");

        ParameterLevelRating parameterLevelRating = new ParameterLevelRating();
        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        parameterLevelRating.setParameterLevelId(parameterLevelId);
        parameterLevelRating.setRating(4);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setDeliveryHorizon(LATER);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRating topicLevelRating = new TopicLevelRating();

        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);

        topicLevelRating.setTopicLevelId(topicLevelId);
        topicLevelRating.setRating(4);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setDeliveryHorizon(LATER);
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        AssessmentModuleId assessmentModuleId = new AssessmentModuleId(assessment, assessmentParameter.getTopic().getModule());
        UserAssessmentModule userAssessmentModule = new UserAssessmentModule(assessmentModuleId, assessment, assessmentParameter.getTopic().getModule());

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        answerRepository.save(answer);
        parameterLevelRatingRepository.save(parameterLevelRating);
        topicLevelRatingRepository.save(topicLevelRating);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        userAssessmentModuleRepository.save(userAssessmentModule);
        entityManager.getTransaction().commit();

        String expectedResponse = "{" + "\"assessmentId\"" + ":" + assessment.getAssessmentId() + "," + "\"assessmentPurpose\"" + ":" + "\"Client Assessment\"" + "," + "\"assessmentName\"" + ":" + "\"mocked assessment\"" + "," + "\"assessmentDescription\"" + ":" + "\"description\"" + "," + "\"organisationName\"" + ":" + "\"testorg\"" + "," + "\"assessmentStatus\"" + ":" + "\"Completed\"" + "," + "\"updatedAt\"" + ":" + assessment.getUpdatedAt().getTime() + "," + "\"teamSize\"" + ":" + 5 + "," + "\"domain\"" + ":" + "\"new\"" + "," + "\"industry\"" + ":" + "\"new\"" + "," + "\"assessmentState\"" + ":" + "\"inProgress\"" + "," + "\"answerResponseList\"" + ":" + "[" + "{" + "\"questionId\"" + ":" + question.getQuestionId() + "," + "\"answer\"" + ":" + "\"answer\"" + "}" + "]" + "," + "\"parameterRatingAndRecommendation\"" + ":" + "[" + "{" + "\"parameterId\"" + ":" + 1 + "," + "\"rating\"" + ":" + 4 + "," + "\"parameterLevelRecommendation\"" + ":" + "[" + "{" + "\"recommendationId\"" + ":" + parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"LATER\"" + "}]}]," + "\"topicRatingAndRecommendation\"" + ":" + "[{" + "\"topicId\"" + ":" + 1 + "," + "\"rating\"" + ":" + 4 + "," + "\"topicLevelRecommendation\"" + ":" + "[{" + "\"recommendationId\"" + ":" + topicLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"LATER\"}]}],\"owner\":true}";

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments/" + assessment.getAssessmentId()).bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }

    @Test
    void finishAssessment() {

        AssessmentUser assessmentUser = new AssessmentUser();
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        MutableHttpRequest request = HttpRequest.create(HttpMethod.PUT, "/v1/assessments/" + assessment.getAssessmentId() + "/finish").contentLength(0).bearerAuth("anything");
        HttpResponse<AssessmentResponse> assessmentResponse = client.toBlocking().exchange(request, AssessmentResponse.class);

        assertEquals(AssessmentStatusDto.Completed, assessmentResponse.body().getAssessmentStatus());

    }

    @Test
    void reopenAssessment() {
        AssessmentUser assessmentUser = new AssessmentUser();
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        MutableHttpRequest request = HttpRequest.create(HttpMethod.PUT, "/v1/assessments/" + assessment.getAssessmentId() + "/open").contentLength(0).bearerAuth("anything");
        HttpResponse<AssessmentResponse> assessmentResponse = client.toBlocking().exchange(request, AssessmentResponse.class);

        assertEquals(AssessmentStatusDto.Active, assessmentResponse.body().getAssessmentStatus());
    }

    @Test
    void testUpdateAssessmentAnswer() throws IOException {
        AssessmentUser assessmentUser = new AssessmentUser();
        assessment.setAssessmentStatus(Active);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        List<Question> questions = (List<Question>) questionRepository.findAll();
        Question question = questions.get(0);

        AnswerId answerId = new AnswerId(assessment, question);

        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswerNote("Ans");

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        answerRepository.save(answer);
        entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-string-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/" + "answers" + "/" + question.getQuestionId(), dataRequest).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateTopicRecommendation() {

        AssessmentUser assessmentUser = new AssessmentUser();
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("New");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setDeliveryHorizon(LATER);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{\"recommendationId\"" + ":" + topicLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\""+",\"deliveryHorizon\": \"NEXT\", \"effort\": \"HIGH\",\"impact\": \"HIGH\"}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/topics/" + assessmentTopic.getTopicId() + "/recommendations", dataRequest).bearerAuth("anything"));
        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }


    @Test
    void testUpdateParameterRecommendationText() {
        AssessmentUser assessmentUser = new AssessmentUser();
        assessment.setAssessmentStatus(AssessmentStatus.Active);


        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("new");
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setDeliveryHorizon(LATER);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" + parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"LATER\"" + "}";
        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/parameters/" + assessmentParameter.getParameterId() + "/recommendations", dataRequest).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testUpdateParameterRecommendationImpact() {
        AssessmentUser assessmentUser = new AssessmentUser();

        assessment.setAssessmentStatus(AssessmentStatus.Active);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationEffort(RecommendationEffort.MEDIUM);
        parameterLevelRecommendation.setDeliveryHorizon(LATER);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);
        parameterLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" + parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"LATER\"" + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/parameters/" + assessmentParameter.getParameterId() + "/recommendations", dataRequest).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }


    @Test
    void testUpdateParameterRating() throws IOException {
        AssessmentUser assessmentUser = new AssessmentUser();

        assessment.setAssessmentStatus(AssessmentStatus.Active);


        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        ParameterLevelRating parameterLevelRating = new ParameterLevelRating();
        parameterLevelRating.setParameterLevelId(parameterLevelId);
        parameterLevelRating.setRating(1);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        parameterLevelRatingRepository.save(parameterLevelRating);
        entityManager.getTransaction().commit();


        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-rating-values.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/parameters/" + assessmentParameter.getParameterId() + "/ratings", dataRequest).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateTopicRating() throws IOException {
        AssessmentUser assessmentUser = new AssessmentUser();
        assessment.setAssessmentStatus(AssessmentStatus.Active);


        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        TopicLevelRating topicLevelRating = new TopicLevelRating();
        topicLevelRating.setTopicLevelId(topicLevelId);
        topicLevelRating.setRating(1);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        topicLevelRatingRepository.save(topicLevelRating);
        entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-rating-values.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/topics/" + assessmentTopic.getTopicId() + "/ratings", dataRequest).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }


    @Test
    void testDeleteParameterRecommendation() {
        AssessmentUser assessmentUser = new AssessmentUser();

        assessment.setAssessmentStatus(AssessmentStatus.Active);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendation.setRecommendationEffort(RecommendationEffort.LOW);
        parameterLevelRecommendation.setDeliveryHorizon(RecommendationDeliveryHorizon.NOW);
        parameterLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();


        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/assessments/" + assessment.getAssessmentId() + "/parameters/" + assessmentParameter.getParameterId() + "/recommendations/" + parameterLevelRecommendation.getRecommendationId()).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testDeleteTopicRecommendation() {
        AssessmentUser assessmentUser = new AssessmentUser();

        assessment.setAssessmentStatus(AssessmentStatus.Active);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);
        topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.LOW);
        topicLevelRecommendation.setDeliveryHorizon(RecommendationDeliveryHorizon.NOW);


        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();

        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/assessments/" + assessment.getAssessmentId() + "/topics/" + assessmentTopic.getTopicId() + "/recommendations/" + topicLevelRecommendation.getRecommendationId()).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testGetAdminAssessmentsResponse() throws IOException {
        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(userEmail, assessment);

        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);
        AccessControlList accessControlList = new AccessControlList(userEmail, AccessControlRoles.Admin);

        assessmentRepository.save(assessment);
        accessControlRepository.save(accessControlList);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String expectedResponse = resourceFileUtil.getJsonString("dto/get-admin-assessments-response.json");

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/admin/assessments/2022-01-13/2022-05-13").bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }


}
