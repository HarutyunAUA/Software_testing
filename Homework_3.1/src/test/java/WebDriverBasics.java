import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class WebDriverBasics {
    public static WebDriver driver;
    private static String url;

    @BeforeClass
    public static void init(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions().setBinary("C:\\Program Files (x86)\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");
        driver=new ChromeDriver(options);
        url="https://yandex.com.am/weather/";
    }
    //Using tagName and id
    @Test
    public static void searchBar(){
        SoftAssert softAssert=new SoftAssert();
        driver.get(url);
        driver.findElement(By.tagName("input")).sendKeys("Abovyan, Kotayk"+ Keys.ENTER);
        String actualText=driver.findElement(By.id("main_title")).getText();
        softAssert.assertEquals(actualText, "Weather in Abovyan", "The message should contain the name of the place");
        softAssert.assertAll();
    }
    //Using cssSelector
    @Test
    public static void otherCities(){
        SoftAssert softAssert=new SoftAssert();
        driver.get(url);
        String actualText= driver.findElement(By.cssSelector("#other_cities_title")).getText();
        softAssert.assertEquals(actualText, "Weather in other cities", "The message should contain the name of the place");
        driver.findElement(By.cssSelector("#content_bottom > div.other-cities.card.content__elem.content__elem_pos_2 > div.other-cities__links > a")).click();
        softAssert.assertEquals(driver.getCurrentUrl(), "https://yandex.com.am/weather/region?via=moc", "The button WorldWide should forward to the World weather");

        softAssert.assertAll();

    }
    //Using class
    @Test
    public static void refresh(){
        SoftAssert softAssert=new SoftAssert();
        driver.get(url);
        String original="Abovyan, Kotayk";
        driver.findElement(By.className("mini-suggest__input")).sendKeys(original+ Keys.ENTER);
        String actualText=driver.findElement(By.id("main_title")).getText();
        softAssert.assertEquals(actualText, "Weather in Abovyan", "The message should contain the name of the place");

        driver.navigate().back();
        String backText=driver.findElement(By.className("mini-suggest__input")).getAttribute("value");
        softAssert.assertEquals(backText, original, "The input should be the same after returning");

        driver.navigate().refresh();
        String refreshText=driver.findElement(By.className("mini-suggest__input")).getAttribute("value");
        softAssert.assertEquals(refreshText, "", "The input field should be the blank after refreshing");


        softAssert.assertAll();

    }
    //Using linkText
    @Test
    public static void header(){
        SoftAssert softAssert=new SoftAssert();
        driver.get(url);
        driver.findElement(By.linkText("Armenia")).click();
        String title=driver.getTitle();
        softAssert.assertTrue(title.contains("Weather in Armenia"), "The button Armenia should forward to the page with the title Armenia");
        softAssert.assertAll();

    }
    //Using Xpath
    @Test
    public static void forecastMessage(){
        SoftAssert softAssert=new SoftAssert();
        driver.get(url);
        String weekText=driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[1]/h2")).getText();
        softAssert.assertTrue(weekText.contains("10 day"), "This section should contain the 10 day's forecast");
        softAssert.assertAll();
    }
    @AfterClass
    public static void close(){
        driver.quit();
    }
}
