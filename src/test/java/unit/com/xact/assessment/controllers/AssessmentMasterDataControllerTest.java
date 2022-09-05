/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AssessmentMasterDataController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AssessmentMasterDataControllerTest {

    AssessmentMasterDataService assessmentMasterDataService = Mockito.mock(AssessmentMasterDataService.class);

    AssessmentMasterDataController assessmentMasterDataController = new AssessmentMasterDataController(assessmentMasterDataService);

    @Test
    void getAssessmentMasterData() {
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(3);
        category.setCategoryName("My category");
        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(assessmentMasterDataService.getAllCategories()).thenReturn(allCategories);

        HttpResponse<List<AssessmentCategoryDto>> assessmentMasterDataResponse = assessmentMasterDataController.getAssessmentMasterData();

        AssessmentCategoryDto firstAssessmentCategory = assessmentMasterDataResponse.body().get(0);
        assertEquals(firstAssessmentCategory.getCategoryId(), category.getCategoryId());
        assertEquals(firstAssessmentCategory.getCategoryName(), category.getCategoryName());
    }




}
