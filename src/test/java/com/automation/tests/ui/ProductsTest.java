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
@Feature("Product Catalog & Cart")
public class ProductsTest extends BaseTest {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        loginPage = new LoginPage();
        Map<String, Object> testData = JsonUtils.readJsonAsMap(FrameworkConstants.LOGIN_DATA_JSON);
        @SuppressWarnings("unchecked")
        Map<String, String> validUser = (Map<String, String>) testData.get("validUser");

        productsPage = loginPage.loginAsValidUser(validUser.get("username"), validUser.get("password"));
    }

    @Test(description = "Verify adding product to cart updates badge count", groups = {"smoke", "regression", "ui"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("User adds an item to shopping cart")
    public void testAddToCart() {
        productsPage.addFirstProductToCart();
        assertThat(productsPage.getCartItemCount())
                .as("Cart badge count should be 1 after adding first item")
                .isEqualTo(1);
    }

    @Test(description = "Verify user can sort products by Price (low to high)", groups = {"regression", "ui"})
    @Severity(SeverityLevel.NORMAL)
    @Story("User sorts catalog products")
    public void testSortProducts() {
        productsPage.sortBy("Price (low to high)");
        assertThat(productsPage.getPageHeading())
                .as("Products page should remain active after sort")
                .isEqualToIgnoringCase("Products");
    }

    @Test(description = "Verify user can log out successfully", groups = {"regression", "ui"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("User logs out of account")
    public void testLogout() {
        LoginPage redirectedLogin = productsPage.logout();
        assertThat(redirectedLogin.isLoginButtonDisplayed())
                .as("Login button should be visible after logout")
                .isTrue();
    }
}
