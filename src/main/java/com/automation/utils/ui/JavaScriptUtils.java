package com.automation.utils.ui;

import com.automation.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * JavaScript execution utilities for scrolling, clicking, highlighting, and handling tricky DOM elements.
 */
public final class JavaScriptUtils {

    private JavaScriptUtils() {}

    private static JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) DriverManager.getDriver();
    }

    public static Object executeScript(String script, Object... args) {
        return getJsExecutor().executeScript(script, args);
    }

    public static void clickElement(WebElement element) {
        getJsExecutor().executeScript("arguments[0].click();", element);
    }

    public static void scrollToElement(WebElement element) {
        getJsExecutor().executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public static void scrollToBottom() {
        getJsExecutor().executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public static void scrollToTop() {
        getJsExecutor().executeScript("window.scrollTo(0, 0);");
    }

    public static void highlightElement(WebElement element) {
        getJsExecutor().executeScript("arguments[0].setAttribute('style', 'border: 2px solid red; background: yellow;');", element);
    }

    public static String getPageTitle() {
        return (String) getJsExecutor().executeScript("return document.title;");
    }

    public static String getDocumentReadyState() {
        return (String) getJsExecutor().executeScript("return document.readyState;");
    }
}
