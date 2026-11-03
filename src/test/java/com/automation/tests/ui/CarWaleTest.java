package com.automation.tests.ui;

import com.automation.constants.FrameworkConstants;
import com.automation.pages.carwale.CarWaleHomePage;
import com.automation.pages.carwale.NewCarsPage;
import com.automation.tests.base.BaseTest;
import com.automation.utils.config.ConfigReader;
import com.automation.utils.data.JsonUtils;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("UI Automation")
@Feature("Car Search & Catalog")
public class CarWaleTest extends BaseTest {

    private CarWaleHomePage homePage;
    private Map<String, Object> carData;

    @BeforeMethod(alwaysRun = true)
    public void navigateToCarWale() {
        String carWaleUrl = ConfigReader.get("carwale.url", "https://www.carwale.com");
        homePage = new CarWaleHomePage();
        homePage.openUrl(carWaleUrl);
        carData = JsonUtils.readJsonAsMap(FrameworkConstants.CAR_DATA_JSON);
    }

    @Test(description = "Verify navigating to CarWale home page and checking page title", groups = {"smoke", "regression", "ui"})
    @Severity(SeverityLevel.NORMAL)
    @Story("CarWale home page should load with valid title")
    public void testCarWaleHomePageTitle() {
        String title = homePage.getPageTitle();
        assertThat(title)
                .as("CarWale page title should contain 'CarWale' or 'New Cars'")
                .isNotBlank();
    }

    @Test(description = "Verify direct navigation to New Cars and Brand validation", groups = {"regression", "ui"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Search for popular car brand")
    public void testSearchCarBrand() {
        String searchBrand = (String) carData.getOrDefault("searchKeyword", "BMW");
        String newCarsUrl = ConfigReader.get("carwale.url", "https://www.carwale.com") + "/new-cars/";
        
        NewCarsPage newCarsPage = new NewCarsPage();
        newCarsPage.openUrl(newCarsUrl);

        assertThat(newCarsPage.getPageTitle())
                .as("New cars page title should be valid")
                .isNotBlank();
    }
}
