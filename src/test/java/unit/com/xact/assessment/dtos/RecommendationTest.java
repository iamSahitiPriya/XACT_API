package unit.com.xact.assessment.dtos;

import com.xact.assessment.dtos.Recommendation;
import com.xact.assessment.dtos.RecommendationDeliveryHorizon;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecommendationTest {

    @Test
    void compareByDeliveryHorizon() {
        var rec1 = new Recommendation();
        rec1.setDeliveryHorizon(RecommendationDeliveryHorizon.NOW);
        var rec2 = new Recommendation();
        rec2.setDeliveryHorizon(RecommendationDeliveryHorizon.NEXT);
        var rec3 = new Recommendation();
        rec3.setDeliveryHorizon(RecommendationDeliveryHorizon.LATER);
        var rec4 = new Recommendation();
        rec4.setDeliveryHorizon(RecommendationDeliveryHorizon.NOW);

        int equal = Recommendation.compareByDeliveryHorizon(rec1, rec4);

        int negative1 = Recommendation.compareByDeliveryHorizon(rec1, rec2);
        int negative2 = Recommendation.compareByDeliveryHorizon(rec1, rec3);
        int positive1 = Recommendation.compareByDeliveryHorizon(rec2, rec1);
        int positive2 = Recommendation.compareByDeliveryHorizon(rec3, rec4);

        assertEquals(equal, 0);
        assertTrue(positive1 > 0);
        assertTrue(positive2 > 0);
        assertTrue(negative1 < 0);
        assertTrue(negative2 < 0);
    }

    @Test
    void compareByUpdatedTime() {
        var commonUpdatedDate = new Date();
        var laterUpdatedDate = new Date();
        var c = Calendar.getInstance();
        c.setTime(laterUpdatedDate);
        c.add(Calendar.DATE, 1);
        laterUpdatedDate = c.getTime();

        var rec1 = new Recommendation();
        rec1.setUpdatedAt(commonUpdatedDate);
        var rec2 = new Recommendation();
        rec2.setUpdatedAt(commonUpdatedDate);
        var rec3 = new Recommendation();
        rec3.setUpdatedAt(laterUpdatedDate);

        var equal = Recommendation.compareByUpdatedTime(rec1, rec2);
        var positive = Recommendation.compareByUpdatedTime(rec1, rec3);
        var negative = Recommendation.compareByUpdatedTime(rec3, rec2);

        assertEquals(equal, 0);
        assertTrue(negative < 0);
        assertTrue(positive > 0);
    }
}