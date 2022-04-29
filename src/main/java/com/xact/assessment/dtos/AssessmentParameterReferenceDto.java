package com.xact.assessment.dtos;


import com.xact.assessment.models.Rating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentParameterReferenceDto {

    private Integer referenceId;
    private Integer parameter;
    private Rating rating;
    private String reference;
}
