package com.tuma_solutions.reporting_demo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public abstract class TextReport {

    protected PrintStream out;

    public void setOutput(OutputStream out) throws IOException {
        this.out = new PrintStream(out);
    }

    public void print(Collection<ReportDataRow> rows) {
        List<String> columnNames = new ArrayList();
        columnNames.addAll(Arrays.asList("Project Name", "Component Name",
            "Percent Complete", "Date Completed", "Total Planned Time",
            "Total Actual Time"));
        columnNames.addAll(ReportDataRow.timePhaseNames);
        columnNames.addAll(ReportDataRow.sizeMetrics);
        columnNames.add("Total Defects Removed");
        columnNames.addAll(ReportDataRow.defectPhaseNames);
        columnNames.add("Design/Code Ratio");
        columnNames.add("PQI");

        printValues(columnNames.toArray());
        out.println();

        for (ReportDataRow row : rows)
            printRow(row);
    }

    private void printRow(ReportDataRow row) {
        printValues(row.projectName, row.wbsElementName,
            new Percentage(row.getPercentComplete()), row.completionDate,
            row.totalPlannedTime, row.totalActualTime, row.timeInPhase,
            row.sizeData, //
            row.defectsRemoved.sum(), row.defectsRemoved,
            row.getDesignToCodeRatio(), row.getPQI());
        out.println();
    }

    private void printValues(Object... values) {
        for (int i = 0; i < values.length; i++) {
            if (i > 0)
                printDelimiter();
            printValue(values[i]);
        }
    }

    protected abstract void printDelimiter();

    private void printValue(Object object) {
        if (object instanceof Date) {
            out.print(esc(DATE_FMT.format((Date) object)));
        } else if (object instanceof Percentage) {
            out.print(esc(PCT_FMT.format(((Percentage) object).doubleValue())));
        } else if (object instanceof Number) {
            double d = ((Number) object).doubleValue();
            if (Double.isInfinite(d) || Double.isNaN(d))
                out.print("#DIV/0!");
            else
                out.print(esc(NUM_FMT.format(d)));
        } else if (object instanceof KeyedData) {
            printValues(((KeyedData) object).asList().toArray());
        } else if (object != null) {
            out.print(esc(String.valueOf(object)));
        }
    }

    protected String esc(String value) {
        return value;
    }

    private static final NumberFormat NUM_FMT = NumberFormat
            .getNumberInstance();

    private static final NumberFormat PCT_FMT = NumberFormat
            .getPercentInstance();

    private static final DateFormat DATE_FMT = new SimpleDateFormat("M/d/yyyy");

    static {
        NUM_FMT.setGroupingUsed(false);
        NUM_FMT.setMaximumFractionDigits(2);
    }

}
