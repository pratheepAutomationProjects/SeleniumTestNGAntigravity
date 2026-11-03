package com.automation.listeners;

import com.automation.driver.DriverManager;
import com.automation.reports.CustomTabularReportGenerator;
import com.automation.reports.ExtentManager;
import com.automation.reports.ExtentTestManager;
import com.automation.utils.logging.Log;
import com.automation.utils.ui.ScreenshotUtils;
import com.aventstack.extentreports.Status;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Master TestNG Listener integrating:
 * 1. ExtentReports (with automatic screenshot attachments on failure)
 * 2. Allure Reports (with failure screenshots)
 * 3. Custom Tabular Reports (summary.html & detailed_report.html)
 * 4. Structured Log4j2 execution tracing
 */
public class TestListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        Log.info("========== SUITE EXECUTION STARTED: %s ==========", suite.getName());
        ExtentManager.getInstance();
        CustomTabularReportGenerator.recordSuiteStart();
    }

    @Override
    public void onFinish(ISuite suite) {
        Log.info("========== SUITE EXECUTION FINISHED: %s ==========", suite.getName());
        ExtentManager.getInstance().flush();
        CustomTabularReportGenerator.generateReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String className = result.getTestClass().getRealClass().getSimpleName();

        Log.info(">>>>> STARTING TEST: %s.%s <<<<<", className, testName);
        ExtentTestManager.startTest(className + " :: " + testName, description);
        ExtentTestManager.log(Status.INFO, "Test execution started for: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();
        String description = result.getMethod().getDescription();

        Log.info(">>>>> TEST PASSED: %s (Duration: %d ms) <<<<<", testName, duration);
        ExtentTestManager.log(Status.PASS, "Test passed successfully.");
        ExtentTestManager.unload();

        CustomTabularReportGenerator.addRecord(
                new CustomTabularReportGenerator.TestRecord(className, testName, description, "PASS", duration, null)
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();
        String description = result.getMethod().getDescription();

        Throwable throwable = result.getThrowable();
        StringWriter sw = new StringWriter();
        if (throwable != null) {
            throwable.printStackTrace(new PrintWriter(sw));
        }
        String errorTrace = sw.toString();

        Log.error(">>>>> TEST FAILED: " + testName + " - " + (throwable != null ? throwable.getMessage() : "No exception details"), throwable);

        // Capture screenshot if driver is active (UI tests)
        String base64Screenshot = "";
        if (DriverManager.getDriver() != null) {
            base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64();
            ScreenshotUtils.captureScreenshotAsFile(testName);
            AllureListener.saveFailureScreenshot();
        }

        ExtentTestManager.logWithScreenshot(Status.FAIL, "Test failed: " + (throwable != null ? throwable.getMessage() : "Unknown error"), base64Screenshot);
        if (!errorTrace.isEmpty()) {
            ExtentTestManager.log(Status.FAIL, "<pre>" + errorTrace + "</pre>");
        }
        ExtentTestManager.unload();

        CustomTabularReportGenerator.addRecord(
                new CustomTabularReportGenerator.TestRecord(className, testName, description, "FAIL", duration, errorTrace)
        );
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();
        String description = result.getMethod().getDescription();
        String reason = (result.getThrowable() != null) ? result.getThrowable().getMessage() : "Skipped";

        Log.warn(">>>>> TEST SKIPPED: %s (Reason: %s) <<<<<", testName, reason);
        ExtentTestManager.log(Status.SKIP, "Test skipped: " + reason);
        ExtentTestManager.unload();

        CustomTabularReportGenerator.addRecord(
                new CustomTabularReportGenerator.TestRecord(className, testName, description, "SKIP", duration, reason)
        );
    }
}
