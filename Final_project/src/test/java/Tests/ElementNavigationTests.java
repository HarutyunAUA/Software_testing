package Tests;

import BaseTests.BaseTest;
import POMs.ConfirmationPopUpPOM;
import POMs.HomePagePOM;
import POMs.ProductPagePOM;
import POMs.SearchResultPOM;
import dataproviders.TestDataProvider;
import dtos.Product;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

import static messages.ErrorMessages.*;

public class ElementNavigationTests extends BaseTest {

    @DataProvider
    public Object[][] menuItems() {
        return TestDataProvider.MENU_ITEMS;
    }

    @Test(dataProvider = "menuItems")
    public void checkingParameters(String category, String style) {

        HomePagePOM home = new HomePagePOM(driver);
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(home.header.headerMenu.canHover(category), USER_CANNOT_HOVER);

        SearchResultPOM result = home.header.headerMenu.openFromMenu(category, style);
        softAssert.assertEquals(result.getCategory(), category, CATEGORIES_DO_NOT_MATCH);
        softAssert.assertEquals(result.getStyle(), style, STYLES_DO_NOT_MATCH);
        //choosing random item
        int elementNum = new Random().nextInt(result.numOfResults());
        String elementName = result.getElementName(elementNum);
        ProductPagePOM productPage = result.clickElement(elementNum);
        Product prod = productPage.getProductDetails();
        softAssert.assertEquals(prod.getName(), elementName, ITEM_DOES_NOT_MATCH);
    }

    @Test(dataProvider = "menuItems")
    public void addingToCart(String category, String style) throws InterruptedException {
        HomePagePOM home = new HomePagePOM(driver);
        SoftAssert softAssert = new SoftAssert();
        SearchResultPOM result = home.header.headerMenu.openFromMenu(category, style);

        //choosing random item
        int elementNum = new Random().nextInt(result.numOfResults());
        String elementName = result.getElementName(elementNum);

        ProductPagePOM productPage = result.clickElement(elementNum);
        Product prod = productPage.getProductDetails();
        String productSize = productPage.clickFirstSize();
        prod.setSize(productSize);
        int initialValue = productPage.header.getCartCount();

        ConfirmationPopUpPOM popUp = productPage.addToCart();
        softAssert.assertTrue(popUp.getMessage().contains(elementName), POPUP_ITEM_DOES_NOT_MATCH);
        popUp.closePopUp();
        softAssert.assertEquals(home.header.getCartCount(), initialValue + 1, CART_COUNT_NOT_UPDATED);
        softAssert.assertAll();

    }

}
