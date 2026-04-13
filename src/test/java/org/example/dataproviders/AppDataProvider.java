package org.example.dataproviders;

import org.example.utils.CSVUtils;
import org.testng.annotations.DataProvider;

public class AppDataProvider {

    @DataProvider(name = "productData")
    public Object[][] getProductData() {
        return CSVUtils.readCSV("testdata/productData.csv");
    }

}
