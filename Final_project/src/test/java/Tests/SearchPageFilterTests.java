package Tests;

import BaseTests.BaseTest;
import POMs.HomePagePOM;
import POMs.ProductPagePOM;
import POMs.SearchResultPOM;
import dataproviders.TestDataProvider;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

import static messages.ErrorMessages.PRICE_FILTERING;

public class SearchPageFilterTests extends BaseTest {
    @DataProvider
    public Object[][] menuItems() {
        return TestDataProvider.MENU_ITEMS;
    }

    @DataProvider
    public Object[][] filterOptions() {
        return TestDataProvider.FILTER_OPTIONS;
    }

    @Test(dataProvider = "menuItems")
    public void priceFilters(String category, String style) {
        HomePagePOM home = new HomePagePOM(driver);
        SoftAssert softAssert = new SoftAssert();
        SearchResultPOM result = home.header.headerMenu.openFromMenu(category);
        int startPrice = 10000;
        int endPrice = 12000;
        SearchResultPOM filteredpage = result.setPriceRange(startPrice, endPrice);
        int numOfResults = filteredpage.numOfResults();
        for (int i = 1; i <= numOfResults; i++) {
            int price = filteredpage.getPrice(i);
            boolean filterCondition = price >= startPrice && price <= endPrice;
            softAssert.assertTrue(filterCondition, PRICE_FILTERING);
        }
        softAssert.assertAll();
    }

    @Test(dataProvider = "filterOptions")
    public void materialFilters(String category, String filterName) {
        HomePagePOM home = new HomePagePOM(driver);
        SoftAssert softAssert = new SoftAssert();
        boolean filterResult = true;
        SearchResultPOM filteredPage = null;
        String materialName = "";

        //finds the first non-empty result page
        while (filterResult) {
            SearchResultPOM result = home.header.headerMenu.openFromMenu(category);
            int numOfMaterials = result.numOfMaterials();
            int materialIndex = new Random().nextInt(numOfMaterials);
            materialName = result.getMaterialName(materialIndex);
            filteredPage = result.clickMaterialFilter(materialIndex);
            filterResult = filteredPage.isResultPageEmpty();

        }
        int numOfResults = filteredPage.numOfResults();
        int index = new Random().nextInt(numOfResults);
        ProductPagePOM product = filteredPage.clickElement(index);
        softAssert.assertEquals(product.getMaterialValue(filterName), materialName);
        softAssert.assertAll();
    }
}
