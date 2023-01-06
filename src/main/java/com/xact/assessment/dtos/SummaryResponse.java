package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SummaryResponse {
    private Long categoryAssessed;
    private Long moduleAssessed;
    private Long topicAssessed;
    private Long parameterAssessed;
    private Integer questionAssessed;
}
