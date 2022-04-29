package integration;

import com.xact.assessment.clients.UserClient;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.Profile;
import com.xact.assessment.models.User;
import com.xact.assessment.repositories.CategoryRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class AssessmentMasterDataControllerTest {

    @Inject
    @Client("/")
    HttpClient client; //

    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @Inject
    CategoryRepository categoryRepository;

    @MockBean(CategoryRepository.class)
    CategoryRepository categoryRepository() {
        return mock(CategoryRepository.class);
    }


    @Test
    void testGetMasterDataCategoryResponse() throws IOException {
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(3);
        category.setCategoryName("My category");
        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(categoryRepository.findAll()).thenReturn(allCategories);
        String expectedResponse = resourceFileUtil.getJsonString("dto/get-master-data-category-response.json");

        String userResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessment-master-data/categories")
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, userResponse);

    }


}
