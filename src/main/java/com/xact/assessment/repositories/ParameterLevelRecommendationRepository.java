package com.xact.assessment.repositories;

import com.xact.assessment.models.ParameterLevelRecommendation;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ParameterLevelRecommendationRepository extends CrudRepository<ParameterLevelRecommendation,Integer> {
}
