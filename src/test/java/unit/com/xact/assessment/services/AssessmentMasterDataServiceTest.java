package unit.com.xact.assessment.services;

import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.repositories.CategoryRepository;
import com.xact.assessment.services.AssessmentMasterDataService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssessmentMasterDataServiceTest {

    private CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private AssessmentMasterDataService assessmentMasterDataService = new AssessmentMasterDataService(categoryRepository);

    @Test
    void getAllCategories() {
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(3);
        category.setCategoryName("My category");
        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(categoryRepository.findAll()).thenReturn(allCategories);

        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getAllCategories();

        assertEquals(assessmentCategories, allCategories);

        verify(categoryRepository).findAll();
    }
}
