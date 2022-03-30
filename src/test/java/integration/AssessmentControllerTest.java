/*
package integration;

import com.xact.assessment.models.Assessment;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class AssessmentControllerTest {

    @Inject
    EmbeddedServer server; //

    @Inject
    @Client("/")
    HttpClient client; //


    @Test
    void testGetAssessmentResponse() {
        Assessment assessmentResponse = client.toBlocking() //
                .retrieve(HttpRequest.GET("/v1/assessments/open/125"),Assessment.class);
        assertEquals("Created an anonymous endpoint 125", assessmentResponse.getName()); //
    }
}
*/
