package POMs;

import org.openqa.selenium.WebDriver;

public class BasePOM {
    public WebDriver driver;
    public HeaderPOM header;

    public BasePOM(WebDriver driver) {
        this.driver = driver;
        header = new HeaderPOM(driver);
    }
}
