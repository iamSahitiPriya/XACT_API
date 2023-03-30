package integration;

import com.xact.assessment.utils.ResourceFileUtil;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;

class ContributorControllerTest {
    @Inject
    @Client("/")
    HttpClient client; //


    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();
}
