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
import static constants.values.WaitValues.AVERAGE_WAIT_VALUE;
import static constants.values.WaitValues.SHORT_WAIT_VALUE;
import static org.openqa.selenium.support.locators.RelativeLocator.with;


public class HeaderPOM {


    private final By cartCount = By.className(CART_COUNT_CLASS);
    private final By cartButton = By.className(CART_BUTTON_CLASS);
    private final By goToCartButton = By.xpath(GO_TO_CART_XPATH);
    private final By personalMenuButton = By.className(PERSONAL_PAGE_CLASS);
    private final By registerButton = By.className(REGISTER_CLASS);
    private final By searchIcon = By.className(SEARCH_ICON);
    private final By logOutBttn = By.className(LOGOUT_CLASS);

    private final WebDriver driver;
    public HeaderMenuPOM headerMenu;

    public HeaderPOM(WebDriver driver) {
        this.driver = driver;
        headerMenu = new HeaderMenuPOM(driver);
    }

    public int getCartCount() {
        try {
            Thread.sleep(SHORT_WAIT_VALUE * 100);
            WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(cartCount));
            if (elem.getText().equals(""))
                return 0;
            return Integer.parseInt(elem.getText());
        } catch (TimeoutException | InterruptedException e) {
            return 0;
        }
    }

    public CartPOM goToCart() {
        WebElement cartIcon = driver.findElement(cartButton);
        Actions actions = new Actions(driver);
        actions.moveToElement(cartIcon).perform();
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(goToCartButton));
        elem.click();
        return new CartPOM(driver);
    }

    public RegistrationPOM goToRegistration() {
        WebElement personalMenu = driver.findElement(with(personalMenuButton).toRightOf(searchIcon));
        Actions actions = new Actions(driver);
        actions.moveToElement(personalMenu).perform();
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(registerButton));
        elem.click();
        return new RegistrationPOM(driver);
    }

    public boolean isLogOutVisible() {
        WebElement personalMenu = driver.findElement(with(personalMenuButton).toRightOf(searchIcon));
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(personalMenu).perform();
            WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(logOutBttn));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public HomePagePOM logOut() {
        WebElement personalMenu = driver.findElement(with(personalMenuButton).toRightOf(searchIcon));
        Actions actions = new Actions(driver);
        actions.moveToElement(personalMenu).perform();
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(logOutBttn));
        elem.click();
        return new HomePagePOM(driver);
    }
}
