package com.tuma_solutions.reporting_demo;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.processdash.api.PDashContext;


public class ProjectDataDemo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // perform a series of queries to load data
        PDashContext ctx = (PDashContext) req
                .getAttribute(PDashContext.REQUEST_ATTR);
        Map<String, ReportDataRow> data = new LinkedHashMap();
        for (DataLoader loader : DATA_LOADERS)
            loader.load(data, ctx);

        // print the results
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition",
            "attachment; filename=projectData.csv");
        TextReport report = new TextReportCsv();
        report.setOutput(resp.getOutputStream());
        report.print(data.values());
    }

    private DataLoader[] DATA_LOADERS = { new DataLoader.LoadLeafComponents(),
            new DataLoader.LoadTimes(), new DataLoader.LoadCompletionDates(),
            new DataLoader.LoadEarnedValue(),
            new DataLoader.LoadTimeByMetricsCollectionFrameworkPhases(),
            new DataLoader.LoadActualSizeData(),
            new DataLoader.LoadDefectRemovalData(), };

}
