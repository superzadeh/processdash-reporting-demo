package com.tuma_solutions.reporting_demo;



public class TextReportCsv extends TextReport {

    protected void printDelimiter() {
        out.print(",");
    }

    @Override
    protected String esc(String value) {
        if (value.indexOf('"') == -1
                && value.indexOf(',') == -1
                && value.indexOf(' ') == -1)
            return value;
        else
            return "\"" + value.replaceAll("\"", "\"\"") + "\"";
    }

}
