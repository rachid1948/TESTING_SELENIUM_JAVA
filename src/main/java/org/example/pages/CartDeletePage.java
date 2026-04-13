package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.Constants;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.example.utils.Constants.productByCategory;
import static org.example.utils.Constants.productByName;

public class CartDeletePage extends BasePage {

    @FindBy(id = "shoppingCartLink")
    private WebElement cartIcon;

    @FindBy(xpath = "//a[contains(@class,'remove')]")
    private WebElement removeBtn;

    @FindBy(xpath = "//a[contains(@class,'edit')]")
    private WebElement editBtn;

    @FindBy(name = "quantity")
    private WebElement qtyInput;

    @FindBy(xpath = "//a[@class='removeProduct']")
    private WebElement popupRemoveBtn;

    @FindBy(xpath = "//div[@id='tabletsImg']")
    private WebElement categoryTabletsLocator;

    @FindBy(xpath ="//div[@id='headphonesImg']")
    private WebElement categoryHeadphonesLocator;

    @FindBy(xpath = "//button[@name='save_to_cart']")
    private WebElement addToCartBtnLocator;

    @FindBy(css = "div.plus")
    private WebElement plusButton;

    @FindBy(name = "quantity")
    private WebElement quantityInput;

    @FindBy(name = "save_to_cart")
    private WebElement addToCartButton;

    // Pour les ASSERTIONS (absence)
    //private static final By REMOVE_BTN_LOCATOR =By.xpath("//a[contains(@class,'remove')]");

    public CartDeletePage(WebDriver driver) {
        super(driver);
    }

   /* public void goToCart() {
        waitForLoaderToDisappear();
        click(cartIcon);
    }*/

