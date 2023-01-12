package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SummaryResponse {
    private Integer categoryAssessed;
    private Integer moduleAssessed;
    private Integer topicAssessed;
    private Integer parameterAssessed;
    private Integer questionAssessed;
}
