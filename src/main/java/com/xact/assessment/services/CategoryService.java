/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentModuleRequest;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.repositories.CategoryRepository;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public AssessmentCategory getCategory(Integer categoryId) {
        return categoryRepository.findCategoryById(categoryId);
    }

    public void save(AssessmentCategory assessmentCategory) {
        categoryRepository.save(assessmentCategory);
    }

    public List<AssessmentCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<String> getCategoryNames() {
        return categoryRepository.getCategoryNames();
    }

    public AssessmentCategory findCategoryById(AssessmentModuleRequest assessmentModuleRequest) {
        return categoryRepository.findCategoryById(assessmentModuleRequest.getCategory());
    }

    public void update(AssessmentCategory assessmentCategory) {
        categoryRepository.update(assessmentCategory);
    }

    public List<AssessmentCategory> getCategoriesSortedByUpdatedDate() {
        return categoryRepository.findCategoriesSortedByUpdatedDate();
    }
}
