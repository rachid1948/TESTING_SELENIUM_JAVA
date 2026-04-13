package org.example.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static WebDriver driver;
    private static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();
    public static WebDriver getDriver() {
        if (driver == null) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }
        return driver;
    }
    
    
    public static void initDriver(String browser) {
        if (drivers.get() != null) {
            return; // already initialized for this thread
        }

        if ("chrome".equalsIgnoreCase(browser)) {
            // Selenium Manager (Selenium 4.6+) will resolve the driver automatically
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            WebDriver wd = new ChromeDriver(options);
            drivers.set(wd);
        } else {
            // Fallback to Chrome if unknown
            ChromeOptions options = new ChromeOptions();
            WebDriver wd = new ChromeDriver(options);
            drivers.set(wd);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}

