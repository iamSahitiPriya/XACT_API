package com.xact.assessment.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private String comments;
    private boolean active;
}

