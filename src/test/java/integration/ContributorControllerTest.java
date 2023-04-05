/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.dtos.ContributorQuestionStatus;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ContributorId;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.models.Question;
import com.xact.assessment.repositories.*;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class ContributorControllerTest {
    @Inject
    @Client("/")
    HttpClient client;


    @Inject
    QuestionRepository questionRepository;

    @Inject
    ModuleContributorRepository moduleContributorRepository;

    @Inject
    UserQuestionRepository userQuestionRepository;

    @Inject
    AssessmentParameterRepository assessmentParameterRepository;

    @Inject
    AccessControlRepository accessControlRepository;

    @Inject
    ModuleRepository moduleRepository;

    @Inject
    EntityManager entityManager;

    @AfterEach
    public void afterEach() {
        questionRepository.deleteAll();
        moduleContributorRepository.deleteAll();
        entityManager.flush();
    }

    @Test
    void shouldGetContributorQuestionResponse() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = new ModuleContributor(contributorId, ContributorRole.AUTHOR);
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        Question question = new Question();
        question.setQuestionText("new user question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentModule.getTopics().stream().toList().get(0).getParameters().stream().toList().get(0));

        questionRepository.save(question);
        entityManager.getTransaction().commit();


        String expectedResponse = "{\"contributorModuleData\":[{\"moduleId\":1,\"moduleName\":\"Architecture Quality\",\"categoryName\":\"Software engineering\",\"categoryId\":1,\"topics\":[{\"topicId\":" + question.getParameter().getTopic().getTopicId() + ",\"topicName\":" + "\"" + question.getParameter().getTopic().getTopicName() + "\"" + ",\"parameters\":[{\"parameterId\":" + question.getParameter().getParameterId() + ",\"parameterName\":" + "\"" + question.getParameter().getParameterName() + "\"" + ",\"questions\":[{\"questionId\":" + question.getQuestionId() + ",\"question\":\"new user question\",\"status\":\"DRAFT\"}]}]}]}]}";
        String actualResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/contributor/questions?role=Author").bearerAuth("anything"), String.class);
        Assertions.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    void shouldUpdateContributorQuestionStatus() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = new ModuleContributor(contributorId, ContributorRole.AUTHOR);
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        Question question = new Question();
        question.setQuestionText("new user question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentModule.getTopics().stream().toList().get(0).getParameters().stream().toList().get(0));

        questionRepository.save(question);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{\n" +
                "    \"questionId\":[" + question.getQuestionId() + "],\n" +
                "    \"comments\":\"comments\"\n" +
                "}";

        String expectedResponse = "{\"questionId\":[" + question.getQuestionId() + "],\"comments\":\"comments\",\"status\":\"SENT_FOR_REVIEW\"}";
        String actualResponse = client.toBlocking().retrieve(HttpRequest.PATCH("/v1/contributor/module/1/questions?status=SENT_FOR_REVIEW", dataRequest).bearerAuth("anything"), String.class);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldDeleteContributorQuestion() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = new ModuleContributor(contributorId, ContributorRole.AUTHOR);
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        Question question = new Question();
        question.setQuestionText("new user question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentModule.getTopics().stream().toList().get(0).getParameters().stream().toList().get(0));

        questionRepository.save(question);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        var actualResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/contributor/question/" + question.getQuestionId()).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateContributorQuestion() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = new ModuleContributor(contributorId, ContributorRole.AUTHOR);
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        Question question = new Question();
        question.setQuestionText("new user question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentModule.getTopics().stream().toList().get(0).getParameters().stream().toList().get(0));

        questionRepository.save(question);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();


        String expectedResponse = "{\"questionId\":" + question.getQuestionId() + ",\"questionText\":\"new question text\",\"parameter\":" + question.getParameter().getParameterId() + ",\"status\":\"DRAFT\"}";
        String actualResponse = client.toBlocking().retrieve(HttpRequest.PATCH("/v1/contributor/question/" + question.getQuestionId(), "new question text").bearerAuth("anything"), String.class);


        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}
