/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
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
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationImpact.LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class AssessmentControllerTest {

    private ModelMapper mapper = new ModelMapper();

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

    }

    @Test
    void testGetAssessmentsResponse() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("testorg");
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        assessment.setOrganisation(organisation);
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments")
                .bearerAuth("anything"), String.class);


        String expectedResponse = "[{" + "\"assessmentId\"" + ":" + assessment.getAssessmentId() +"," +"\"assessmentName\"" +  ":" +"\"mocked assessment\"" + "," +
                "\"organisationName\"" + ":" +"\"testorg\"" + "," + "\"assessmentStatus\"" + ":" + "\"Completed\"" + "," + "\"updatedAt\"" +  ":" + assessment.getUpdatedAt().getTime() + "," +"\"assessmentState\"" + ":" + "\"Draft\""+ "}]";

        assertEquals(expectedResponse,assessmentResponse);

    }

    @Test
    void testGetAssessmentResponse() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("testorg");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);


        List <Question> questions = (List<Question>) questionRepository.findAll();
        Question question = questions.get(0);


        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswer("answer");

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(4);

        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
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

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setDeliveryHorizon("some text");
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        answerRepository.save(answer);
        parameterLevelAssessmentRepository.save(parameterLevelAssessment);
        topicLevelAssessmentRepository.save(topicLevelAssessment);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();

        String expectedResponse = "{" + "\"assessmentId\"" + ":" + assessment.getAssessmentId() +"," +"\"assessmentName\"" +  ":" +"\"mocked assessment\"" + "," +
                "\"organisationName\"" + ":" +"\"testorg\"" + "," + "\"assessmentStatus\"" + ":" + "\"Completed\"" + "," + "\"updatedAt\"" +  ":" + assessment.getUpdatedAt().getTime() + "," +"\"teamSize\"" + ":" + 10 +"," +"\"domain\"" + ":" + "\"Telecom\"" + "," +"\"industry\"" + ":" + "\"IT\"" +"," +"\"assessmentState\"" + ":" + "\"Draft\""+","+
                "\"answerResponseList\"" + ":" + "[" + "{" + "\"questionId\"" +  ":" + 1 + "," + "\"answer\"" + ":" + "\"answer\"" +  "}" + "]" + "," +
                "\"parameterRatingAndRecommendation\"" + ":" + "[" + "{" + "\"parameterId\"" +  ":" + 1 + "," + "\"rating\"" + ":" + 4 + "," +
                "\"parameterLevelRecommendation\"" + ":" + "[" + "{" + "\"recommendationId\""+  ":" + parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"some text\"" + "}]}]," +
                "\"topicRatingAndRecommendation\"" + ":" + "[{" +"\"topicId\"" + ":" +1 + "," + "\"rating\"" + ":" +4 + "," +"\"topicLevelRecommendation\"" + ":" + "[{" + "\"recommendationId\"" + ":" + topicLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" +  ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"some text\"" + "}]}]}";

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments/"+assessment.getAssessmentId())
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }

    @Test
    void finishAssessment() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        MutableHttpRequest request = HttpRequest.create(HttpMethod.PUT, "/v1/assessments/" + assessment.getAssessmentId() + "/statuses/finish").contentLength(0)
                .bearerAuth("anything");
        HttpResponse<String> assessmentResponse = client.toBlocking().exchange(request, String.class);

        Date updatedDate = assessmentRepository.findById(assessment.getAssessmentId()).get().getUpdatedAt();
        String expectedResponse = "{" + "\"assessmentId\"" + ":" + assessment.getAssessmentId() +"," +"\"assessmentName\"" +  ":" +"\"mocked assessment\"" + "," +
                "\"organisationName\"" + ":" +"\"org\"" + "," + "\"assessmentStatus\"" + ":" + "\"Completed\"" + "," + "\"updatedAt\"" +  ":" + updatedDate.getTime() + "}";


        assertEquals(expectedResponse, assessmentResponse.body());

    }

    @Test
    void reopenAssessment() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        MutableHttpRequest request = HttpRequest.create(HttpMethod.PUT, "/v1/assessments/" + assessment.getAssessmentId() +"/statuses/open").contentLength(0)
                .bearerAuth("anything");
        HttpResponse<String> assessmentResponse = client.toBlocking().exchange(request, String.class);

        Date updatedDate = assessmentRepository.findById(assessment.getAssessmentId()).get().getUpdatedAt();
        String expectedResponse = "{" + "\"assessmentId\"" + ":" + assessment.getAssessmentId() +"," +"\"assessmentName\"" +  ":" +"\"mocked assessment\"" + "," +
                "\"organisationName\"" + ":" +"\"org\"" + "," + "\"assessmentStatus\"" + ":" + "\"Active\"" + "," + "\"updatedAt\"" +  ":" + updatedDate.getTime() + "}";


        assertEquals(expectedResponse, assessmentResponse.body());

    }

    @Test
    void testSaveTopicLevelAssessmentResponse() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/set-topic-level-request.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessments/notes/" + assessment.getAssessmentId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testSaveParameterLevelAssessmentResponse() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/set-parameter-level-request.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessments/notes/" + assessment.getAssessmentId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateAssessmentAnswer() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        List <Question> questions = (List<Question>) questionRepository.findAll();
        Question question = questions.get(0);

        AnswerId answerId = new AnswerId(assessment,question);

        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswer("Ans");

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        answerRepository.save(answer);
        entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-string-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/answers/" + assessment.getAssessmentId() + "/" + question.getQuestionId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }
    @Test
    void testUpdateTopicRecommendation() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("New");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setDeliveryHorizon("Low");

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" + topicLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRecommendationText/" + assessment.getAssessmentId() + "/" + assessmentTopic.getTopicId(), dataRequest)
                .bearerAuth("anything"));
        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }



    @Test
    void testUpdateParameterRecommendationText() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("new");
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setDeliveryHorizon("HIGH");

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" +  parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"dummy text\"" + "}" ;
        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/parameterRecommendation/"+assessment.getAssessmentId() + "/" +assessmentParameter.getParameterId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }
    @Test
    void testUpdateTopicRecommendationImpact() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.MEDIUM);
        topicLevelRecommendation.setDeliveryHorizon("MEDIUM");
        topicLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" +  topicLevelRecommendation.getRecommendationId() + "," + "\"impact\"" + ":" + "\"\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"\"" + "}" ;

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRecommendationFields/"+assessment.getAssessmentId()+"/"+assessmentTopic.getTopicId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }
    @Test
    void testUpdateParameterRecommendationImpact() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationEffort(RecommendationEffort.MEDIUM);
        parameterLevelRecommendation.setDeliveryHorizon("MEDIUM");
        parameterLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" +  parameterLevelRecommendation.getRecommendationId() + "," + "\"recommendation\"" + ":" + "\"some recommendation\"" + "," + "\"impact\"" + ":" + "\"LOW\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"dummy text\"" + "}" ;

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/parameterRecommendation/"+assessment.getAssessmentId()+"/"+assessmentParameter.getParameterId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testUpdateTopicRecommendationEffect() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        topicLevelRecommendation.setDeliveryHorizon("MEDIUM");
        topicLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"recommendationId\"" + ":" +  topicLevelRecommendation.getRecommendationId() + "," + "\"impact\"" + ":" + "\"\"" + "," + "\"effort\"" + ":" + "\"HIGH\"" + "," + "\"deliveryHorizon\"" + ":" + "\"\"" + "}" ;

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRecommendationFields/"+assessment.getAssessmentId()+"/"+assessmentTopic.getTopicId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateParameterRating() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(1);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        parameterLevelAssessmentRepository.save(parameterLevelAssessment);
        entityManager.getTransaction().commit();


        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-rating-values.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/parameterRating/"+assessment.getAssessmentId()+ "/" +assessmentParameter.getParameterId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateTopicRating() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(1);

       assessmentRepository.save(assessment);
       usersAssessmentsRepository.save(assessmentUsers);
       topicLevelAssessmentRepository.save(topicLevelAssessment);
       entityManager.getTransaction().commit();

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-rating-values.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRating/"+assessment.getAssessmentId()+"/"+assessmentTopic.getTopicId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }


    @Test
    void testDeleteParameterRecommendation() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        entityManager.getTransaction().commit();


        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/assessments/deleteParameterRecommendation/"+assessment.getAssessmentId()+"/"+assessmentParameter.getParameterId()+"/"+parameterLevelRecommendation.getRecommendationId())
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testDeleteTopicRecommendation() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();

        Organisation org = new Organisation();
        org.setOrganisationName("org");
        org.setIndustry("IT");
        org.setDomain("Telecom");
        org.setSize(10);

        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(org);

        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.MEDIUM);
        topicLevelRecommendation.setAssessment(assessment);

        assessmentRepository.save(assessment);
        usersAssessmentsRepository.save(assessmentUsers);
        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        entityManager.getTransaction().commit();

        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/assessments/deleteRecommendation/"+assessment.getAssessmentId()+"/"+assessmentTopic.getTopicId()+"/"+topicLevelRecommendation.getRecommendationId())
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }
    @Test
    void testGetAdminAssessmentsResponse() throws IOException, ParseException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("new");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("new");
        assessment.setOrganisation(organisation);
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        UserId userId = new UserId(userEmail, assessment);

        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);
        AccessControlList accessControlList = new AccessControlList(userEmail, AccessControlRoles.Admin);

        assessmentRepository.save(assessment);
        accessControlRepository.save(accessControlList);
        usersAssessmentsRepository.save(assessmentUsers);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String expectedResponse = resourceFileUtil.getJsonString("dto/get-admin-assessments-response.json");

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments/admin/1/2022-01-13/2022-05-13")
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }


}
