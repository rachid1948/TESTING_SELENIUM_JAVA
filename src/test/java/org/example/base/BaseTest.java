package org.example.base;

import org.example.utils.Constants;
import org.example.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected WebDriver driver;
    @BeforeMethod
    public void setup() {
        driver = DriverFactory.getDriver();
        driver.get(Constants.BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtil.takeScreenshot(
                    driver,
                    result.getName()
            );
        }
        DriverFactory.quitDriver();
    }
}
