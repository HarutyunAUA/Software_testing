package Tests;

import BaseTests.BaseTest;
import POMs.*;
import dataproviders.DataProvider;
import dtos.Product;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

import static messages.ErrorMessages.*;

public class CartInteractionTests extends BaseTest {
    @org.testng.annotations.DataProvider
    public Object[][] menuItems(){
        return DataProvider.MENU_ITEMS;
    }

    @Test(dataProvider ="menuItems")
    public void goingToCartAfterAdding(String category, String style) {
        //home Page interaction
        HomePagePOM home = new HomePagePOM(driver);
        SoftAssert softAssert = new SoftAssert();

        //Search Result Page interaction
        SearchResultPOM result = home.header.headerMenu.openFromMenu(category,
                style);
        //choosing random item
        int elementNum = new Random().nextInt(result.numOfResults());
        String elementName = result.getElementName(elementNum);
        ProductPagePOM productPage = result.clickElement(elementNum);

        //Product Page interaction
        Product prod = productPage.getProductDetails();
        String productSize = productPage.clickFirstSize();
        prod.setSize(productSize);
        int initialCartCount = productPage.header.getCartCount();
        PopUpPOM popUp = productPage.addToCart();
        popUp.closePopUp();

        //Cart Page interaction
        CartPOM cartPage=home.header.goToCart();
        softAssert.assertEquals(cartPage.getNumOfItems(),initialCartCount+1, CART_COUNT_NOT_UPDATED);
        Product productDetails = cartPage.getProductDetails(initialCartCount+1);

        softAssert.assertEquals(productDetails.getName(), prod.getName(), PRODUCT_NAME_MISMATCH);
        softAssert.assertEquals(productDetails.getCount(), prod.getCount(), PRODUCT_QUANTITY_MISMATCH);
        softAssert.assertEquals(productDetails.getSize(), prod.getSize(), PRODUCT_SIZE_MISMATCH);
        softAssert.assertEquals(productDetails.getPrice(), prod.getPrice(), PRODUCT_PRICE_MISMATCH);
        softAssert.assertAll();

    }
    @Test(dataProvider ="menuItems")
    public void addDifferentQuantities(String category, String style) {
        //home Page interaction
        HomePagePOM home = new HomePagePOM(driver);
        int initialCartCount = home.header.getCartCount();
        SoftAssert softAssert = new SoftAssert();

        //Search Result Page interaction
        SearchResultPOM result = home.header.headerMenu.openFromMenu(category,
                style);
        //choosing random item
        int elementNum = new Random().nextInt(result.numOfResults());
        String elementName = result.getElementName(elementNum);
        ProductPagePOM productPage = result.clickElement(elementNum);

        //Product Page interaction
        Product prod = productPage.getProductDetails();
        String productSize = productPage.clickFirstSize();
        prod.setSize(productSize);
        int qty=3;
        productPage.setQuantity(qty);
        PopUpPOM popUp = productPage.addToCart();
        popUp.closePopUp();

        //Cart Page interaction
        CartPOM cartPage=home.header.goToCart();
        Product productDetails = cartPage.getProductDetails(initialCartCount+1);
        softAssert.assertEquals(productDetails.getCount(), qty, PRODUCT_QUANTITY_MISMATCH);
        softAssert.assertEquals(cartPage.getFinalPrice(initialCartCount+1), qty*productDetails.getPrice(), PRODUCT_PRICE_MISMATCH);

        softAssert.assertAll();

    }
}
