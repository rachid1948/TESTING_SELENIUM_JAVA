package org.example.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

protected WebDriver driver;
protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        int explicitWait = 20;
        wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }
    protected void waitForLoaderToDisappear() {
        By loader = By.cssSelector(".loader");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
        } catch (TimeoutException e) {
            // Le loader n’était pas présent ou déjà disparu
        }
    }
    protected void click(WebElement element) {
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void type(WebElement element, String value) {
        wait.until(ExpectedConditions.visibilityOf(element)).clear();
        element.sendKeys(value);
    }

    protected void selectByText(WebElement element, String text) {
        new Select(wait.until(ExpectedConditions.visibilityOf(element))).selectByVisibleText(text);
    }

    protected WebElement waitVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    protected void forceType(WebElement element, String value) {
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(value);
    }

    protected void pressEnter(WebElement element) {
        element.sendKeys(Keys.ENTER);
    }

    protected boolean isTextPresent(String text) {
        return driver.getPageSource().contains(text);
    }

    protected void waitForOverlaysToDisappear() {
        By overlays = By.cssSelector(".loader, .overlay, .modal");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlays));
        } catch (TimeoutException e) {

        }
    }

    protected void waitForUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    /*protected boolean isElementPresent(WebElement element) {
        return  element.findElements().isEmpty();
    }*/

   /* protected void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }*/

}
