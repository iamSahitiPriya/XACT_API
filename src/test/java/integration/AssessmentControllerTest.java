/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.dtos.AssessmentResponse;
import com.xact.assessment.dtos.AssessmentStatusDto;
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
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationImpact.LOW;
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
    ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    @Inject
    TopicLevelAssessmentRepository topicLevelAssessmentRepository;
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
    EntityManager entityManager;


    @AfterEach
    public void afterEach() {
        usersAssessmentsRepository.deleteAll();
        accessControlRepository.deleteAll();
        answerRepository.deleteAll();
        parameterLevelAssessmentRepository.deleteAll();
        topicLevelAssessmentRepository.deleteAll();
        parameterLevelRecommendationRepository.deleteAll();
        topicLevelRecommendationRepository.deleteAll();
        assessmentRepository.deleteAll();
        userQuestionRepository.deleteAll();


    }

    @Test
    void testGetAssessmentsResponse() {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();
        Organisation organisation = new Organisation();
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

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments")
                .bearerAuth("anything"), String.class);


        String expectedResponse = "[{" + "\"assessmentId\"" + ":" + assessment.getAssessmentId() + "," + "\"assessmentPurpose\"" + ":" + "\"Client Assessment\"" + "," + "\"assessmentName\"" + ":" + "\"mocked assessment\"" + "," +
                "\"organisationName\"" + ":" + "\"testorg\"" + "," + "\"assessmentStatus\"" + ":" + "\"Completed\"" + "," + "\"updatedAt\"" + ":" + assessment.getUpdatedAt().getTime() + "," + "\"teamSize\"" + ":" + organisation.getSize() + "," + "\"domain\"" + ":" + "\"new\"" + "," + "\"industry\"" + ":" + "\"new\"" + "," + "\"assessmentState\"" + ":" + "\"Draft\",\"owner\":true}]";

        assertEquals(expectedResponse, assessmentResponse);

    }

    @Test
    void testGetAssessmentResponse() {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("testorg");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        assessment.setOrganisation(org);
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

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(4);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setDeliveryHorizon("some text");
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();

        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);

        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(4);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setDeliveryHorizon("some text");
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        answerRepository.save(answer);
        parameterLevelAssessmentRepository.save(parameterLevelAssessment);
        topicLevelAssessmentRepository.save(topicLevelAssessment);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();

        String expectedResponse = "{" + "\"assessmentId\"" + ":" + assessment.getAssessmentId() + "," + "\"assessmentPurpose\"" + ":" + "\"Client Assessment\"" + "," + "\"assessmentName\"" + ":" + "\"Mocked Assessment\"" + "," +
                "\"organisationName\"" + ":" + "\"testorg\"" + "," + "\"assessmentStatus\"" + ":" + "\"Completed\"" + "," + "\"updatedAt\"" + ":" + assessment.getUpdatedAt().getTime() + "," + "\"teamSize\"" + ":" + 10 + "," + "\"domain\"" + ":" + "\"Telecom\"" + "," + "\"industry\"" + ":" + "\"IT\"" + "," + "\"assessmentState\"" + ":" + "\"Draft\"" + "," +
                "\"answerResponseList\"" + ":" + "[" + "{" + "\"questionId\"" + ":" + question.getQuestionId() + "," + "\"answer\"" + ":" + "\"answer\"" + "}" + "]" + "," +
                "\"parameterRatingAndRecommendation\"" + ":" + "[" + "{" + "\"parameterId\"" + ":" + 1 + "," + "\"rating\"" + ":" + 4 + "," +
                "\"parameterLevelRecommendation\"" + ":" + "[" + "{" + "\"recommendationId\"" + ":" + parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"some text\"" + "}]}]," +
                "\"topicRatingAndRecommendation\"" + ":" + "[{" + "\"topicId\"" + ":" + 1 + "," + "\"rating\"" + ":" + 4 + "," + "\"topicLevelRecommendation\"" + ":" + "[{" + "\"recommendationId\"" + ":" + topicLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"some text\"}]}],\"owner\":true}";

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments/" + assessment.getAssessmentId())
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }

    @Test
    void finishAssessment() {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        MutableHttpRequest request = HttpRequest.create(HttpMethod.PUT, "/v1/assessments/" + assessment.getAssessmentId() + "/finish").contentLength(0)
                .bearerAuth("anything");
        HttpResponse<AssessmentResponse> assessmentResponse = client.toBlocking().exchange(request, AssessmentResponse.class);

        assertEquals(AssessmentStatusDto.Completed, assessmentResponse.body().getAssessmentStatus());

    }

    @Test
    void reopenAssessment() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        MutableHttpRequest request = HttpRequest.create(HttpMethod.PUT, "/v1/assessments/" + assessment.getAssessmentId() + "/open").contentLength(0)
                .bearerAuth("anything");
        HttpResponse<AssessmentResponse> assessmentResponse = client.toBlocking().exchange(request, AssessmentResponse.class);

        assertEquals(AssessmentStatusDto.Active, assessmentResponse.body().getAssessmentStatus());
    }

    @Test
    void testSaveTopicLevelAssessmentResponse() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/set-topic-level-request.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessments/" + assessment.getAssessmentId() + "/notes", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testSaveParameterLevelAssessmentResponse() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(Active);
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/set-parameter-level-request.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessments/" + assessment.getAssessmentId() + "/notes", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateAssessmentAnswer() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(Active);
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setOrganisation(org);

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

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/" + "answers" + "/" + question.getQuestionId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateTopicRecommendation() {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("New");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setDeliveryHorizon("Low");

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" + topicLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/"+assessment.getAssessmentId()+"/topics/"+assessmentTopic.getTopicId()+"/recommendations-text" , dataRequest)
                .bearerAuth("anything"));
        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }


    @Test
    void testUpdateParameterRecommendationText() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setOrganisation(org);

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
        parameterLevelRecommendation.setDeliveryHorizon("HIGH");

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" + parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"dummy text\"" + "}";
        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/parameters/" + assessmentParameter.getParameterId() + "/recommendations", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testUpdateTopicRecommendationImpact() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.MEDIUM);
        topicLevelRecommendation.setDeliveryHorizon("MEDIUM");
        topicLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" + topicLevelRecommendation.getRecommendationId() + "," + "\"impact\"" + ":" + "\"\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"\"" + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/topics/" + assessmentTopic.getTopicId() + "/recommendations-fields", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateParameterRecommendationImpact() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationEffort(RecommendationEffort.MEDIUM);
        parameterLevelRecommendation.setDeliveryHorizon("MEDIUM");
        parameterLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" + parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"dummy text\"" + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/parameters/" + assessmentParameter.getParameterId() + "/recommendations", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testUpdateTopicRecommendationEffect() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        topicLevelRecommendation.setDeliveryHorizon("MEDIUM");
        topicLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" + topicLevelRecommendation.getRecommendationId() + "," + "\"impact\"" + ":" + "\"\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"\"" + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/topics/" + assessmentTopic.getTopicId() + "/recommendations-fields", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateParameterRating() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(1);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        parameterLevelAssessmentRepository.save(parameterLevelAssessment);
        entityManager.getTransaction().commit();


        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-rating-values.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/parameters/" + assessmentParameter.getParameterId() + "/ratings", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateTopicRating() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(1);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        topicLevelAssessmentRepository.save(topicLevelAssessment);
        entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-rating-values.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/" + assessment.getAssessmentId() + "/topics/" + assessmentTopic.getTopicId() + "/ratings", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }


    @Test
    void testDeleteParameterRecommendation() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();


        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/assessments/" + assessment.getAssessmentId() + "/parameters/" + assessmentParameter.getParameterId() + "/recommendations/" + parameterLevelRecommendation.getRecommendationId())
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testDeleteTopicRecommendation() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUser assessmentUser = new AssessmentUser();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        topicLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUser);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();

        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/assessments/" + assessment.getAssessmentId() + "/topics/" + assessmentTopic.getTopicId() + "/recommendations/" + topicLevelRecommendation.getRecommendationId())
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testGetAdminAssessmentsResponse() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("new");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("new");
        assessment.setOrganisation(organisation);
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

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/admin/assessments/2022-01-13/2022-05-13")
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }


}
