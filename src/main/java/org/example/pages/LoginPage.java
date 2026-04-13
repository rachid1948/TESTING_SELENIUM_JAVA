package org.example.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private By Usermenu = By.xpath("//*[@id='menuUser']");
    private By arcticlemenu = By.id("our_products");
    private By usernameField = By.xpath("//input[@name='username']");
    private By passwordField = By.xpath("//input[@name='password']");
    private By loginButton = By.id("sign_in_btn");
    
    private By loggedIndicator = By.xpath("//span[@class='hi-user containMiniTitle ng-binding']"); // example
    private By errorMessage = By.xpath("//label[@id='signInResultMessage']");
    private By errorMessageFieldRequired = By.xpath("//label[@class='invalid' and contains(text(),'required')]");
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String url) {
        driver.get(url);
        
    }
    
    public void clickUserMenu() {
    	
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(arcticlemenu)); // Wait for page to load
        driver.findElement(arcticlemenu).isDisplayed(); // Wait for page to load
        driver.findElement(Usermenu).click();
	}

    public void enterUsername(String username) {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	WebElement field = wait.until(ExpectedConditions.elementToBeClickable(usernameField));
    	
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	WebElement field = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
    	
    	field.clear();
        field.sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public boolean isLoggedIn() {
        try {
        	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        	WebElement indicator = wait.until(ExpectedConditions.visibilityOfElementLocated(loggedIndicator));
        	System.out.println("Logged in indicator found: " + indicator.getText());
            return indicator.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
   public boolean isErrorMessageDisplayed() {
		try {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				WebElement errorMessageindicator = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
				System.out.println("Error message found: " + errorMessageindicator.getText());
				return errorMessageindicator.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
   public boolean isRequiredFieldErrorDisplayed() {
	   	try {
	   			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	   			WebElement errorMessageindicator = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageFieldRequired));
	   			System.out.println("Required field error message found: " + errorMessageindicator.getText());
	   			return errorMessageindicator.isDisplayed();
	   		} catch (Exception e) {
	   				return false;
	   	}
   }
}

