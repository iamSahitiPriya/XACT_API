/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.AssessmentTopicReference;
import com.xact.assessment.repositories.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import com.xact.assessment.services.ParameterService;
import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.TopicService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssessmentMasterDataServiceTest {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final ModuleRepository moduleRepository = mock(ModuleRepository.class);
    private final TopicService topicService = mock(TopicService.class);
    private final ParameterService parameterService = mock(ParameterService.class);
    private final QuestionService questionService = mock(QuestionService.class);
    private final AssessmentTopicReferenceRepository assessmentTopicReferenceRepository = mock(AssessmentTopicReferenceRepository.class);
    private final AssessmentParameterReferenceRepository assessmentParameterReferenceRepository = mock(AssessmentParameterReferenceRepository.class);

    private final AssessmentMasterDataService assessmentMasterDataService = new AssessmentMasterDataService(categoryRepository, moduleRepository, questionService,assessmentTopicReferenceRepository, parameterService,topicService,assessmentParameterReferenceRepository);

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
