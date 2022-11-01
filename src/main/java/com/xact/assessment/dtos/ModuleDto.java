package com.xact.assessment.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.SortedSet;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class ModuleDto {
    private Integer moduleId ;
    private String moduleName;
    private String categoryName;
    private boolean isActive;
    private Date updatedAt;
    private String comments;
    private SortedSet<AssessmentTopicDto> topics;
}
