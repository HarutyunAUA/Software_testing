package POMs;
import dtos.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;

import static constants.locators.ProductPageLocators.*;
import static constants.values.WaitValues.DEFAULT_WAIT_VALUE;

public class ProductPagePOM extends BasePOM{
    private By productName=By.className(PRODUCT_NAME_CLASS);
    private By price= By.cssSelector(PRICE_AMOUNT_CLASS);
    private By color=By.xpath(COLOR_XPATH);
    private By firstSize=By.className(FIRST_SIZE_DATA_CLASS);
    private By addToCartButton=By.id(ADD_TO_CART_BUTTON_ID);
    private By sizeSection=By.xpath(SIZE_SECTION_XPATH);
    private By count=By.name(COUNT_NAME);

    public ProductPagePOM(WebDriver driver) {
        super(driver);
    }

    public Product getProductDetails(){
        WebElement titleElem=new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(productName));
        String itemName= titleElem.getText();

        WebElement priceElem=new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(price));
        int itemPrice=Integer.parseInt(priceElem.getDomAttribute(PRICE_AMOUNT_ATTRIBUTE));

        WebElement colorItemElem=new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(color));
        String itemColor=colorItemElem.getText();

        WebElement qtyItemElem=new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(count));
        int qty=Integer.parseInt(qtyItemElem.getDomAttribute(ATTRIBUTE_VALUE));

        return new Product(itemPrice, qty, itemColor, itemName);
    }
    public boolean hasSizeSection(){
        try {
            WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(sizeSection));
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public String clickFirstSize(){
           if(driver.findElement(sizeSection).isDisplayed()){
                WebElement element = driver.findElements(firstSize)
                        .stream()
                        .filter(webElement -> !webElement.getAttribute("class").endsWith("disabled"))
                        .findFirst().orElse(null);
                if (element == null)
                    return null;
                String size = element.getText();
                element.click();
                return size;
        }
            return null;


    }

    public PopUpPOM addToCart(){
        WebElement elem=new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(addToCartButton));
        elem.click();
        return new PopUpPOM(driver);
    }

    public void setQuantity(int quantity){
        driver.findElement(count).sendKeys(Keys.DELETE);
        driver.findElement(count).sendKeys(""+quantity);

    }

}
