package unit.com.xact.assessment.dtos;

import com.xact.assessment.dtos.AssessmentCategoryDto;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentCategoryDtoTest {

    @Test
    void compareToCategoryId() {

        var AssessmentCategoryDto1 = new AssessmentCategoryDto();
        AssessmentCategoryDto1.setCategoryId(1);
        var AssessmentCategoryDto2 = new AssessmentCategoryDto();
        AssessmentCategoryDto2.setCategoryId(1);
        var AssessmentCategoryDto3 = new AssessmentCategoryDto();
        AssessmentCategoryDto3.setCategoryId(2);

        var equal = AssessmentCategoryDto1.compareTo(AssessmentCategoryDto2);
        var negative = AssessmentCategoryDto1.compareTo(AssessmentCategoryDto3);
        var positive = AssessmentCategoryDto3.compareTo(AssessmentCategoryDto2);

        assertEquals(0, equal);
        assertTrue(negative < 0);
        assertTrue(positive > 0);
    }
}