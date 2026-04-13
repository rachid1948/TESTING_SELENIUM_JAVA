package org.example.suites;

import org.example.base.BaseTest;
import org.example.dataproviders.AppDataProvider;
import org.example.pages.SignupPage;
import org.example.utils.CSVUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SignupSuccessTest extends BaseTest {

    /**
     * Les donnees viennent du CSV.
     * Seul le username est adapte en incrementant son nombre final (ex: rachid23 -> rachid24).
     */
    @Test(
        description = "TC_SIGNUP_001 - Inscription avec donnees valides",
        dataProvider = "signupSuccessData",
        dataProviderClass = AppDataProvider.class
    )
    public void TC_SIGNUP_001_Register_With_Valid_Data(
            String csvUsername,
            String csvEmail,
            String password,
            String confirmPassword
    ) {
        SignupPage signupPage = new SignupPage(driver);

        String username = incrementTrailingNumber(csvUsername);

        // Mise à jour du CSV avec le nouveau username pour les prochaines exécutions
        CSVUtils.updateCSVValue("testdata/signupData.csv", 0, csvUsername, username);

        signupPage.navigateToRegisterPage();
        signupPage.fillUsername(username);
        signupPage.fillEmail(csvEmail);
        signupPage.fillPassword(password);
        signupPage.fillConfirmPassword(confirmPassword);
        signupPage.agreeTerms();
        signupPage.submitRegistration();

        Assert.assertTrue(
                signupPage.isRegistrationSuccessful(),
                "Inscription non confirmee. Indicateur : " + signupPage.getRegistrationIndicatorText()
        );
    }

    private String incrementTrailingNumber(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "user1";
        }

        String value = username.trim();
        int index = value.length() - 1;
        while (index >= 0 && Character.isDigit(value.charAt(index))) {
            index--;
        }

        // Aucun chiffre final -> on ajoute 1
        if (index == value.length() - 1) {
            return value + "1";
        }

        String prefix = value.substring(0, index + 1);
        String numberPart = value.substring(index + 1);
        int incremented = Integer.parseInt(numberPart) + 1;
        return prefix + incremented;
    }
}
