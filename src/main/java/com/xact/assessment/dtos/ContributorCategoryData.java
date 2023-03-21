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
public class ContributorCategoryData {
    private String categoryName;
    private List<ContributorModuleData> modules;
}
