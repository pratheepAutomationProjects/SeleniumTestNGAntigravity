package com.automation.tests.base;

import com.automation.utils.config.ConfigReader;
import com.automation.utils.logging.Log;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

/**
 * Base Test class configuring REST Assured for all API test suites.
 */
public abstract class BaseApiTest {

    @BeforeClass(alwaysRun = true)
    public void setupApi() {
        String baseUri = ConfigReader.get("api.base.url", "https://jsonplaceholder.typicode.com");
        Log.info("Configuring REST Assured base URI: %s", baseUri);
        RestAssured.baseURI = baseUri;
        RestAssured.filters(new AllureRestAssured());
    }
}
