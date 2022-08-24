package com.xact.assessment.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AssessmentNotes {
    private int assessmentId;
    private int questionId;
    private String notes;
    private int updatedAt;
}