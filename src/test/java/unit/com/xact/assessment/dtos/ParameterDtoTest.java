package unit.com.xact.assessment.dtos;

import com.xact.assessment.dtos.ParameterDto;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParameterDtoTest {

    @Test
    void compareToUpdatedDate() {
        var commonUpdatedDate = new Date();
        var laterUpdatedDate = new Date();
        var c = Calendar.getInstance();
        c.setTime(laterUpdatedDate);
        c.add(Calendar.DATE, 1);
        laterUpdatedDate = c.getTime();

        var ParameterDto1 = new ParameterDto();
        ParameterDto1.setUpdatedAt(commonUpdatedDate);
        var ParameterDto2 = new ParameterDto();
        ParameterDto2.setUpdatedAt(commonUpdatedDate);
        var ParameterDto3 = new ParameterDto();
        ParameterDto3.setUpdatedAt(laterUpdatedDate);

        var equal = ParameterDto1.compareTo(ParameterDto2);
        var positive = ParameterDto1.compareTo(ParameterDto3);
        var negative = ParameterDto3.compareTo(ParameterDto2);

        assertEquals(0, equal);
        assertTrue(negative < 0);
        assertTrue(positive > 0);
    }
}
