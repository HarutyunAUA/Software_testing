package POMs;


import dtos.Product;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static constants.locators.CartLocators.*;
import static constants.values.WaitValues.AVERAGE_WAIT_VALUE;
import static constants.values.WaitValues.LONG_WAIT_VALUE;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class CartPOM extends BasePOM {
    private final By productName = By.className(PRODUCT_NAME_CLASS);
    private final By numOfItems = By.tagName(ITEM_LIST_TAGNAME);
    private By priceItem;
    private By quantiyItem;
    private By sizeItem;
    private By nameItem;
    private By finalPriceItem;
    private final By itemInfo = By.className(ITEM_INFO);
    private final By deleteBttn = By.className(DELETE_BUTTON_CLASS);
    private final By emptyCart = By.className(EMPTY_CART);
    private final By quantityCss = By.cssSelector(QUANTITY_CSS);

    public CartPOM(WebDriver driver) {
        super(driver);
    }

    public int getNumOfItems() {
        return new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).
                until(ExpectedConditions.presenceOfAllElementsLocatedBy(numOfItems)).size();

    }

    public Product getProductDetails(int index) {
        int numOfItems = getNumOfItems();
        if (index > numOfItems || index <= 0)
            return null;
        Product prod = new Product();

        priceItem = By.xpath(BEFORE_INDEX_PRICE_XPATH + index + AFTER_INDEX_PRICE_XPATH);
        WebElement priceElem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(priceItem));
        String priceText = priceElem.getText();
        int price = Integer.parseInt(priceText.substring(0, priceText.indexOf(' ')));
        prod.setPrice(price);

        prod.setCount(getQuantity(index));

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
        WebElement priceElem = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(finalPriceItem));
        String priceText = priceElem.getText();
        return Integer.parseInt(priceText.substring(0, priceText.indexOf(' ')));

    }

    public void deleteFromCart(int index) {
        WebElement item = driver.findElements(itemInfo).get(index);
        WebElement deleteButton = driver.findElement(with(deleteBttn).below(item));
        deleteButton.click();
    }
    public boolean isEmptyCartMessageDisplayed() {
        try {
            driver.findElement(emptyCart).getText()
;
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }

    public int getQuantity(int index) {
        quantiyItem = By.xpath(BEFORE_INDEX_QUANTITY_XPATH + index + AFTER_INDEX_QUANTITY_XPATH);
        WebElement qtyElem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(quantiyItem));
        return Integer.parseInt(qtyElem.getAttribute(ATTRIBUTE_VALUE));
    }

    public void setQuantity(int quantity, int index) {
        nameItem = By.xpath(BEFORE_INDEX_NAME_XPATH + index + AFTER_INDEX_NAME_XPATH);
        WebElement name = driver.findElement(nameItem);
        WebElement changeQuantity = driver.findElement(with(quantityCss).toRightOf(name));
        changeQuantity.sendKeys(Keys.DELETE);
        changeQuantity.sendKeys("" + quantity + Keys.ENTER);

    }
}
