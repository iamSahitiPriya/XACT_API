package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@Getter
@Setter
public class ReportModuleResponse {
    private String name;
    private double rating;
    private List<ReportTopicResponse> children;
}
