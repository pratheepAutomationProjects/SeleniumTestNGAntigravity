package com.automation.reports;

import com.automation.constants.FrameworkConstants;
import com.automation.utils.logging.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Automatically generates customized tabular format reports:
 * 1. summary.html (Executive Summary)
 * 2. detailed_report.html (Detailed Execution Table with filters and error traces)
 */
public final class CustomTabularReportGenerator {

    public static class TestRecord {
        private final String className;
        private final String methodName;
        private final String description;
        private final String status;
        private final long durationMs;
        private final String errorMessage;

        public TestRecord(String className, String methodName, String description, String status, long durationMs, String errorMessage) {
            this.className = className;
            this.methodName = methodName;
            this.description = description;
            this.status = status;
            this.durationMs = durationMs;
            this.errorMessage = errorMessage;
        }

        public String getClassName() { return className; }
        public String getMethodName() { return methodName; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
        public long getDurationMs() { return durationMs; }
        public String getErrorMessage() { return errorMessage; }
    }

    private static final List<TestRecord> testRecords = new ArrayList<>();
    private static long suiteStartTime = System.currentTimeMillis();

    private CustomTabularReportGenerator() {}

    public static synchronized void recordSuiteStart() {
        suiteStartTime = System.currentTimeMillis();
        testRecords.clear();
    }

    public static synchronized void addRecord(TestRecord record) {
        testRecords.add(record);
    }

    public static synchronized void generateReports() {
        long suiteEndTime = System.currentTimeMillis();
        long totalDurationMs = suiteEndTime - suiteStartTime;

        int total = testRecords.size();
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (TestRecord r : testRecords) {
            if ("PASS".equalsIgnoreCase(r.getStatus())) passed++;
            else if ("FAIL".equalsIgnoreCase(r.getStatus())) failed++;
            else skipped++;
        }

        double passRate = (total > 0) ? ((double) passed / total) * 100 : 0.0;

        generateSummaryHtml(total, passed, failed, skipped, passRate, totalDurationMs);
        generateDetailedHtml(testRecords, total, passed, failed, skipped, totalDurationMs);
    }

    private static void generateSummaryHtml(int total, int passed, int failed, int skipped, double passRate, long durationMs) {
        File dir = new File(FrameworkConstants.REPORTS_DIR);
        if (!dir.exists()) dir.mkdirs();

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>QA Automation Execution - Summary Report</title>\n" +
                "  <style>\n" +
                "    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background-color: #f8fafc; color: #1e293b; margin: 0; padding: 24px; }\n" +
                "    .container { max-width: 1000px; margin: 0 auto; background: white; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); padding: 32px; }\n" +
                "    h1 { margin-top: 0; color: #0f172a; font-size: 26px; border-bottom: 2px solid #e2e8f0; padding-bottom: 16px; }\n" +
                "    .cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 16px; margin: 24px 0; }\n" +
                "    .card { padding: 20px; border-radius: 8px; text-align: center; color: white; }\n" +
                "    .card-total { background: #3b82f6; }\n" +
                "    .card-pass { background: #10b981; }\n" +
                "    .card-fail { background: #ef4444; }\n" +
                "    .card-skip { background: #f59e0b; }\n" +
                "    .card-rate { background: #8b5cf6; }\n" +
                "    .card h3 { margin: 0 0 8px 0; font-size: 14px; text-transform: uppercase; letter-spacing: 0.05em; opacity: 0.9; }\n" +
                "    .card .value { font-size: 32px; font-weight: bold; margin: 0; }\n" +
                "    .meta-table { width: 100%; border-collapse: collapse; margin-top: 24px; }\n" +
                "    .meta-table td { padding: 12px; border-bottom: 1px solid #e2e8f0; }\n" +
                "    .meta-table td:first-child { font-weight: 600; width: 30%; color: #64748b; }\n" +
                "    .actions { margin-top: 32px; display: flex; gap: 16px; }\n" +
                "    .btn { display: inline-block; padding: 12px 20px; border-radius: 6px; text-decoration: none; font-weight: 600; font-size: 14px; }\n" +
                "    .btn-primary { background: #2563eb; color: white; }\n" +
                "    .btn-secondary { background: #e2e8f0; color: #1e293b; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "  <h1>QA Test Automation - Executive Summary</h1>\n" +
                "  <div class=\"cards\">\n" +
                "    <div class=\"card card-total\"><h3>Total Tests</h3><div class=\"value\">" + total + "</div></div>\n" +
                "    <div class=\"card card-pass\"><h3>Passed</h3><div class=\"value\">" + passed + "</div></div>\n" +
                "    <div class=\"card card-fail\"><h3>Failed</h3><div class=\"value\">" + failed + "</div></div>\n" +
                "    <div class=\"card card-skip\"><h3>Skipped</h3><div class=\"value\">" + skipped + "</div></div>\n" +
                "    <div class=\"card card-rate\"><h3>Pass Rate</h3><div class=\"value\">" + String.format("%.1f", passRate) + "%</div></div>\n" +
                "  </div>\n" +
                "  <table class=\"meta-table\">\n" +
                "    <tr><td>Generated At</td><td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</td></tr>\n" +
                "    <tr><td>Total Execution Time</td><td>" + (durationMs / 1000.0) + " seconds</td></tr>\n" +
                "    <tr><td>Operating System</td><td>" + System.getProperty("os.name") + "</td></tr>\n" +
                "    <tr><td>Java Version</td><td>" + System.getProperty("java.version") + "</td></tr>\n" +
                "  </table>\n" +
                "  <div class=\"actions\">\n" +
                "    <a class=\"btn btn-primary\" href=\"detailed_report.html\">View Detailed Tabular Report &rarr;</a>\n" +
                "    <a class=\"btn btn-secondary\" href=\"extent-report.html\">Open Extent Spark Report</a>\n" +
                "  </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        writeToFile(FrameworkConstants.TABULAR_SUMMARY_REPORT, html);
    }

