package org.example.suites;

import org.example.base.BaseTest;
import org.example.dataproviders.AppDataProvider;
import org.example.pages.SignupPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SignupFailedTest extends BaseTest {

    /**
     * TC_SIGNUP_004_Username_Too_Short
     * Calqué sur SignupFailed.robot → ligne 2 du CSV (abc)
     */
    @Test(
        description = "TC_SIGNUP_004 - Username trop court (< 5 caractères)",
        dataProvider = "signupUsernameTooShortData",
        dataProviderClass = AppDataProvider.class
    )
    public void TC_SIGNUP_004_Username_Too_Short(
            String username,
            String email,
            String password,
            String confirmPassword
    ) {
        SignupPage signupPage = new SignupPage(driver);

        signupPage.navigateToRegisterPage();
        signupPage.fillUsername(username);
        signupPage.fillEmail(email);
        signupPage.fillPassword(password);
        signupPage.fillConfirmPassword(confirmPassword);
        signupPage.agreeTerms();

        // Le site affiche le message de validation username trop court
        Assert.assertTrue(
            signupPage.isUsernameTooShortValidationVisible(),
            "La validation 'Use 5 character or longer' n'est pas visible"
        );
        // Note : le site ne desactive pas forcement le bouton, on verifie uniquement le message
    }

}
