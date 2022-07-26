/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentCategory;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<AssessmentCategory, Integer> {

    @Executable
    @Query("SELECT category FROM AssessmentCategory category WHERE category.isActive=true")
    List<AssessmentCategory> findAll();
}
