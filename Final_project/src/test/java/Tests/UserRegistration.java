package Tests;

import BaseTests.BaseTest;
import POMs.HomePagePOM;
import POMs.RegistrationPOM;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static messages.ErrorMessages.LOGOUT_INVISIBLE;
import static messages.ErrorMessages.LOGOUT_VISIBLE;

public class UserRegistration extends BaseTest {

    @Test
    public void registerUser() {
        //home Page interaction
        HomePagePOM home = new HomePagePOM(driver);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertFalse(home.header.isLogOutVisible(), LOGOUT_INVISIBLE);

        String firstName = RandomStringUtils.randomAlphanumeric(6);
        String lastName = RandomStringUtils.randomAlphanumeric(6);
        String email = RandomStringUtils.randomAlphanumeric(6) + "@mail.ru";
        String password = "SOFTware_345";

        RegistrationPOM registrationPOM = home.header.goToRegistration();
        HomePagePOM homePagePOM = registrationPOM.fillFields(firstName, lastName, email, password);
        softAssert.assertTrue(home.header.isLogOutVisible(), LOGOUT_VISIBLE);

        homePagePOM.header.logOut();
        softAssert.assertFalse(home.header.isLogOutVisible(), LOGOUT_INVISIBLE);

    }
}
