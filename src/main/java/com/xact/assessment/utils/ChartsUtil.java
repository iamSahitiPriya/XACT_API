/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.utils;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ChartsUtil {
    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset.
     * @return The chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
        plot.setStartAngle(54);
        plot.setInteriorGap(0.40);
        plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        JFreeChart chart = new JFreeChart("Assessment Maturity",
                TextTitle.DEFAULT_FONT, plot, false);
        LegendTitle legend = new LegendTitle(plot);
        legend.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legend);
        chart.getPlot().setBackgroundPaint(Color.pink);
        ChartUtilities.applyCurrentTheme(chart);
        return chart;

    }


    public static byte[] getSpiderChart(int width, int height, CategoryDataset dataset) throws IOException {
        JFreeChart chart = createChart(dataset);
        chart.getPlot().setBackgroundPaint(Color.white);
//        File file = new File("spider-chart.png");
//        ChartUtilities.saveChartAsPNG(file, chart, width, height);
        return ChartUtilities.encodeAsPNG(chart.createBufferedImage(width, height));

    }

}
