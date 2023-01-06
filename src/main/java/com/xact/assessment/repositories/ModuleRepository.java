/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentModule;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ModuleRepository extends CrudRepository<AssessmentModule, Integer> {
    @Executable
    @Query("SELECT pla FROM AssessmentModule pla WHERE pla.category.categoryId=:categoryId")
    List<AssessmentModule> findByCategory(@Parameter("categoryId") Integer categoryId);

    @Executable
    @Query("SELECT module FROM AssessmentModule module WHERE module.moduleId=:moduleId")
    AssessmentModule findByModuleId(Integer moduleId);

    @Executable
    @Query(nativeQuery = true, value = "select sum(total_module) from (select count(distinct module) as total_module from (select distinct tap.topic as topic_id from tbl_assessment_parameter t join tbm_assessment_parameter tap on t.parameter_id = tap.parameter_id where t.assessment_id=:assessmentId group by tap.topic) as t join tbm_assessment_topic tq on t.topic_id = tq.topic_id group by module) as module")
    Long getAssessedModuleByParameter(@Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query(nativeQuery = true, value = "select sum(module_count) from (select count(distinct module) as module_count from tbl_assessment_topic tbl join tbm_assessment_topic tat on tat.topic_id = tbl.topic_id where tbl.assessment_id=:assessmentId group by module) as module_count;")
    Long getAssessedModuleByTopic(@Parameter("assessmentId") Integer assessmentId);

}
