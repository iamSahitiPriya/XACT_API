/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.CategoryMaturity;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;

@Singleton
public interface ChartService {

    byte[] generateChart(Map<String, List<CategoryMaturity>> dataSet);
}
