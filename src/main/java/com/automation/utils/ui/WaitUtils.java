package com.automation.utils.ui;

import com.automation.constants.FrameworkConstants;
import com.automation.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Explicit wait utilities for reliable element synchronization.
 */
public final class WaitUtils {

    private WaitUtils() {}

    private static WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }

    public static WebElement waitForElementToBeVisible(By locator) {
        return waitForElementToBeVisible(locator, FrameworkConstants.EXPLICIT_WAIT_TIMEOUT);
    }

    public static WebElement waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementToBeClickable(By locator) {
        return waitForElementToBeClickable(locator, FrameworkConstants.EXPLICIT_WAIT_TIMEOUT);
    }

    public static WebElement waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementPresence(By locator) {
        return waitForElementPresence(locator, FrameworkConstants.EXPLICIT_WAIT_TIMEOUT);
    }

    public static WebElement waitForElementPresence(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> waitForAllElementsVisible(By locator) {
        return getWait(FrameworkConstants.EXPLICIT_WAIT_TIMEOUT).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean waitForUrlContains(String fraction) {
        return getWait(FrameworkConstants.EXPLICIT_WAIT_TIMEOUT).until(ExpectedConditions.urlContains(fraction));
    }

    public static boolean waitForTitleContains(String titleFraction) {
        return getWait(FrameworkConstants.EXPLICIT_WAIT_TIMEOUT).until(ExpectedConditions.titleContains(titleFraction));
    }

    public static boolean waitForInvisibilityOfElement(By locator) {
        return getWait(FrameworkConstants.EXPLICIT_WAIT_TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
