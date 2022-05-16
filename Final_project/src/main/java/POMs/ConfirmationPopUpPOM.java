package POMs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static constants.locators.PopUpLocators.CLOSE_POP_UP_CLASS;
import static constants.locators.PopUpLocators.MESSAGE_CLASS;
import static constants.values.WaitValues.AVERAGE_WAIT_VALUE;


public class ConfirmationPopUpPOM extends BasePOM {
    private By closePopUpButton = By.className(CLOSE_POP_UP_CLASS);
    private By message = By.className(MESSAGE_CLASS);

    public ConfirmationPopUpPOM(WebDriver driver) {
        super(driver);
    }

    public void closePopUp() {
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.elementToBeClickable(closePopUpButton));
        elem.click();
    }

    public String getMessage() {
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(message));
        return elem.getText();
    }
}
