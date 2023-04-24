package unit.com.xact.assessment.dtos;

import com.xact.assessment.dtos.TopicDto;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TopicDtoTest {

    @Test
    void compareToUpdatedDate() {
        var commonUpdatedDate = new Date();
        var laterUpdatedDate = new Date();
        var c = Calendar.getInstance();
        c.setTime(laterUpdatedDate);
        c.add(Calendar.DATE, 1);
        laterUpdatedDate = c.getTime();

        var TopicDto1 = new TopicDto();
        TopicDto1.setUpdatedAt(commonUpdatedDate);
        var TopicDto2 = new TopicDto();
        TopicDto2.setUpdatedAt(commonUpdatedDate);
        var TopicDto3 = new TopicDto();
        TopicDto3.setUpdatedAt(laterUpdatedDate);

        var equal = TopicDto1.compareTo(TopicDto2);
        var positive = TopicDto1.compareTo(TopicDto3);
        var negative = TopicDto3.compareTo(TopicDto2);

        assertEquals(0, equal);
        assertTrue(negative < 0);
        assertTrue(positive > 0);
    }
}
