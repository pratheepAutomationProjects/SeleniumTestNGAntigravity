package com.automation.tests.base;

import com.automation.driver.DriverFactory;
import com.automation.driver.DriverManager;
import com.automation.utils.config.ConfigReader;
import com.automation.utils.logging.Log;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base Test class managing driver lifecycle for all UI test suites.
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional String browser) {
        String browserToUse = (browser != null && !browser.isEmpty()) 
                ? browser 
                : ConfigReader.get("browser", "chrome");

        Log.info("Starting test with browser: %s", browserToUse);
        WebDriver driver = DriverFactory.createDriver(browserToUse);
        DriverManager.setDriver(driver);

        String appUrl = ConfigReader.get("app.url", "https://www.saucedemo.com");
        Log.info("Navigating to application URL: %s", appUrl);
        driver.get(appUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (DriverManager.getDriver() != null) {
            Log.info("Quitting WebDriver instance");
            DriverManager.getDriver().quit();
            DriverManager.unload();
        }
    }
}
