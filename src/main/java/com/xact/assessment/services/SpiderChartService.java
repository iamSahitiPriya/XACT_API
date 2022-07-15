/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.CategoryMaturity;
import com.xact.assessment.utils.ChartsUtil;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.List;
import java.util.Map;

@Singleton
public class SpiderChartService implements ChartService {
    @SneakyThrows
    @Override
    public byte[] generateChart(Map<String, List<CategoryMaturity>> dataSet) {
        return ChartsUtil.getSpiderChart(680, 480, createDataset());
    }

    private static CategoryDataset createDataset(Map<String, List<CategoryMaturity>> dataMap) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, List<CategoryMaturity>> entry : dataMap.entrySet()) {
            for (CategoryMaturity maturity : entry.getValue()) {
                dataset.addValue(maturity.getScore(), entry.getKey(), maturity.getCategory());
            }
        }
        return dataset;
    }

    private CategoryDataset createDataset() {

        // row keys...
        String series1 = "Desired Maturity";
        String series2 = "Current Maturity";

        // column keys...
        String category1 = "Category 1";
        String category2 = "Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(5.0, series1, category1);
        dataset.addValue(5.0, series1, category2);
        dataset.addValue(5.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);

        dataset.addValue(3.0, series2, category1);
        dataset.addValue(2.0, series2, category2);
        dataset.addValue(2.5, series2, category3);
        dataset.addValue(3.5, series2, category4);
        dataset.addValue(4.0, series2, category5);


        return dataset;

    }
}
