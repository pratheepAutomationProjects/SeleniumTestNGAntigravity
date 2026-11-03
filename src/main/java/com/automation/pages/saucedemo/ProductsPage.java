package com.automation.pages.saucedemo;

import com.automation.pages.base.BasePage;
import com.automation.utils.ui.ElementActions;
import org.openqa.selenium.By;

/**
 * Page Object for SauceDemo Products (Inventory) Page.
 */
public class ProductsPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By firstAddToCartBtn = By.xpath("(//button[contains(text(),'Add to cart')])[1]");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By sortDropdown = By.className("product_sort_container");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public String getPageHeading() {
        return ElementActions.getText(pageTitle, "Page Title Header");
    }

    public ProductsPage addFirstProductToCart() {
        ElementActions.click(firstAddToCartBtn, "Add to Cart Button (First Item)");
        return this;
    }

    public int getCartItemCount() {
        if (ElementActions.isDisplayed(cartBadge, "Shopping Cart Badge")) {
            String text = ElementActions.getText(cartBadge, "Cart Badge Count");
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    public ProductsPage sortBy(String visibleOption) {
        ElementActions.selectByVisibleText(sortDropdown, visibleOption, "Sort Dropdown");
        return this;
    }

    public LoginPage logout() {
        ElementActions.click(menuButton, "Burger Menu Button");
        ElementActions.click(logoutLink, "Logout Link");
        return new LoginPage();
    }
}
