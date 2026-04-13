package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.example.utils.Constants.productByCategory;
import static org.example.utils.Constants.productByName;

public class CartAddPage extends BasePage {

    @FindBy(id = "shoppingCartLink")
    private WebElement cartIcon;

    @FindBy(css = "span.cart")
    private WebElement cartCountBadge;

    @FindBy(name = "save_to_cart")
    private WebElement addToCartButton;

    @FindBy(css = "div.plus")
    private WebElement plusButton;

    @FindBy(name = "quantity")
    private WebElement quantityInput;

    public CartAddPage(WebDriver driver) {
        super(driver);
    }

    public void goToHome() {
        driver.get(Constants.BASE_URL);
    }

    private void clickCategoryProduct(String category) {
        By categoryLocator = productByCategory(category);
        wait.until(ExpectedConditions.presenceOfElementLocated(categoryLocator));
        wait.until(ExpectedConditions.elementToBeClickable(categoryLocator));
        click(driver.findElement(categoryLocator));
    }

    private void clickProductByName(String productName) {
        By productLocator = productByName(productName);
        wait.until(ExpectedConditions.presenceOfElementLocated(Constants.PRODUCTS_GRID));
        wait.until(ExpectedConditions.presenceOfElementLocated(productLocator));
        wait.until(ExpectedConditions.elementToBeClickable(productLocator));
        click(driver.findElement(productLocator));
    }

    /**
     * Navigates to the product and clicks "Add to Cart" once (default quantity = 1).
     */
    public void addProductToCart(String productName, String category) {
        clickCategoryProduct(category);
        clickProductByName(productName);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        click(addToCartButton);
        waitForLoaderToDisappear();
    }

    /**
     * Navigates to the product and clicks "Add to Cart" n times.
     * Each click adds 1 unit and increments the cart counter by 1.
     */
    public void addProductToCartNTimes(String productName, String category, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }
        clickCategoryProduct(category);
        clickProductByName(productName);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        for (int i = 0; i < n; i++) {
            click(addToCartButton);
            waitForLoaderToDisappear();
        }
    }

    /**
     * Returns the numeric value shown on the cart counter badge.
     */
    public int getCartCount() {
        wait.until(ExpectedConditions.visibilityOf(cartCountBadge));
        return Integer.parseInt(cartCountBadge.getText().trim());
    }

    /**
     * Returns the cart count, or 0 if the badge is not visible (empty cart).
     */
    public int getCartCountOrZero() {
        By badgeLocator = By.cssSelector("#shoppingCartLink span.cart");
        java.util.List<WebElement> badges = driver.findElements(badgeLocator);
        if (badges.isEmpty()) {
            return 0;
        }
        WebElement badge = badges.get(0);
        if (!badge.isDisplayed()) {
            return 0;
        }
        String text = badge.getText().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    /**
     * Navigates to the shopping cart page.
     */
    public void goToCart() {
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon));
        click(cartIcon);
    }

    /**
     * Returns true if the product name (case-insensitive) is found in the cart page.
     */
    public boolean isProductInCart(String productName) {
        By nameLocator = By.xpath("//*[contains(translate(normalize-space(text()),"
                + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),"
                + "'" + productName.toUpperCase() + "')]");
        return !driver.findElements(nameLocator).isEmpty();
    }

    /**
     * Returns the product name displayed in the cart.
     */
    public String getCartProductName() {
        By nameLocator = By.cssSelector("h3.ng-binding");
        WebElement nameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(nameLocator));
        return nameEl.getText().trim();
    }

    /**
     * Returns the product quantity shown in the cart (parses "QTY: N").
     */
    public int getCartProductQuantity() {
        By qtyLocator = By.xpath("//label[contains(text(),'QTY')]");
        WebElement qtyEl = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyLocator));
        return Integer.parseInt(qtyEl.getText().replaceAll("\\D+", ""));
    }

    /**
     * Navigates to the product page and waits until the quantity input is ready.
     */
    public void navigateToProduct(String productName, String category) {
        clickCategoryProduct(category);
        clickProductByName(productName);
        wait.until(ExpectedConditions.visibilityOf(quantityInput));
        wait.until(ExpectedConditions.elementToBeClickable(quantityInput));
    }

    /**
     * Clears the quantity input, types the given value, then tabs out to trigger AngularJS validation.
     */
    public void setQuantity(int qty) {
        type(quantityInput, String.valueOf(qty));
        quantityInput.sendKeys(Keys.TAB);
        waitForLoaderToDisappear();
    }

    /**
     * Returns the current value displayed in the product-page quantity input field.
     */
    public int getProductQuantityFieldValue() {
        wait.until(ExpectedConditions.visibilityOf(quantityInput));
        String value = quantityInput.getAttribute("value");
        return (value == null || value.isEmpty()) ? 0 : Integer.parseInt(value.trim());
    }

    /**
     * Navigates to the product, types the given quantity directly into the input, then clicks "Add to Cart".
     */
    public void addProductToCartWithQuantity(String productName, String category, int quantity) {
        navigateToProduct(productName, category);
        setQuantity(quantity);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        click(addToCartButton);
        waitForLoaderToDisappear();
    }

    /**
     * Returns true if the cart contains no products.
     */
    public boolean isCartEmpty() {
        By removeLocator = By.xpath("//a[contains(@class,'remove')]");
        return driver.findElements(removeLocator).isEmpty();
    }
}
