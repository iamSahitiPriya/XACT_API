/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentCategory;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<AssessmentCategory, Integer> {

    @Executable
    @Query("SELECT category FROM AssessmentCategory category ORDER BY category.isActive DESC")
    List<AssessmentCategory> findAll();

    @Executable
    @Query("SELECT category FROM AssessmentCategory category WHERE category.categoryId=:categoryId")
    AssessmentCategory findCategoryById(Integer categoryId);

    @Executable
    @Query("SELECT category FROM AssessmentCategory category ORDER BY category.updatedAt DESC ")
    List<AssessmentCategory> findCategories();

    @Executable
    @Query("SELECT category.categoryName FROM AssessmentCategory category")
    List<String> getAllCategories();


}
