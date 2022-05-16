package POMs;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static constants.values.WaitValues.AVERAGE_WAIT_VALUE;


public class HeaderMenuPOM {
    private WebDriver driver;


    public HeaderMenuPOM(WebDriver driver) {
        this.driver = driver;
    }

    public boolean canHover(String category) {
        try {
            WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(category)));
            Actions actions = new Actions(driver);
            actions.moveToElement(elem).perform();
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }

    public SearchResultPOM openFromMenu(String category, String menuItem) {
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(category)));
        Actions actions = new Actions(driver);
        actions.moveToElement(elem).perform();
        WebElement style = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(menuItem)));
        style.click();
        return new SearchResultPOM(driver);
    }

    public SearchResultPOM openFromMenu(String category) {
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(category)));
        elem.click();
        return new SearchResultPOM(driver);
    }
}
