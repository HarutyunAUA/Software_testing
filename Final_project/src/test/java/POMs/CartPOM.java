package POMs;


import dtos.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static constants.locators.CartLocators.*;
import static constants.values.WaitValues.DEFAULT_WAIT_VALUE;

public class CartPOM extends BasePOM {
    private By productName = By.className(PRODUCT_NAME_CLASS);
    private By numOfItems = By.tagName(ITEM_LIST_TAGNAME);
    private By priceItem;
    private By quantiyItem;
    private By sizeItem;
    private By nameItem;
    private By finalPriceItem;

    public CartPOM(WebDriver driver) {
        super(driver);
    }

    public int getNumOfItems() {
        return driver.findElements(numOfItems).size();
    }

    public Product getProductDetails(int index) {
        if (index > getNumOfItems() || index <= 0)
            return null;
        Product prod = new Product();

        priceItem = By.xpath(BEFORE_INDEX_PRICE_XPATH + index + AFTER_INDEX_PRICE_XPATH);
        WebElement priceElem = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(priceItem));
        String priceText = priceElem.getText();
        int price = Integer.parseInt(priceText.substring(0, priceText.indexOf(' ')));
        prod.setPrice(price);

        quantiyItem = By.xpath(BEFORE_INDEX_QUANTITY_XPATH + index + AFTER_INDEX_QUANTITY_XPATH);
        WebElement qtyElem = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(quantiyItem));
        int count = Integer.parseInt(qtyElem.getAttribute(ATTRIBUTE_VALUE));
        prod.setCount(count);

        sizeItem = By.xpath(BEFORE_INDEX_SIZE_XPATH + index + AFTER_INDEX_SIZE_XPATH);
        if (driver.findElement(sizeItem).isDisplayed()) {
            String size = driver.findElement(sizeItem).getText();
            prod.setSize(size);
        }

        nameItem = By.xpath(BEFORE_INDEX_NAME_XPATH + index + AFTER_INDEX_NAME_XPATH);
        String name = driver.findElement(nameItem).getText();
        prod.setName(name);

        return prod;

    }

    public int getFinalPrice(int index) {
        if (index > getNumOfItems() || index <= 0)
            return 0;
        finalPriceItem = By.xpath(BEFORE_INDEX_FINAL_PRICE_XPATH + index + AFTER_INDEX_FINAL_PRICE_XPATH);
        WebElement priceElem = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE + 20)).until(ExpectedConditions.presenceOfElementLocated(finalPriceItem));
        String priceText = priceElem.getText();
        return Integer.parseInt(priceText.substring(0, priceText.indexOf(' ')));

    }
}
