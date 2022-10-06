/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AssessmentMasterDataController;
import com.xact.assessment.dtos.AssessmentCategoryDto;
import com.xact.assessment.dtos.UserAssessmentResponse;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AssessmentMasterDataControllerTest {

    AssessmentMasterDataService assessmentMasterDataService = Mockito.mock(AssessmentMasterDataService.class);

    AssessmentMasterDataController assessmentMasterDataController = new AssessmentMasterDataController(assessmentMasterDataService);

    @Test
    void getAssessmentMasterData() {
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(3);
        category.setCategoryName("My category");
        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(assessmentMasterDataService.getCategories()).thenReturn(allCategories);

        HttpResponse<UserAssessmentResponse> userAssessmentResponseHttpResponse = assessmentMasterDataController.getAssessmentMasterData(assessment.getAssessmentId());

        List<AssessmentCategoryDto> assessmentCategoryDto=userAssessmentResponseHttpResponse.body().getAssessmentCategories();
        AssessmentCategoryDto firstAssessmentCategory = assessmentCategoryDto.get(0);
        assertEquals(firstAssessmentCategory.getCategoryId(), category.getCategoryId());
        assertEquals(firstAssessmentCategory.getCategoryName(), category.getCategoryName());
    }




}
