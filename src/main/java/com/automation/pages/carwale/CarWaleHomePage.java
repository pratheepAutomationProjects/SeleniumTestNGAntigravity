package com.automation.pages.carwale;

import com.automation.pages.base.BasePage;
import com.automation.utils.ui.ElementActions;
import org.openqa.selenium.By;

/**
 * Page Object for CarWale Home Page (CarWale UI Automation).
 */
public class CarWaleHomePage extends BasePage {

    private final By newCarsMenu = By.xpath("//div[normalize-space()='NEW CARS'] | //div[contains(text(),'NEW CARS')] | //nav//span[contains(text(),'New')]");
    private final By findNewCarsLink = By.xpath("//div[normalize-space()='Find New Cars'] | //a[contains(@href,'new-cars')]");
    private final By searchInput = By.xpath("//input[@placeholder='Search'] | //input[contains(@placeholder,'Search')]");

    public CarWaleHomePage hoverOnNewCarsMenu() {
        ElementActions.hover(newCarsMenu, "NEW CARS Menu Header");
        return this;
    }

    public NewCarsPage clickFindNewCars() {
        ElementActions.click(findNewCarsLink, "Find New Cars Link");
        return new NewCarsPage();
    }

    public void searchForCar(String carBrandOrModel) {
        ElementActions.type(searchInput, carBrandOrModel, "Search Box");
    }
}
