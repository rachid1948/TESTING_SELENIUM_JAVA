package org.example.suites;

import org.example.base.BaseTest;
import org.example.dataproviders.AppDataProvider;
import org.example.pages.SignupPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SignupPasswordMismatchTest extends BaseTest {

    /**
     * TC_SIGNUP_005_Password_Mismatch
     * Vérifie que le site affiche une erreur lorsque les mots de passe ne correspondent pas.
     * Données : ligne 3 du CSV (RachMismatch)
     */
    @Test(
        description = "TC_SIGNUP_005 - Mots de passe non identiques",
        dataProvider = "signupPasswordMismatchData",
        dataProviderClass = AppDataProvider.class
    )
    public void TC_SIGNUP_005_Password_Mismatch(
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
        // Ne pas soumettre : le bouton Register est désactivé quand les passwords ne correspondent pas
        // La validation est déjà visible après fillConfirmPassword + agreeTerms

        Assert.assertTrue(
            signupPage.hasPasswordMismatchValidation(),
            "La validation password mismatch n'est pas détectée. Erreur : "
                + signupPage.getRegistrationErrorText()
        );
    }
}

