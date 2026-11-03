package com.automation.pages.saucedemo;

import com.automation.pages.base.BasePage;
import com.automation.utils.ui.ElementActions;
import org.openqa.selenium.By;

/**
 * Page Object for SauceDemo Login Page.
 */
public class LoginPage extends BasePage {

    // Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage enterUsername(String username) {
        ElementActions.type(usernameInput, username, "Username Input");
        return this;
    }

    public LoginPage enterPassword(String password) {
        ElementActions.type(passwordInput, password, "Password Input");
        return this;
    }

    public void clickLogin() {
        ElementActions.click(loginButton, "Login Button");
    }

    public ProductsPage loginAsValidUser(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new ProductsPage();
    }

    public String getErrorMessage() {
        return ElementActions.getText(errorMessage, "Error Message Banner");
    }

    public boolean isLoginButtonDisplayed() {
        return ElementActions.isDisplayed(loginButton, "Login Button");
    }
}
