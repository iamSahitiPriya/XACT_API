package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@Getter
@Setter
public class ReportTopicResponse {
    private String name;
    private double rating;
    private Integer value;
    private List<ReportParameterResponse> children;
}
