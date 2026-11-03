package com.automation.pages.carwale;

import com.automation.pages.base.BasePage;
import com.automation.utils.ui.ElementActions;
import org.openqa.selenium.By;

/**
 * Page Object for CarWale New Cars Listing Page.
 */
public class NewCarsPage extends BasePage {

    private final By bmwBrandLink = By.xpath("//div[normalize-space()='BMW'] | //a[contains(@title,'BMW')]");
    private final By toyotaBrandLink = By.xpath("//div[normalize-space()='Toyota'] | //a[contains(@title,'Toyota')]");
    private final By hyundaiBrandLink = By.xpath("//div[normalize-space()='Hyundai'] | //a[contains(@title,'Hyundai')]");
    private final By pageHeading = By.xpath("//h1");

    public void selectBrand(String brand) {
        By brandLocator = By.xpath("//div[normalize-space()='" + brand + "'] | //a[contains(@title,'" + brand + "')] | //span[text()='" + brand + "']");
        ElementActions.click(brandLocator, brand + " Brand Card");
    }

    public String getHeadingText() {
        return ElementActions.getText(pageHeading, "New Cars Page Heading");
    }
}
