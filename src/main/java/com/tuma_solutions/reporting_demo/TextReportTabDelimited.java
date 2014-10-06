package com.tuma_solutions.reporting_demo;



public class TextReportTabDelimited extends TextReport {

    protected void printDelimiter() {
        out.print("\t");
    }

}
