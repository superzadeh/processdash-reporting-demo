package com.tuma_solutions.reporting_demo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sourceforge.processdash.api.PDashContext;

public abstract class DataLoader {

    public void load(Map reportRows, PDashContext ctx) {
        // run an SQL query and retrieve the results
        String hql = getHQL();
        Object[] args = getArgs(ctx);
        List<Object[]> queryResults = ctx.getQuery().query(hql, args);

        // iterate over the data we received from the database
        for (Object[] row : queryResults) {
            // all of our queries put project key and WBS element key in the
            // first two positions. Use these to construct a pseudo-key for
            // one of the rows in our final report.
            Object projectKey = row[0];
            Object wbsElementKey = row[1];
            String reportRowKey = projectKey + ":" + wbsElementKey;
            ReportDataRow reportRow = getReportRow(reportRows, reportRowKey);

            // if we find an applicable report row, save the query data into it
            if (reportRow != null)
                storeQueryData(row, reportRow);
        }
    }

    public ReportDataRow getReportRow(Map rows, String key) {
        return (ReportDataRow) rows.get(key);
    }

    public abstract String getHQL();

    public Object[] getArgs(PDashContext ctx) {
        return new Object[0];
    }

    public abstract void storeQueryData(Object[] rawData, ReportDataRow dest);


    /**
     * An object to get a list of the leaf components in the database
     */
    public static class LoadLeafComponents extends DataLoader {

        @Override
        public String getHQL() {
            return "SELECT pi.project.key, pi.wbsElement.key, "
                    + "pi.project.name, pi.wbsElement.name "
                    + "FROM PlanItem as pi " //
                    + "WHERE pi.leafComponent = 1 "
                    + "ORDER BY pi.wbsElement.name";
        }

        @Override
        public ReportDataRow getReportRow(Map rows, String key) {
            // all of the other DataLoaders look up an existing report row to
            // store data into. This loader is different. It runs first in
            // the loading sequence, so it creates the report rows that the
            // remaining loaders will store data into.
            ReportDataRow result = new ReportDataRow();
            rows.put(key, result);
            return result;
        }

        @Override
        public void storeQueryData(Object[] rawData, ReportDataRow dest) {
            dest.projectName = (String) rawData[2];
            dest.wbsElementName = (String) rawData[3];
        }

    }


    /**
     * Load the total planned and actual time for each WBS element
     */
    public static class LoadTimes extends DataLoader {

        @Override
        public String getHQL() {
            return "SELECT task.planItem.project.key, "
                    + "      task.planItem.wbsElement.key, "
                    + "      SUM(task.planTimeMin), "
                    + "      SUM(task.actualTimeMin) "
                    + "FROM TaskStatusFact as task "
                    + "GROUP BY task.planItem.project.key, "
                    + "      task.planItem.wbsElement.key";
        }

        @Override
        public void storeQueryData(Object[] rawData, ReportDataRow dest) {
            dest.totalPlannedTime = ((Number) rawData[2]).doubleValue() / 60;
            dest.totalActualTime = ((Number) rawData[3]).doubleValue() / 60;
        }
    }


    /**
     * Load the completion date for each WBS element
     */
    public static class LoadCompletionDates extends DataLoader {

        @Override
        public String getHQL() {
            return "SELECT task.planItem.project.key, "
                    + "      task.planItem.wbsElement.key, "
                    + "      MAX(task.actualCompletionDateDim.fullDate) "
                    + "FROM TaskStatusFact as task "
                    + "GROUP BY task.planItem.project.key, "
                    + "      task.planItem.wbsElement.key "
                    + "HAVING MAX(task.actualCompletionDateDim.key) < 99990000";
        }

        @Override
        public void storeQueryData(Object[] rawData, ReportDataRow dest) {
            dest.completionDate = (Date) rawData[2];
        }

    }


    /**
     * Retrieve the value earned on completed tasks
     */
    public static class LoadEarnedValue extends DataLoader {

        @Override
        public String getHQL() {
            return "SELECT task.planItem.project.key, "
                    + "      task.planItem.wbsElement.key, "
                    + "      SUM(task.planTimeMin) "
                    + "FROM TaskStatusFact as task "
                    + "WHERE task.actualCompletionDateDim.key < 99990000 "
                    + "GROUP BY task.planItem.project.key, "
                    + "      task.planItem.wbsElement.key";
        }

        @Override
        public void storeQueryData(Object[] rawData, ReportDataRow dest) {
            dest.earnedValueTime = ((Number) rawData[2]).doubleValue() / 60;
        }
    }


    /**
     * Retrieve time in phase, using MCF phase names as the qualifiers.
     */
    public static class LoadTimeByMetricsCollectionFrameworkPhases extends
            DataLoader {

        @Override
        public String getHQL() {
            return "SELECT task.planItem.project.key, "
                    + "      task.planItem.wbsElement.key, "
                    + "      mapsTo.shortName, "
                    + "      SUM(task.actualTimeMin) "
                    + "FROM TaskStatusFact as task "
                    + "INNER JOIN task.planItem.phase.mapsToPhase mapsTo "
                    + "WHERE mapsTo.process.identifier = ? "
                    + "GROUP BY task.planItem.project.key, "
                    + "      task.planItem.wbsElement.key, "
                    + "      mapsTo.shortName";
        }

        @Override
        public Object[] getArgs(PDashContext ctx) {
            String pid = ctx.getData().getString("Process_ID");
            if (pid == null)
                pid = "TSP";
            return new Object[] { pid };
        }

        @Override
        public void storeQueryData(Object[] rawData, ReportDataRow dest) {
            String phaseName = (String) rawData[2];
            double timeInPhase = ((Number) rawData[3]).doubleValue() / 60;
            dest.timeInPhase.putData(phaseName, timeInPhase);
        }

    }


    /**
     * Load actual size data for each component
     */
    public static class LoadActualSizeData extends DataLoader {

        @Override
        public String getHQL() {
            return "SELECT size.planItem.project.key, "
                    + "      size.planItem.wbsElement.key, "
                    + "      size.sizeMetric.shortName, "
                    + "      SUM(size.addedAndModifiedSize) "
                    + "FROM SizeFact as size "
                    + "WHERE size.measurementType.name = 'Actual' "
                    + "GROUP BY size.planItem.project.key, "
                    + "      size.planItem.wbsElement.key, "
                    + "      size.sizeMetric.shortName";
        }

        @Override
        public void storeQueryData(Object[] rawData, ReportDataRow dest) {
            String sizeMetric = (String) rawData[2];
            double size = ((Number) rawData[3]).doubleValue();
            dest.sizeData.putData(sizeMetric, size);
        }

    }


    /**
     * Load defects removed by phase for each component
     */
    public static class LoadDefectRemovalData extends DataLoader {

        @Override
        public String getHQL() {
            return "SELECT defect.planItem.project.key, "
                    + "      defect.planItem.wbsElement.key, "
                    + "      defect.removedPhase.shortName, "
                    + "      SUM(defect.fixCount) "
                    + "FROM DefectLogFact as defect "
                    + "GROUP BY defect.planItem.project.key, "
                    + "      defect.planItem.wbsElement.key, "
                    + "      defect.removedPhase.shortName";
        }

        @Override
        public void storeQueryData(Object[] rawData, ReportDataRow dest) {
            String phaseName = (String) rawData[2];
            double defectCount = ((Number) rawData[3]).doubleValue();
            dest.defectsRemoved.putData(phaseName, defectCount);
        }
    }

}
