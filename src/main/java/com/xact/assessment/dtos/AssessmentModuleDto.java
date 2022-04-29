package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentModuleDto {

    private Integer moduleId;
    private String moduleName;
    private Integer category;
    private Set<AssessmentTopicDto> topics;
}
