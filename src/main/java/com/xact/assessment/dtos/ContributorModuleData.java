package com.xact.assessment.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class ContributorModuleData {
    private Integer moduleId;
    private String moduleName;
    private String categoryName;
    private List<ContributorTopicData> topics;
}
