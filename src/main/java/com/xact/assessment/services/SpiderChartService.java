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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Singleton
public class SpiderChartService implements ChartService {
    @SneakyThrows
    @Override
    public byte[] generateChart(Map<String, List<CategoryMaturity>> dataSet) {
        return ChartsUtil.getSpiderChart(680, 440, createDataset(dataSet));
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
}
