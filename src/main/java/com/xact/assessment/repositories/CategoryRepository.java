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

    @Executable
    @Query(nativeQuery = true, value = "select distinct category from (select distinct module as module_id from (select distinct tap.topic as topic_id from tbl_assessment_parameter t join tbm_assessment_parameter tap on t.parameter_id = tap.parameter_id where t.assessment_id=:assessmentId group by tap.topic) as t join tbm_assessment_topic tq on t.topic_id = tq.topic_id group by module) as tm join tbm_assessment_module tbm on tm.module_id=tbm.module_id ")
    List<Long> getAssessedCategoryByParameter(@Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query(nativeQuery = true, value = "select distinct category  from (select distinct module as module_id from tbl_assessment_topic tbl join tbm_assessment_topic tat on tat.topic_id = tbl.topic_id where tbl.assessment_id=:assessmentId group by module) as tm join tbm_assessment_module tbm on tm.module_id=tbm.module_id ;")
    List<Long> getAssessedCategoryByTopic(@Parameter("assessmentId") Integer assessmentId);

}
