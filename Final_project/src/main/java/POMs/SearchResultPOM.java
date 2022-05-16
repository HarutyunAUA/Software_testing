package POMs;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static constants.locators.SearchResultsLocator.*;
import static constants.values.WaitValues.AVERAGE_WAIT_VALUE;
import static constants.values.WaitValues.SHORT_WAIT_VALUE;

public class SearchResultPOM extends BasePOM {

    private By elementName;
    private final By categoryUlName = By.xpath(CATEGORY_XPATH);
    private final By styleName = By.className(STYLE_CLASS);
    private final By resultList = By.xpath(RESULT_LIST_XPATH);
    private final By priceFilter = By.xpath(PRICE_FILTER);
    private final By materialFilter = By.xpath(MATERIAL_FILTER);
    private final By materialDropDownFilter = By.cssSelector(MATERIAL_DROPDOWN_SECTIONS);
    private final By priceFrom = By.cssSelector(PRICE_FROM);
    private final By priceTo = By.cssSelector(PRICE_TO);
    private final By priceFilterButton = By.cssSelector(PRICE_FILTER_BUTTON);


    public SearchResultPOM(WebDriver driver) {
        super(driver);
    }

    public String getCategory() {
        WebElement category = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(categoryUlName));
        return category.getText();
    }

    public String getStyle() {
        return driver.findElement(styleName).getText();
    }

    public int numOfResults() {
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(AVERAGE_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(resultList));
        return driver.findElements(resultList).size();
    }

    public ProductPagePOM clickElement(int elementNum) {
        driver.findElements(resultList).get(elementNum).click();
        return new ProductPagePOM(driver);
    }

    public String getElementName(int elementNum) {
        elementName = By.xpath(BEFORE_PRODUCT_NAME_XPATH + (elementNum + 1) + AFTER_PRODUCT_NAME_XPATH);
        return driver.findElement(elementName).getText();
    }

    public SearchResultPOM setPriceRange(int startPrice, int endPrice) {
        clickFilter(priceFilter);
        WebElement fromPrice = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(priceFrom));
        fromPrice.clear();
        fromPrice.sendKeys("" + startPrice);
        WebElement toPrice = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(priceTo));
        toPrice.clear();
        toPrice.sendKeys("" + endPrice);
        WebElement goButton = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_VALUE)).until(ExpectedConditions.elementToBeClickable(priceFilterButton));
        goButton.click();
        return new SearchResultPOM(driver);
    }

    public int getPrice(int index) {
        By pricePlace = By.xpath(BEFORE_ELEMENT_PATH + index + AFTER_ELEMENT_PATH);
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_VALUE)).until(ExpectedConditions.presenceOfElementLocated(pricePlace));
        String price = elem.getAttribute(DATA_AMOUNT_TAG);
        if (price != null)
            return Integer.parseInt(price);
        pricePlace = By.xpath(BEFORE_ELEMENT_PATH + index + AFTER_ELEMENT_PATH + FINAL_PRICE_ENDING);
        price = driver.findElement(pricePlace).getAttribute(DATA_AMOUNT_TAG);
        return Integer.parseInt(price);
    }

    public int numOfMaterials() {
        clickFilter(materialFilter);
        WebElement elem = driver.findElement(materialDropDownFilter);
        return elem.findElements(By.tagName(MATERIAL_DROPDOWN_TAG)).size();
    }

    public SearchResultPOM clickMaterialFilter(int index) {
        clickFilter(materialFilter);
        WebElement elem = driver.findElement(materialDropDownFilter);
        elem.findElements(By.tagName(MATERIAL_DROPDOWN_TAG)).get(index).click();
        return new SearchResultPOM(driver);
    }

    public String getMaterialName(int index) {
        clickFilter(materialFilter);
        WebElement elem = driver.findElement(materialDropDownFilter);
        return elem.findElements(By.tagName(MATERIAL_DROPDOWN_TAG)).get(index).getAttribute(DATA_LABEL);
    }

    private void clickFilter(By filterName) {
        boolean isClicked = false;
        WebElement filter = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_VALUE)).until(ExpectedConditions.visibilityOfElementLocated(filterName));
        while (!isClicked) {
            filter.click();
            String value = filter.getAttribute(FILTER_VISIBILITY_TAG);
            isClicked = value != null && value.equals(FILTER_VISIBILITY_TAG_VALUE);
        }
    }

    public boolean isResultPageEmpty() {
        try {
            driver.findElement(By.cssSelector(EMPTY_PAGE_MESSAGE));
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }
}
