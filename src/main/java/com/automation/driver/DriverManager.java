package com.automation.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-safe WebDriver storage using ThreadLocal.
 * Guarantees isolation when executing parallel tests in TestNG.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    public static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void unload() {
        DRIVER_THREAD_LOCAL.remove();
    }
}
