package com.automation.utils.ui;

import com.automation.driver.DriverManager;
import com.automation.utils.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * High-level wrapper for UI element interactions with built-in synchronization and fallback handling.
 */
public final class ElementActions {

    private ElementActions() {}

    public static void click(By locator, String elementName) {
        try {
            WebElement element = WaitUtils.waitForElementToBeClickable(locator);
            element.click();
            Log.info("Clicked on: %s", elementName);
        } catch (ElementClickInterceptedException e) {
            Log.warn("Click intercepted for %s. Attempting fallback via JavaScript click.", elementName);
            WebElement element = WaitUtils.waitForElementPresence(locator);
            JavaScriptUtils.clickElement(element);
            Log.info("Clicked on %s using JavaScript fallback.", elementName);
        } catch (Exception e) {
            Log.error("Failed to click on " + elementName + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static void type(By locator, String text, String elementName) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
            Log.info("Typed '%s' into: %s", text, elementName);
        } catch (Exception e) {
            Log.error("Failed to type into " + elementName + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static String getText(By locator, String elementName) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(locator);
            String text = element.getText().trim();
            Log.info("Retrieved text from %s: '%s'", elementName, text);
            return text;
        } catch (Exception e) {
            Log.error("Failed to retrieve text from " + elementName + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static boolean isDisplayed(By locator, String elementName) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(locator);
            boolean displayed = element.isDisplayed();
            Log.info("Element %s is displayed: %s", elementName, displayed);
            return displayed;
        } catch (Exception e) {
            Log.warn("Element %s is not displayed: %s", elementName, e.getMessage());
            return false;
        }
    }

    public static void selectByVisibleText(By locator, String visibleText, String elementName) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(locator);
            Select select = new Select(element);
            select.selectByVisibleText(visibleText);
            Log.info("Selected '%s' from dropdown: %s", visibleText, elementName);
        } catch (Exception e) {
            Log.error("Failed to select by visible text from " + elementName + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static void selectByValue(By locator, String value, String elementName) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(locator);
            Select select = new Select(element);
            select.selectByValue(value);
            Log.info("Selected value '%s' from dropdown: %s", value, elementName);
        } catch (Exception e) {
            Log.error("Failed to select by value from " + elementName + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static void hover(By locator, String elementName) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(locator);
            Actions actions = new Actions(DriverManager.getDriver());
            actions.moveToElement(element).perform();
            Log.info("Hovered over: %s", elementName);
        } catch (Exception e) {
            Log.error("Failed to hover over " + elementName + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static void rightClick(By locator, String elementName) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(locator);
            Actions actions = new Actions(DriverManager.getDriver());
            actions.contextClick(element).perform();
            Log.info("Right-clicked on: %s", elementName);
        } catch (Exception e) {
            Log.error("Failed to right-click on " + elementName + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static List<WebElement> getElements(By locator) {
        return WaitUtils.waitForAllElementsVisible(locator);
    }
}
