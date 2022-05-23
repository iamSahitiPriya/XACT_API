/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.repositories.CategoryRepository;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AssessmentMasterDataService {

    CategoryRepository categoryRepository;

    public AssessmentMasterDataService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<AssessmentCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}
