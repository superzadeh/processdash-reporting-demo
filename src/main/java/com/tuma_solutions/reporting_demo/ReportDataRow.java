package com.tuma_solutions.reporting_demo;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class ReportDataRow {

    public String projectName;

    public String wbsElementName;

    public double totalPlannedTime;

    public double totalActualTime;

    public double earnedValueTime;

    public Date completionDate;

    public static SortedSet<String> timePhaseNames = new TreeSet<String>();

    public KeyedData timeInPhase = new KeyedData(timePhaseNames);

    public static SortedSet<String> sizeMetrics = new TreeSet<String>();

    public KeyedData sizeData = new KeyedData(sizeMetrics);

    public static SortedSet<String> defectPhaseNames = new TreeSet<String>();

    public KeyedData defectsRemoved = new KeyedData(defectPhaseNames);


    public double getPercentComplete() {
        return earnedValueTime / totalPlannedTime;
    }

    public double getPercentSpent() {
        return totalActualTime / totalPlannedTime;
    }

    public double getPhaseTimeRatio(String numeratorPhase,
            String denominatorPhase) {
        double numerator = timeInPhase.getData(numeratorPhase);
        double denominator = timeInPhase.getData(denominatorPhase);
        return numerator / denominator;
    }

    public double getDesignToCodeRatio() {
        return getPhaseTimeRatio("Design", "Code");
    }

    public double getDesignReviewRatio() {
        return getPhaseTimeRatio("Design Review", "Design");
    }

    public double getCodeReviewRatio() {
        return getPhaseTimeRatio("Code Review", "Code");
    }

    public double getCompileDefectDensity() {
        return 1000 * defectsRemoved.getData("Compile")
                / sizeData.getData("LOC");
    }

    public double getTestDefectDensity() {
        return 1000 * defectsRemoved.getData("Test") / sizeData.getData("LOC");
    }

    public double getPQI() {
        double axis1 = getDesignToCodeRatio() / 0.5;
        double axis2 = getDesignReviewRatio() / 0.5;
        double axis3 = getCodeReviewRatio() / 0.5;
        double axis4 = 2.0 / (1 + (getCompileDefectDensity() / 10));
        double axis5 = 2.0 / (1 + (getTestDefectDensity() / 5));
        return limit(axis1) * limit(axis2) * limit(axis3) * limit(axis4)
                * limit(axis5);
    }

    private double limit(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d))
            return 1;
        else
            return Math.max(0, Math.min(1, d));
    }

}
