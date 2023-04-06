/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.AssessmentParameterReference;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssessmentParameterTest {

    @Test
    void hasReferences() {
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        Set<AssessmentParameterReference> references = new HashSet<>();
        AssessmentParameterReference reference = new AssessmentParameterReference();
        references.add(reference);
        assessmentParameter.setReferences(references);
        AssessmentParameter assessmentParameter1 = new AssessmentParameter();

        assertTrue(assessmentParameter.hasReferences());
        assertFalse(assessmentParameter1.hasReferences());
    }
}
