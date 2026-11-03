package com.automation.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

/**
 * ThreadLocal management of ExtentTest instances for thread-safe parallel test execution.
 */
public final class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private ExtentTestManager() {}

    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    public static void setTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static void startTest(String testName, String description) {
        ExtentTest test = ExtentManager.getInstance().createTest(testName, description);
        setTest(test);
    }

    public static void log(Status status, String message) {
        if (getTest() != null) {
            getTest().log(status, message);
        }
    }

    public static void logWithScreenshot(Status status, String message, String base64Screenshot) {
        if (getTest() != null) {
            if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                getTest().log(status, message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            } else {
                getTest().log(status, message);
            }
        }
    }

    public static void unload() {
        extentTestThreadLocal.remove();
    }
}
