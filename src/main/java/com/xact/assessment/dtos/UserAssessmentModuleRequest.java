package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.model.query.QueryModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserAssessmentModuleRequest {
    @JsonProperty("modules")
    private List<ModuleRequest> moduleRequests;
}
