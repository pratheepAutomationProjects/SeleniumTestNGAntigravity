package com.automation.reports;

import com.automation.constants.FrameworkConstants;
import com.automation.utils.config.ConfigReader;
import com.automation.utils.logging.Log;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * Initializes and configures the ExtentReports instance.
 */
public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            File reportDir = new File(FrameworkConstants.REPORTS_DIR);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(FrameworkConstants.EXTENT_REPORT_PATH);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Test Automation Execution Report");
            sparkReporter.config().setReportName("Selenium + TestNG Hybrid Framework Report");
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Environment", ConfigReader.get("env", "QA"));
            extent.setSystemInfo("Browser", ConfigReader.get("browser", "chrome"));
            extent.setSystemInfo("User", System.getProperty("user.name"));

            Log.info("ExtentReports initialized at: %s", FrameworkConstants.EXTENT_REPORT_PATH);
        }
        return extent;
    }
}
