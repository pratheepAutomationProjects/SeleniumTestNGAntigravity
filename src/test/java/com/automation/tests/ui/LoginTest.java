package com.automation.tests.ui;

import com.automation.constants.FrameworkConstants;
import com.automation.pages.saucedemo.LoginPage;
import com.automation.pages.saucedemo.ProductsPage;
import com.automation.tests.base.BaseTest;
import com.automation.utils.data.JsonUtils;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("UI Automation")
@Feature("Authentication")
public class LoginTest extends BaseTest {

    private LoginPage loginPage;
    private Map<String, Object> testData;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        loginPage = new LoginPage();
        testData = JsonUtils.readJsonAsMap(FrameworkConstants.LOGIN_DATA_JSON);
    }

    @Test(description = "Verify successful login with valid credentials", groups = {"smoke", "regression", "ui"})
    @Severity(SeverityLevel.BLOCKER)
    @Story("User should be able to log in with valid credentials")
    public void testValidLogin() {
        @SuppressWarnings("unchecked")
        Map<String, String> validUser = (Map<String, String>) testData.get("validUser");

        ProductsPage productsPage = loginPage.loginAsValidUser(validUser.get("username"), validUser.get("password"));
        
        assertThat(productsPage.getPageHeading())
                .as("Products page header should be 'Products'")
                .isEqualToIgnoringCase("Products");
    }

    @Test(description = "Verify error message when user is locked out", groups = {"regression", "ui"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Locked out user should receive descriptive error message")
    public void testLockedOutUser() {
        @SuppressWarnings("unchecked")
        Map<String, String> lockedUser = (Map<String, String>) testData.get("lockedOutUser");

        loginPage.enterUsername(lockedUser.get("username"))
                 .enterPassword(lockedUser.get("password"))
                 .clickLogin();

        assertThat(loginPage.getErrorMessage())
                .as("Error message should match expected text for locked out user")
                .contains(lockedUser.get("expectedError"));
    }

    @Test(description = "Verify error message with invalid credentials", groups = {"regression", "ui"})
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid credentials should trigger error notice")
    public void testInvalidCredentials() {
        @SuppressWarnings("unchecked")
        Map<String, String> invalidUser = (Map<String, String>) testData.get("invalidUser");

        loginPage.enterUsername(invalidUser.get("username"))
                 .enterPassword(invalidUser.get("password"))
                 .clickLogin();

        assertThat(loginPage.getErrorMessage())
                .as("Error banner should indicate credentials do not match")
                .contains(invalidUser.get("expectedError"));
    }
}
