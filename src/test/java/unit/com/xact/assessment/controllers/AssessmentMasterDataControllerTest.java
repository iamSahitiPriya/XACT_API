/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AssessmentMasterDataController;
import com.xact.assessment.dtos.CategoryDto;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AssessmentMasterDataControllerTest {

    AssessmentMasterDataService assessmentMasterDataService = Mockito.mock(AssessmentMasterDataService.class);

    private final Authentication authentication = Mockito.mock(Authentication.class);
    AssessmentMasterDataController assessmentMasterDataController = new AssessmentMasterDataController(assessmentMasterDataService);


    @Test
    void shouldGetCategories() {
        Date created = new Date(2022 - 11 - 14);
        Date updated = new Date(2022 - 11 - 24);

        AssessmentCategory assessmentCategory=new AssessmentCategory();
        assessmentCategory.setCategoryId(1);
        assessmentCategory.setCategoryName("category");
        assessmentCategory.setActive(true);
        assessmentCategory.setComments("comments");
        assessmentCategory.setCreatedAt(created);
        assessmentCategory.setCreatedAt(updated);

        CategoryDto categoryDto= new CategoryDto();
        categoryDto.setCategoryId(assessmentCategory.getCategoryId());
        categoryDto.setCategoryName(assessmentCategory.getCategoryName());
        categoryDto.setActive(assessmentCategory.getIsActive());
        categoryDto.setComments(assessmentCategory.getComments());
        categoryDto.setUpdatedAt(assessmentCategory.getUpdatedAt());

        List<AssessmentCategory> categories=new ArrayList<>();
        categories.add(assessmentCategory);
        List<CategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        assessmentCategoriesResponse.add(categoryDto);
        when(assessmentMasterDataService.getCategoriesSortedByUpdatedDate()).thenReturn(categories);

        HttpResponse<List<CategoryDto>> actualResponse = assessmentMasterDataController.getMasterData(authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        assertEquals("category",actualResponse.body().get(0).getCategoryName());
    }


}