    public void goToCart() {
        By loader = By.cssSelector(".loader");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon));
        click(cartIcon);
    }

  public void removeProductViaRemoveButton(){
        waitForLoaderToDisappear();
        click(removeBtn);
    }


  /*  public void removeProductViaRemoveButton() {

        // attendre la stabilité du panier
        waitForLoaderToDisappear();

        By removeLocator = By.xpath("//a[contains(@class,'remove')]");

        // attendre présence
        WebElement remove = wait.until(
                ExpectedConditions.presenceOfElementLocated(removeLocator)
        );

        // attendre visibilité réelle
        wait.until(ExpectedConditions.visibilityOf(remove));

        // attendre cliquabilité réelle
        wait.until(ExpectedConditions.elementToBeClickable(remove));

        // scroll obligatoire (évite interception)
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", remove);

        // clic JS (DERNIÈRE GARANTIE)
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", remove);

        // attendre que l’élément disparaisse
        wait.until(ExpectedConditions.stalenessOf(remove));
    }*/

    public void removeProductViaRemoveButton2() {

        waitForLoaderToDisappear();

        By removeLocator = By.xpath("//a[contains(@class,'remove')]");

        // nombre de boutons REMOVE avant clic
        int removeCountBefore = driver.findElements(removeLocator).size();

        WebElement remove = wait.until(
                ExpectedConditions.elementToBeClickable(removeLocator)
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", remove);

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", remove);

        // attendre que le nombre de produits diminue
        wait.until(driver ->
                driver.findElements(removeLocator).size() == removeCountBefore - 1
        );
    }


    /*public void removeProductViaRemoveButton() {

        waitForLoaderToDisappear();

        By removeLocator = By.xpath("//a[contains(@class,'remove')]");

        int removeCountBefore = driver.findElements(removeLocator).size();

        WebElement remove = wait.until(
                ExpectedConditions.elementToBeClickable(removeLocator)
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", remove);

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", remove);

        if (removeCountBefore == 1) {
            // TC1 : attendre panier vide
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.tagName("body"),
                    Constants.EMPTY_MSG
            ));
        } else {
            // TC2 : attendre qu'un produit reste
            wait.until(driver ->
                    driver.findElements(removeLocator).size() == removeCountBefore - 1
            );
        }
    }*/

    public void removeProductFromPopup() {
        By popupRemove = By.xpath("//a[@class='removeProduct']");
        // attendre que la popup existe
        wait.until(ExpectedConditions.presenceOfElementLocated(popupRemove));

        WebElement remove = wait.until(
                ExpectedConditions.elementToBeClickable(popupRemove)
        );

        remove.click();

        //click(popupRemoveBtn);
    }

public void editQuantity(int value) {
        click(editBtn);
        forceType(qtyInput, Integer.toString(value));
        pressEnter(qtyInput);
        //sleep();
    }

    /*public void editQuantity(int value) {

        By editLocator = By.xpath("//a[contains(@class,'edit')]");

        //Cliquer sur EDIT seulement s’il existe
        if (!driver.findElements(editLocator).isEmpty()) {
            click(driver.findElement(editLocator));
        }

        //Le champ est maintenant éditable (ou l’était déjà)
        wait.until(ExpectedConditions.visibilityOf(qtyInput));

        qtyInput.sendKeys(Keys.CONTROL + "a");
        qtyInput.sendKeys(Keys.DELETE);
        qtyInput.sendKeys(String.valueOf(value));
        qtyInput.sendKeys(Keys.ENTER);

        //Synchronisation métier
        if (value <= 0) {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.tagName("body"),
                    Constants.EMPTY_MSG
            ));
        } else {
            wait.until(ExpectedConditions.attributeContains(
                    qtyInput,
                    "value",
                    String.valueOf(value)
            ));
        }
    }*/
   /* public void editQuantity(int value) {

        By editLocator = By.xpath("//a[contains(@class,'edit')]");
        //Si le bouton EDIT existe, on clique dessus
        if (!driver.findElements(editLocator).isEmpty()) {
            click(editBtn);
        }

        //Le champ quantité est maintenant éditable
        wait.until(ExpectedConditions.visibilityOf(qtyInput));

        qtyInput.sendKeys(Keys.CONTROL + "a");
        qtyInput.sendKeys(Keys.DELETE);
        qtyInput.sendKeys(String.valueOf(value));
        qtyInput.sendKeys(Keys.ENTER);

        //Synchronisation métier
        if (value <= 0) {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.tagName("body"),
                    Constants.EMPTY_MSG
            ));
        } else {
            wait.until(ExpectedConditions.attributeContains(
                    qtyInput,
                    "value",
                    String.valueOf(value)
            ));
        }
    }*/

    /*public int getQuantityForProduct(String productName) {

        By qtyLocator = By.xpath(
                "//*[normalize-space()='" + productName + "']" +
                        "/ancestor::tr//label[contains(text(),'QTY')]"
        );

        WebElement qtyLabel = wait.until(
                ExpectedConditions.visibilityOfElementLocated(qtyLocator)
        );

        String text = qtyLabel.getText(); // ex: "QTY 3"

        return Integer.parseInt(text.replaceAll("\\D+", ""));
    }*/

    public int getQuantityFromCart() {

        // Cas 1 : input quantity (quand EDIT est actif)
        By inputLocator = By.name("quantity");

        if (!driver.findElements(inputLocator).isEmpty()) {
            WebElement input = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(inputLocator)
            );
            return Integer.parseInt(input.getAttribute("value"));
        }

        // Cas 2 : affichage normal (QTY + nombre dans 2 labels)
        By qtyValueLocator = By.xpath(
                "//td[contains(@class,'quantityMobile')]//label[last()]"
        );

        WebElement qtyLabel = wait.until(
                ExpectedConditions.visibilityOfElementLocated(qtyValueLocator)
        );

        return Integer.parseInt(qtyLabel.getText().trim());
    }

   /* public boolean isCartEmpty() {
        return isTextPresent(Constants.EMPTY_MSG);
    }*/

    public boolean isCartEmpty() {

        // Un panier vide = aucun bouton REMOVE / aucune ligne produit
        By productRowLocator = By.xpath("//a[contains(@class,'remove')]");

        return driver.findElements(productRowLocator).isEmpty();
    }

    public boolean isProductPresent(String productName) {
        return isTextPresent(productName);
    }

    public boolean isRemoveBtnNotPresent() {
        return driver.findElements(Constants.REMOVE_BTN_LOCATOR).isEmpty();
    }

    public boolean isEditBtnNotPresent() {
        return driver.findElements(Constants.EDIT_BTN_LOCATOR).isEmpty();
    }
    public void goToHome() {
        driver.get(Constants.BASE_URL);
    }
    public void goToShopping() {
        driver.get(Constants.BASE_URL);

        // attendre que le loader disparaisse
        waitForLoaderToDisappear();

        // attendre que la home soit vraiment prête
        wait.until(ExpectedConditions.presenceOfElementLocated(
                Constants.CATEGORY_TABLETS_LOCATOR
        ));
    }

    public void ensureCartIsEmpty() {
        goToCart();
        if (!isCartEmpty()){
            try {
                while (true) {
                    removeProductViaRemoveButton();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//a[contains(@class,'remove')]")));
                }
            } catch (TimeoutException e) {
            }
        }
    }

    public void clickCategoryProduct(String category) {
        By categoryLocator =  productByCategory(category);
        wait.until(ExpectedConditions.presenceOfElementLocated(categoryLocator));
        wait.until(ExpectedConditions.elementToBeClickable(categoryLocator));
        click(driver.findElement(categoryLocator));
    }

    /*public void addProductToCart( String productName) {
        //Aller à la home
        // driver.get(Constants.BASE_URL);
        //Cliquer sur la catégorie
        click(categoryTabletsLocator);
        //Cliquer sur le produit
        clickProductByName(productName);
        //clickProductByNamePagination(productName);
        //Ajouter au panier
        click(addToCartBtnLocator);

    }*/

 /* public void addProductToCart( String productName,String categoryProduct) {
        //Aller à la home
       // driver.get(Constants.BASE_URL);
        //Cliquer sur la catégorie
       //click(categoryTabletsLocator);
        clickCategoryProduct(categoryProduct);
       //Cliquer sur le produit
        clickProductByName(productName);
       //Ajouter au panier
        click(addToCartBtnLocator);
    }*/

    public void addProductToCart(String productName, String categoryProduct) {

        //Aller à la catégorie
        clickCategoryProduct(categoryProduct);

        //Attendre que la grille produits soit chargée
        wait.until(ExpectedConditions.presenceOfElementLocated(
                Constants.PRODUCTS_GRID
        ));

        //Cliquer sur le produit
        clickProductByName(productName);

        //ATTENTE CRITIQUE : page détail chargée
        By addToCart = By.name("save_to_cart");

        wait.until(ExpectedConditions.presenceOfElementLocated(addToCart));
        wait.until(ExpectedConditions.elementToBeClickable(addToCart));

        //Ajouter au panier
        click(addToCartBtnLocator);

        //Attendre la stabilisation Angular
        waitForLoaderToDisappear();
    }

    public void addProductToCart(String productName,
                                 String categoryProduct,
                                 int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être > 0");
        }

        //Aller à la catégorie
        clickCategoryProduct(categoryProduct);

        //Attendre la grille produits
        wait.until(ExpectedConditions.presenceOfElementLocated(
                Constants.PRODUCTS_GRID
        ));

        //Cliquer sur le produit
        clickProductByName(productName);

        //Attendre la page produit
        wait.until(ExpectedConditions.visibilityOf(quantityInput));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));

        //Clique sur "+"
        // quantité initiale = 1 → on clique (quantity - 1) fois
        for (int i = 1; i < quantity; i++) {
            click(plusButton);
        }

        //Ajouter au panier UNE FOIS
        click(addToCartButton);

        // Attendre la stabilisation Angular
        waitForLoaderToDisappear();
    }
   /* public void addProductToCart(String productName,
                                 String categoryProduct,
                                 int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "La quantité doit être > 0"
            );
        }

        //Aller à la page produit une seule fois
        clickCategoryProduct(categoryProduct);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                Constants.PRODUCTS_GRID
        ));

        clickProductByName(productName);

        By addToCart = By.name("save_to_cart");
        wait.until(ExpectedConditions.elementToBeClickable(addToCart));

        //Cliquer X fois sur "Add to Cart"
        for (int i = 0; i < quantity; i++) {
            click(addToCartBtnLocator);
            waitForLoaderToDisappear();
        }
    }*/


    public void clickProductByName(String productName) {
        /*String xpath = String.format(
                Constants.PRODUCT_NAME_XPATH,
                productName
        );
        By productLocator = By.xpath(xpath);*/
        By productLocator =  productByName(productName);
        wait.until(ExpectedConditions.presenceOfElementLocated(productLocator));
        wait.until(ExpectedConditions.elementToBeClickable(productLocator));
        click(driver.findElement(productLocator));
    }

}
