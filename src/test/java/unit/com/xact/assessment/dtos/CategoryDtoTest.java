package unit.com.xact.assessment.dtos;

import com.xact.assessment.dtos.CategoryDto;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryDtoTest {

    @Test
    void compareToUpdatedDate() {
        var commonUpdatedDate = new Date();
        var laterUpdatedDate = new Date();
        var c = Calendar.getInstance();
        c.setTime(laterUpdatedDate);
        c.add(Calendar.DATE, 1);
        laterUpdatedDate = c.getTime();

        var categoryDto1 = new CategoryDto();
        categoryDto1.setUpdatedAt(commonUpdatedDate);
        var categoryDto2 = new CategoryDto();
        categoryDto2.setUpdatedAt(commonUpdatedDate);
        var categoryDto3 = new CategoryDto();
        categoryDto3.setUpdatedAt(laterUpdatedDate);

        var equal = categoryDto1.compareTo(categoryDto2);
        var positive = categoryDto1.compareTo(categoryDto3);
        var negative = categoryDto3.compareTo(categoryDto2);

        assertEquals( 0,equal);
        assertTrue(negative < 0);
        assertTrue(positive > 0);
    }
}