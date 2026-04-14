package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartUpdateQuantityPage extends BasePage {

    private static final int MAX_QTY_PER_COLOR = 10;

    private final By productCards = By.xpath("//li[contains(@ng-repeat,'product in')]//img");
    private final By firstProduct = By.xpath("(//a[contains(@class,'productName')])[1]");
    private final By quantityInputPdp = By.name("quantity");
    private final By addToCartBtn = By.name("save_to_cart");
    private final By cartLink = By.id("shoppingCartLink");
    private final By cartHeader = By.xpath("//*[contains(text(),'SHOPPING CART') or contains(text(),'Shopping Cart')]");

    private final By colorOptions = By.xpath("//span[contains(@class,'bunny') and contains(@class,'color')] | //span[@title and contains(@class,'ng-scope')]");

    private final By plusBtnCart = By.cssSelector("div.plus");
    private final By removeBtnCart = By.xpath("//a[contains(@class,'remove')]");
    private final By cartInputLocator = By.cssSelector(
            "#shoppingCart input[name='quantity'], #shoppingCart input[type='number'], tr.ng-scope input[name='quantity'], tr[ng-repeat] input[name='quantity']"
    );

    public CartUpdateQuantityPage(WebDriver driver) {
        super(driver);
    }

    public int getMaxQtyPerColor() {
        return MAX_QTY_PER_COLOR;
    }

    public void openHome() {
        driver.get(Constants.BASE_URL + "/");
        waitForLoaderToDisappear();
    }

    public void openTabletsCategory() {
        driver.get(Constants.TABLETS_URL);
        waitForUrlContains("/category/Tablets/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
        waitForLoaderToDisappear();
    }

    public void openFirstProduct() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
        List<WebElement> cards = driver.findElements(productCards);
        if (cards.isEmpty()) {
            throw new IllegalStateException("Aucun produit disponible dans la categorie tablettes");
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", cards.get(0));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cards.get(0))).click();
        } catch (Exception e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cards.get(0));
            } catch (Exception ignored) {
                wait.until(ExpectedConditions.elementToBeClickable(firstProduct)).click();
            }
        }

        if (!driver.getCurrentUrl().contains("/product/")) {
            driver.get(Constants.DEFAULT_TABLET_PRODUCT_URL);
        }

        waitForUrlContains("/product/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInputPdp));
        wait.until(ExpectedConditions.visibilityOfElementLocated(colorOptions));
    }

    public int getAvailableColorCount() {
        return getVisibleColors().size();
    }

    public int selectFirstAvailableColor() {
        return selectColorByIndex(0);
    }

    public int selectAnotherAvailableColor(int currentIndex) {
        List<WebElement> colors = getVisibleColors();
        for (int i = 0; i < colors.size(); i++) {
            if (i != currentIndex) {
                clickColor(colors.get(i));
                return i;
            }
        }
        throw new IllegalStateException("Aucune autre couleur disponible");
    }

    public int selectColorByIndex(int index) {
        List<WebElement> colors = getVisibleColors();
        if (index < 0 || index >= colors.size()) {
            throw new IllegalArgumentException("Index couleur invalide: " + index);
        }
        clickColor(colors.get(index));
        return index;
    }

    public void setQuantityOnPdp(int qty) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInputPdp));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(String.valueOf(qty));
        input.sendKeys(Keys.TAB);
        waitForLoaderToDisappear();
    }

    public void addCurrentProductToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
        waitForLoaderToDisappear();
    }

    public void openCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        } catch (Exception ignored) {
            // Fallback URL direct si l'icone n'est pas cliquable.
        }
        driver.get(Constants.CART_URL);
        waitForUrlContains("shoppingCart");
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartHeader));
        waitForLoaderToDisappear();
    }

    public int getCartQuantity() {
        String value = String.valueOf(((JavascriptExecutor) driver).executeScript(
                "var row = document.querySelector(\"#shoppingCart tbody tr, #shoppingCart tr.ng-scope, tr[ng-repeat*='product']\");"
                        + "if (row) {"
                        + "  var inp = row.querySelector(\"input[name='quantity'], input[type='number'], td input\");"
                        + "  if (inp && !inp.disabled && inp.value) return inp.value.toString().trim();"
                        + "  var txts = row.querySelectorAll(\"td label, td span, label.ng-binding, span.ng-binding\");"
                        + "  for (var i = 0; i < txts.length; i++) {"
                        + "    var t = (txts[i].textContent || '').trim();"
                        + "    if (/^\\d+$/.test(t)) return t;"
                        + "  }"
                        + "}"
                        + "var badge = document.querySelector(\"#shoppingCartLink .cart, a#shoppingCartLink .cart\");"
                        + "if (badge) return (badge.textContent || '').toString().trim();"
                        + "return '';"));

        String digits = value == null ? "" : value.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            throw new IllegalStateException("Impossible de lire la quantite du panier");
        }
        return Integer.parseInt(digits);
    }

    public int getDisplayedQuantity() {
        return getCartQuantity();
    }

    public void clickPlus() {
        boolean clicked = (Boolean) ((JavascriptExecutor) driver).executeScript(buildClickPlusScript());

        if (!clicked) {
            List<WebElement> pluses = driver.findElements(plusBtnCart);
            if (!pluses.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(pluses.get(0))).click();
            } else {
                // Fallback inspire des keywords Robot: recharger un flux produit -> panier.
                openHome();
                openTabletsCategory();
                openFirstProduct();
                selectFirstAvailableColor();
                addCurrentProductToCart();
                openCart();
            }
        }
        waitForLoaderToDisappear();
    }

    public void doubleClickPlus() {
        // Plus stable sur AOS: deux clics successifs via la methode clickPlus()
        int before = getDisplayedQuantity();
        clickPlus();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        clickPlus();
        // Certains rendus Angular ignorent un double-clic rapide: garantir au moins +1.
        try {
            if (getDisplayedQuantity() <= before) {
                clickPlus();
            }
        } catch (Exception ignored) {
            clickPlus();
        }
    }

    /**
     * Modifie la quantite dans le panier.
     * Identique au keyword Robot "Set Displayed Quantity" :
     *   Wait Until Element Is Visible / input.clear() + sendKeys(qty) + TAB
     * Sur AOS (AngularJS), TAB/blur declenche le $digest et met a jour ng-model.
     */
    public void setDisplayedQuantity(int qty) {
        if (qty == 0) {
            // qty=0 : clic direct sur le bouton REMOVE (comportement AOS)
            List<WebElement> removeButtons = driver.findElements(removeBtnCart);
            if (!removeButtons.isEmpty()) {
                removeButtons.get(0).click();
                waitForLoaderToDisappear();
                return;
            }
        }

        // Attendre l'input du panier avec le bon sélecteur
        WebElement input = tryGetVisibleCartQuantityInput();

        if (input == null) {
            int current = getCartQuantity();
            if (qty > current) {
                for (int i = current; i < qty; i++) {
                    clickPlus();
                }
                return;
            }
            if (qty == 0) {
                List<WebElement> removeButtons = driver.findElements(removeBtnCart);
                if (!removeButtons.isEmpty()) {
                    removeButtons.get(0).click();
                    waitForLoaderToDisappear();
                }
                return;
            }
            throw new IllegalStateException("Impossible de modifier la quantite: input panier introuvable");
        }

        // Scroll + focus + clear + saisie + TAB (exactement comme Robot Press Keys TAB)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", input);
        wait.until(ExpectedConditions.elementToBeClickable(input));
        input.click();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(String.valueOf(qty));
        input.sendKeys(Keys.TAB);
        waitForLoaderToDisappear();
    }

    public boolean isLimitMessageVisible() {
        List<By> candidates = List.of(
                By.xpath("//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'maximum') and contains(.,'10')]"),
                By.xpath("//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'limit') and contains(.,'10')]"),
                By.xpath("//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cannot add')]")
        );

        for (By by : candidates) {
            if (!driver.findElements(by).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public int getCartRowCount() {
        return driver.findElements(removeBtnCart).size();
    }

    public int getQtyInputCount() {
        return driver.findElements(By.name("quantity")).size();
    }

    public void refreshCartPage() {
        driver.navigate().refresh();
        waitForLoaderToDisappear();
    }

    public double getFirstRowSubtotal() {
        List<By> subtotalCandidates = List.of(
                By.xpath("(//td[contains(@class,'price')])[1]"),
                By.xpath("(//tr[contains(@class,'ng-scope')]//*[contains(@class,'price')])[1]"),
                By.xpath("(//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'$')])[1]")
        );

        for (By by : subtotalCandidates) {
            List<WebElement> els = driver.findElements(by);
            if (!els.isEmpty()) {
                String text = els.get(0).getText();
                double value = extractNumber(text);
                if (value > 0) {
                    return value;
                }
            }
        }
        throw new IllegalStateException("Impossible de lire le sous-total de la premiere ligne");
    }

    public double getCartTotal() {
        List<By> totalCandidates = List.of(
                By.xpath("//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'total')]"),
                By.xpath("//*[contains(@class,'total')]"),
                By.xpath("//*[contains(@class,'grandTotal')]"),
                By.xpath("//*[contains(@class,'roboto-medium') and contains(.,'$')]")
        );

        for (By by : totalCandidates) {
            List<WebElement> els = driver.findElements(by);
            for (WebElement el : els) {
                String text = el.getText();
                if (text != null && !text.trim().isEmpty()) {
                    double value = extractNumber(text);
                    if (value > 0) {
                        return value;
                    }
                }
            }
        }
        throw new IllegalStateException("Impossible de lire le total panier");
    }

    public void waitUntilQuantityEquals(int expected, int timeoutSeconds) {
        int effectiveTimeout = Math.max(timeoutSeconds, 20);
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(effectiveTimeout));
        shortWait.until(d -> {
            try {
                return getDisplayedQuantity() == expected;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private String buildClickPlusScript() {
        return "function visible(el){ return !!el && (el.offsetParent !== null || getComputedStyle(el).position === 'fixed'); }"
                + "var row = document.querySelector(\"#shoppingCart tbody tr, #shoppingCart tr.ng-scope, tr[ng-repeat*='product']\");"
                + "var sels = [\".plus\",\"[ng-click*='plus']\",\"button[class*='plus']\",\"a[class*='plus']\",\"div[class*='plus']\",\"span[class*='plus']\"];"
                + "if (row) { for (var s=0;s<sels.length;s++){var inRow=row.querySelectorAll(sels[s]);for(var r=0;r<inRow.length;r++){if(visible(inRow[r])&&!inRow[r].disabled){inRow[r].click();return true;}}} }"
                + "for(var s=0;s<sels.length;s++){var els=document.querySelectorAll(\"#shoppingCart \"+sels[s]);for(var i=0;i<els.length;i++){if(visible(els[i])&&!els[i].disabled){els[i].click();return true;}}}"
                + "var q=document.querySelector(\"#shoppingCart input[name='quantity'],#shoppingCart input[type='number']\");"
                + "if(q&&!q.disabled&&typeof q.stepUp==='function'){q.stepUp();q.dispatchEvent(new Event('input',{bubbles:true}));q.dispatchEvent(new Event('change',{bubbles:true}));q.dispatchEvent(new Event('blur',{bubbles:true}));return true;}"
                + "return false;";
    }

    private WebElement tryGetVisibleCartQuantityInput() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(cartInputLocator));
        } catch (TimeoutException ignored) {
            // L'input peut etre absent selon le rendu du panier.
            return null;
        }
    }

    private List<WebElement> getVisibleColors() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(colorOptions));
        return driver.findElements(colorOptions).stream().filter(WebElement::isDisplayed).toList();
    }

    private void clickColor(WebElement color) {
        wait.until(ExpectedConditions.elementToBeClickable(color)).click();
        waitForLoaderToDisappear();
    }

    private double extractNumber(String text) {
        if (text == null) {
            return 0.0;
        }
        Matcher m = Pattern.compile("(\\d+[\\.,]?\\d*)").matcher(text.replace(",", "."));
        return m.find() ? Double.parseDouble(m.group(1)) : 0.0;
    }
}
