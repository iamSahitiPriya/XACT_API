/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.xact.assessment.models.ModuleContributor;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ModuleResponse {
    private Integer moduleId;
    private String moduleName;
    private Integer categoryId;
    private Date updatedAt;
    private Set<ContributorDto> contributors;
    private String comments;
    private boolean active;
}

