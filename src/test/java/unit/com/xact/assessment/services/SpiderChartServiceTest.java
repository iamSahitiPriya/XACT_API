package unit.com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.CategoryMaturity;
import com.xact.assessment.services.SpiderChartService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

  class SpiderChartServiceTest {

    private final SpiderChartService chartService = new SpiderChartService();

    @Test
    void shouldGenerateSpiderChart() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);

        List<AssessmentCategory> assessmentCategoryList = new ArrayList<>();
        AssessmentCategory assessmentCategory1 = new AssessmentCategory();
        assessmentCategory1.setCategoryId(1);
        assessmentCategory1.setCategoryName("First Category");
        assessmentCategoryList.add(assessmentCategory1);

        AssessmentCategory assessmentCategory2 = new AssessmentCategory();
        assessmentCategory2.setCategoryId(1);
        assessmentCategory2.setCategoryName("Second Category");
        assessmentCategoryList.add(assessmentCategory2);

        Map<String, List<CategoryMaturity>> dataSet = new HashMap<>();
        List<CategoryMaturity> listOfCurrentScores = new ArrayList<>();
        for (AssessmentCategory assessmentCategory : assessmentCategoryList) {
            CategoryMaturity categoryMaturity = new CategoryMaturity();
            double avgCategoryScore = 4.0;
            categoryMaturity.setCategory(assessmentCategory.getCategoryName());
            categoryMaturity.setScore(avgCategoryScore);
            listOfCurrentScores.add(categoryMaturity);
        }
        dataSet.put("Maturity", listOfCurrentScores);
        List<CategoryMaturity> listOfNewScores = new ArrayList<>();
        for (AssessmentCategory assessmentCategory : assessmentCategoryList) {
            CategoryMaturity categoryMaturity = new CategoryMaturity();
            double avgCategoryScore = 4.0;
            categoryMaturity.setCategory(assessmentCategory.getCategoryName());
            categoryMaturity.setScore(avgCategoryScore);
            listOfNewScores.add(categoryMaturity);
        }
        dataSet.put("New Maturity", listOfNewScores);

        byte[] resultant = chartService.generateChart(dataSet);
        assertTrue(resultant.length > 0);

    }
}
