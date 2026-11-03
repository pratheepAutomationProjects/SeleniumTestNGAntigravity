package com.automation.listeners;

import com.automation.utils.ui.ScreenshotUtils;
import io.qameta.allure.Attachment;

/**
 * Allure attachment helper for embedding screenshots and text logs into Allure reports.
 */
public final class AllureListener {

    private AllureListener() {}

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public static byte[] saveFailureScreenshot() {
        return ScreenshotUtils.captureScreenshotAsBytes();
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message, String text) {
        return text;
    }
}
