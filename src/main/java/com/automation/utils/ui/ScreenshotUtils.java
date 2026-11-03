package com.automation.utils.ui;

import com.automation.constants.FrameworkConstants;
import com.automation.driver.DriverManager;
import com.automation.utils.logging.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screenshot utility supporting local file storage, Base64 strings, and raw bytes.
 */
public final class ScreenshotUtils {

    private ScreenshotUtils() {}

    public static String captureScreenshotAsFile(String testName) {
        if (DriverManager.getDriver() == null) {
            return null;
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            byte[] bytes = ts.getScreenshotAs(OutputType.BYTES);

            Path screenshotsDir = Paths.get(FrameworkConstants.SCREENSHOTS_DIR);
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
            String sanitizedTestName = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String fileName = sanitizedTestName + "_" + timestamp + ".png";
            Path destination = screenshotsDir.resolve(fileName);

            Files.write(destination, bytes);
            Log.info("Saved screenshot to: %s", destination.toAbsolutePath());
            return destination.toAbsolutePath().toString();
        } catch (IOException e) {
            Log.error("Failed to save screenshot file: " + e.getMessage(), e);
            return null;
        }
    }

    public static String captureScreenshotAsBase64() {
        if (DriverManager.getDriver() == null) {
            return "";
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            Log.error("Failed to capture Base64 screenshot: " + e.getMessage(), e);
            return "";
        }
    }

    public static byte[] captureScreenshotAsBytes() {
        if (DriverManager.getDriver() == null) {
            return new byte[0];
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            return ts.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            Log.error("Failed to capture screenshot bytes: " + e.getMessage(), e);
            return new byte[0];
        }
    }
}
