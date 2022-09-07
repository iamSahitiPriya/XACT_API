package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@Getter
@Setter
public class ReportDataResponse {
    private String name;
    private List<ReportCategoryResponse> children;
}