    private static void generateDetailedHtml(List<TestRecord> records, int total, int passed, int failed, int skipped, long durationMs) {
        StringBuilder rows = new StringBuilder();
        int index = 1;

        for (TestRecord r : records) {
            String badgeClass = "badge-pass";
            if ("FAIL".equalsIgnoreCase(r.getStatus())) badgeClass = "badge-fail";
            else if ("SKIP".equalsIgnoreCase(r.getStatus())) badgeClass = "badge-skip";

            rows.append("<tr>\n")
                    .append("  <td>").append(index++).append("</td>\n")
                    .append("  <td><strong>").append(r.getClassName()).append("</strong></td>\n")
                    .append("  <td><code>").append(r.getMethodName()).append("</code><br><small style=\"color:#64748b;\">").append(r.getDescription() != null ? r.getDescription() : "").append("</small></td>\n")
                    .append("  <td><span class=\"badge ").append(badgeClass).append("\">").append(r.getStatus()).append("</span></td>\n")
                    .append("  <td>").append(r.getDurationMs()).append(" ms</td>\n")
                    .append("  <td>").append(r.getErrorMessage() != null ? "<pre class=\"error-log\">" + escapeHtml(r.getErrorMessage()) + "</pre>" : "-").append("</td>\n")
                    .append("</tr>\n");
        }

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>QA Automation Execution - Detailed Report</title>\n" +
                "  <style>\n" +
                "    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background-color: #f8fafc; color: #1e293b; margin: 0; padding: 24px; }\n" +
                "    .container { max-width: 1200px; margin: 0 auto; background: white; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); padding: 32px; }\n" +
                "    h1 { margin-top: 0; color: #0f172a; font-size: 26px; border-bottom: 2px solid #e2e8f0; padding-bottom: 16px; display: flex; justify-content: space-between; align-items: center; }\n" +
                "    .badge { padding: 4px 10px; border-radius: 9999px; font-weight: 600; font-size: 12px; text-transform: uppercase; }\n" +
                "    .badge-pass { background-color: #d1fae5; color: #065f46; }\n" +
                "    .badge-fail { background-color: #fee2e2; color: #991b1b; }\n" +
                "    .badge-skip { background-color: #fef3c7; color: #92400e; }\n" +
                "    table { width: 100%; border-collapse: collapse; margin-top: 24px; }\n" +
                "    th, td { text-align: left; padding: 12px 14px; border-bottom: 1px solid #e2e8f0; font-size: 14px; vertical-align: top; }\n" +
                "    th { background: #f1f5f9; color: #475569; font-weight: 600; }\n" +
                "    .error-log { max-height: 120px; overflow-y: auto; background: #fff1f2; color: #b91c1c; padding: 8px; border-radius: 4px; font-size: 12px; margin: 0; }\n" +
                "    .back-link { font-size: 14px; color: #2563eb; text-decoration: none; font-weight: normal; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "  <h1>QA Test Automation - Detailed Execution Report <a class=\"back-link\" href=\"summary.html\">&larr; Back to Summary</a></h1>\n" +
                "  <table>\n" +
                "    <thead>\n" +
                "      <tr>\n" +
                "        <th>#</th>\n" +
                "        <th>Class</th>\n" +
                "        <th>Test Method</th>\n" +
                "        <th>Status</th>\n" +
                "        <th>Duration</th>\n" +
                "        <th>Details / Failure Reason</th>\n" +
                "      </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                rows.toString() +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        writeToFile(FrameworkConstants.TABULAR_DETAILED_REPORT, html);
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
            Log.info("Generated custom tabular report at: %s", filePath);
        } catch (IOException e) {
            Log.error("Failed to generate report at " + filePath + ": " + e.getMessage(), e);
        }
    }
}
