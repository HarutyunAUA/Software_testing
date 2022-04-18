package POMs;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static constants.locators.HeaderLocators.*;
import static constants.values.WaitValues.DEFAULT_WAIT_VALUE;


public class HeaderPOM {


    private By cartCount=By.className(CART_COUNT_CLASS);
    private By cartButton=By.className(CART_BUTTON_CLASS);
    private By goToCartButton=By.xpath(GO_TO_CART_XPATH);
    private WebDriver driver;
    public HeaderMenuPOM headerMenu;
    public HeaderPOM(WebDriver driver){
        this.driver=driver;
        headerMenu=new HeaderMenuPOM(driver);
    }

    public int getCartCount(){
        try {
            WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(cartCount));
            if (elem.getText().equals(""))
                return 0;
            return Integer.parseInt(elem.getText());
        }catch(TimeoutException e){
            return 0;
        }
    }

    public CartPOM goToCart(){
        WebElement cartIcon= driver.findElement(cartButton);
        Actions actions = new Actions(driver);
        actions.moveToElement(cartIcon).perform();
        WebElement elem=new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(goToCartButton));
        elem.click();
        return new CartPOM(driver);
    }
}
