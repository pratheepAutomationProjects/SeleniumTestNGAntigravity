package com.automation.driver;

import com.automation.constants.FrameworkConstants;
import com.automation.utils.config.ConfigReader;
import com.automation.utils.logging.Log;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Factory class to initialize local and remote WebDrivers based on configuration.
 */
public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver createDriver(String browser) {
        WebDriver driver;
        String browserName = (browser != null && !browser.trim().isEmpty()) 
                ? browser.toLowerCase().trim() 
                : ConfigReader.get("browser", "chrome").toLowerCase().trim();

        boolean headless = ConfigReader.getBoolean("headless", false);
        boolean isGrid = ConfigReader.getBoolean("grid.enabled", false);

        Log.info("Initializing WebDriver for browser: %s (Headless: %s, Grid: %s)", browserName, headless, isGrid);

        if (isGrid) {
            driver = createRemoteDriver(browserName, headless);
        } else {
            driver = createLocalDriver(browserName, headless);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(FrameworkConstants.PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(FrameworkConstants.IMPLICIT_WAIT_TIMEOUT));

        return driver;
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--remote-allow-origins=*");
                return new EdgeDriver(edgeOptions);

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                return new ChromeDriver(chromeOptions);
        }
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless) {
        String gridUrl = ConfigReader.get("grid.url", "http://localhost:4444/wd/hub");
        try {
            switch (browser) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) firefoxOptions.addArguments("-headless");
                    return new RemoteWebDriver(new URL(gridUrl), firefoxOptions);

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) edgeOptions.addArguments("--headless=new");
                    return new RemoteWebDriver(new URL(gridUrl), edgeOptions);

                case "chrome":
                default:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    return new RemoteWebDriver(new URL(gridUrl), chromeOptions);
            }
        } catch (MalformedURLException e) {
            Log.error("Invalid Selenium Grid URL: " + gridUrl, e);
            throw new RuntimeException("Grid URL is malformed: " + gridUrl, e);
        }
    }
}
