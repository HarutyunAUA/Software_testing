package Tests;

import BaseTests.BaseTest;
import POMs.CartPOM;
import POMs.ConfirmationPopUpPOM;
import POMs.HomePagePOM;
import POMs.ProductPagePOM;
import dataproviders.TestDataProvider;
import dtos.Product;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

import static TestUtills.NavigationMethods.addProductAndGoToCart;
import static TestUtills.NavigationMethods.goToProductPage;
import static messages.ErrorMessages.*;

public class CartInteractionTests extends BaseTest {
    @DataProvider
    public Object[][] menuItems() {
        return TestDataProvider.MENU_ITEMS;
    }

    @Test(dataProvider = "menuItems")
    public void goingToCartAfterAdding(String category, String style) {
        //home Page interaction
        HomePagePOM home = new HomePagePOM(driver);
        SoftAssert softAssert = new SoftAssert();
        ProductPagePOM productPage = goToProductPage(category, style, driver);

        //Product Page interaction
        Product prod = productPage.getProductDetails();
        String productSize = productPage.clickFirstSize();
        prod.setSize(productSize);
        int initialCartCount = productPage.header.getCartCount();
        ConfirmationPopUpPOM popUp = productPage.addToCart();
        popUp.closePopUp();

        //Cart Page interaction
        CartPOM cartPage = home.header.goToCart();
        softAssert.assertEquals(cartPage.getNumOfItems(), initialCartCount + 1, CART_COUNT_NOT_UPDATED);
        Product productDetails = cartPage.getProductDetails(initialCartCount + 1);

        softAssert.assertEquals(productDetails.getName(), prod.getName(), PRODUCT_NAME_MISMATCH);
        softAssert.assertEquals(productDetails.getCount(), prod.getCount(), PRODUCT_QUANTITY_MISMATCH);
        softAssert.assertEquals(productDetails.getSize(), prod.getSize(), PRODUCT_SIZE_MISMATCH);
        softAssert.assertEquals(productDetails.getPrice(), prod.getPrice(), PRODUCT_PRICE_MISMATCH);
        softAssert.assertAll();

    }

    @Test(dataProvider = "menuItems")
    public void addDifferentQuantities(String category, String style) {
        //home Page interaction
        HomePagePOM home = new HomePagePOM(driver);
        int initialCartCount = home.header.getCartCount();
        SoftAssert softAssert = new SoftAssert();

        ProductPagePOM productPage = goToProductPage(category, style, driver);
        int qty = 3;
        CartPOM cartPage = addProductAndGoToCart(productPage, driver, qty);
        //Cart Page interaction
        Product productDetails = cartPage.getProductDetails(initialCartCount + 1);
        softAssert.assertEquals(productDetails.getCount(), qty, PRODUCT_QUANTITY_MISMATCH);
        softAssert.assertEquals(cartPage.getFinalPrice(initialCartCount + 1), qty * productDetails.getPrice(), PRODUCT_PRICE_MISMATCH);
        softAssert.assertAll();
    }

    @Test(dataProvider = "menuItems")
    public void deleteFromCart(String category, String style) {
        SoftAssert softAssert = new SoftAssert();
        ProductPagePOM productPage = goToProductPage(category, style, driver);
        CartPOM cartPage = addProductAndGoToCart(productPage, driver, 1);
        int initialCount = cartPage.getNumOfItems();
        int deleteIndex = new Random().nextInt(initialCount);
        cartPage.deleteFromCart(deleteIndex);
        softAssert.assertEquals(initialCount - 1, cartPage.getNumOfItems(), CART_ELEMENT_DELETION);
        if (initialCount == 1)
            softAssert.assertTrue(cartPage.isEmptyCartMessageDisplayed(), CART_EMPTY_CHECK);
        softAssert.assertAll();
    }

    @Test(dataProvider = "menuItems")
    public void changeCartQuantity(String category, String style) {
        SoftAssert softAssert = new SoftAssert();
        ProductPagePOM productPage = goToProductPage(category, style, driver);
        CartPOM cartPage = addProductAndGoToCart(productPage, driver, 1);
        int initialCount = cartPage.getNumOfItems();
        int changeItem = new Random().nextInt(initialCount) + 1;
        int qty = 2;
        cartPage.setQuantity(qty, changeItem);
        int changedQuanity = cartPage.getQuantity(changeItem);
        softAssert.assertEquals(changedQuanity, qty, CART_QUANTITY_CHANGE);
        softAssert.assertAll();
        ;
    }

}
