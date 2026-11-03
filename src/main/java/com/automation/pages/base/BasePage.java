package com.automation.pages.base;

import com.automation.driver.DriverManager;
import com.automation.utils.logging.Log;
import com.automation.utils.ui.JavaScriptUtils;
import org.openqa.selenium.WebDriver;

/**
 * BasePage containing common operations inherited by all Page Objects.
 */
public abstract class BasePage {

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    public void openUrl(String url) {
        Log.info("Navigating to URL: %s", url);
        getDriver().get(url);
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public void refreshPage() {
        getDriver().navigate().refresh();
    }
}
