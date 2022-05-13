package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentCategory;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.persistence.OrderBy;
import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<AssessmentCategory, Integer> {

    @Executable
    List<AssessmentCategory> findAll();
}
