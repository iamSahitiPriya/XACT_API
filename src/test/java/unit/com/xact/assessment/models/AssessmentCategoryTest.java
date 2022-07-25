/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.AssessmentModule;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AssessmentCategoryTest {

    @Test
    void getModulesWhenModulesNotExist() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assertNull(assessmentCategory.getModules());

    }

    @Test
    void getOnlyActiveModulesWhenModulesExist() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();

        Set<AssessmentModule> modules = new HashSet<>();
        AssessmentModule activeModule=new AssessmentModule();
        activeModule.setModuleName("active module");
        activeModule.setActive(true);
        modules.add(activeModule);

        AssessmentModule inactiveModule=new AssessmentModule();
        inactiveModule.setModuleName("inactive module");
        inactiveModule.setActive(false);
        modules.add(inactiveModule);

        assessmentCategory.setModules(modules);
        assertEquals(1,assessmentCategory.getModules().size());
        assertEquals("active module",assessmentCategory.getModules().iterator().next().getModuleName());

    }
}
