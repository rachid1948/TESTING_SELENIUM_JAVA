package org.example.dataproviders;

import org.example.utils.CSVUtils;
import org.testng.annotations.DataProvider;

public class AppDataProvider {

    @DataProvider(name = "productData")
    public Object[][] getProductData() {
        return CSVUtils.readCSV("testdata/productData.csv");
    }

    // TC_SIGNUP_001 : donnees valides (ligne 1 CSV)
    @DataProvider(name = "signupSuccessData")
    public Object[][] getSignupSuccessData() {
        Object[][] all = CSVUtils.readCSV("testdata/signupData.csv");
        return new Object[][]{ all[0] };
    }

    // TC_SIGNUP_004 : username trop court (ligne 2 CSV)
    @DataProvider(name = "signupUsernameTooShortData")
    public Object[][] getSignupUsernameTooShortData() {
        Object[][] all = CSVUtils.readCSV("testdata/signupData.csv");
        return new Object[][]{ all[1] };
    }

    // TC_SIGNUP_005 : password mismatch (ligne 3 CSV)
    @DataProvider(name = "signupPasswordMismatchData")
    public Object[][] getSignupPasswordMismatchData() {
        Object[][] all = CSVUtils.readCSV("testdata/signupData.csv");
        return new Object[][]{ all[2] };
    }
}
