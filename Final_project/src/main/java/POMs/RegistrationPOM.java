package POMs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static constants.locators.RegistrationPageLocators.*;

public class RegistrationPOM extends BasePOM {
    private final By firstName = By.id(FIRST_NAME_ID);
    private final By lastName = By.id(LAST_NAME_ID);
    private final By email = By.id(EMAIL_ID);
    private final By password = By.id(PASSWORD_ID);
    private final By password_confirmation = By.name(PASSWORD_CONFIRMATION_NAME);
    private final By termsCheckBox = By.id(TERMS_ACCEPT_ID);
    private final By registerButton = By.id(REGISTER_BUTTON);

    public RegistrationPOM(WebDriver driver) {
        super(driver);
    }

    public HomePagePOM fillFields(String firstName, String lastName, String email, String password) {
        driver.findElement(this.firstName).sendKeys(firstName);
        driver.findElement(this.lastName).sendKeys(lastName);
        driver.findElement(this.email).sendKeys(email);
        driver.findElement(this.password).sendKeys(password);
        driver.findElement(this.password_confirmation).sendKeys(password);
        driver.findElement(this.termsCheckBox).click();
        driver.findElement(this.registerButton).click();

        return new HomePagePOM(driver);
    }
}

