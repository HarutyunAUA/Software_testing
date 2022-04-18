package POMs;
import constants.locators.SearchResultsLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static constants.locators.SearchResultsLocator.*;
import static constants.values.WaitValues.DEFAULT_WAIT_VALUE;

public class SearchResultPOM extends BasePOM{

    private By elementName;
    private By categoryUlName=By.xpath(CATEGORY_XPATH);
    private By styleName=By.className(STYLE_CLASS);
    private By resultList=By.xpath(SearchResultsLocator.RESULT_LIST_XPATH);
    public SearchResultPOM(WebDriver driver){
        super(driver);
    }
    public String getUrl(){
        return driver.getCurrentUrl();
    }

    public String getCategory(){
        WebElement category=new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(categoryUlName));
        return category.getText();
    }
    public String getStyle(){
        return driver.findElement(styleName).getText();
    }
    public int numOfResults(){
        WebElement elem=new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(resultList));

        return driver.findElements(resultList).size();
    }
    public ProductPagePOM clickElement(int elementNum){
        driver.findElements(resultList).get(elementNum).click();
        return new ProductPagePOM(driver);
    }
    public String getElementName(int elementNum){
        elementName=By.xpath(BEFORE_PRODUCT_NAME_XPATH+(elementNum+1)+AFTER_PRODUCT_NAME_XPATH);
        return driver.findElement(elementName).getText();

    }
}
