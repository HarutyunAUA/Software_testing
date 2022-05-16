package TestUtills;

import POMs.*;
import dtos.Product;
import org.openqa.selenium.WebDriver;

import java.util.Random;

public class NavigationMethods {

    public static CartPOM addProductAndGoToCart(ProductPagePOM productPage, WebDriver driver, int qty) {
        //Product Page interaction
        Product prod = productPage.getProductDetails();
        String productSize = productPage.clickFirstSize();
        prod.setSize(productSize);
        productPage.setQuantity(qty);
        ConfirmationPopUpPOM popUp = productPage.addToCart();
        popUp.closePopUp();
        return new HomePagePOM(driver).header.goToCart();

    }

    public static ProductPagePOM goToProductPage(String category, String style, WebDriver driver) {
        //home Page interaction
        HomePagePOM home = new HomePagePOM(driver);
        //Search Result Page interaction
        SearchResultPOM result = home.header.headerMenu.openFromMenu(category, style);
        //choosing random item
        int elementNum = new Random().nextInt(result.numOfResults());
        return result.clickElement(elementNum);
    }
}
