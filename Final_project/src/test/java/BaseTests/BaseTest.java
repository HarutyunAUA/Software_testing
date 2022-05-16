package BaseTests;

import com.google.common.io.Files;
import constants.urls.URLs;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;

import static constants.urls.URLs.SELENIUM_BASE_URL;

public class BaseTest {
    public WebDriver driver;

    @BeforeClass
    @Parameters("browser")
    public void init(String browser) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);
        driver = new RemoteWebDriver(new URL(SELENIUM_BASE_URL), capabilities);
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void goHome() {
        driver.get(URLs.BASE_URL);
    }

    @AfterMethod
    public void recordFailures(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            var camera = (TakesScreenshot) driver;
            File screenshot = camera.getScreenshotAs(OutputType.FILE);
            try {
                Files.move(screenshot, new File("src\\test\\resources\\ScreenShots\\"
                        + result.getName() + LocalDate.now().atStartOfDay(ZoneId.of("GMT")).toInstant().toEpochMilli()
                        + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
