package org.example.suites;

import org.example.base.BaseTest;
import org.example.dataproviders.AppDataProvider;
import org.example.pages.CartDeletePage;
import org.example.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartDeleteProductSuccessTest extends BaseTest {

    @Test(description = "TC1_Suppression totale produit")
    public void TC1_suppression_totale() {
        CartDeletePage cart = new CartDeletePage(driver);
        //cart.ensureCartIsEmpty();
        //cart.goToHome();
        cart.addProductToCart(Constants.PRODUCT_TABLET_A,"tablets");
        cart.goToCart();
        cart.removeProductViaRemoveButton();
        Assert.assertTrue(cart.isCartEmpty());
    }
    @Test(description = "TC2_Suppression automatique quand quantité = 0")
    public void TC2_quantite_zero_supprime_produit() {
        CartDeletePage cart = new CartDeletePage(driver);
        //cart.ensureCartIsEmpty();
        //cart.goToHome();
        cart.addProductToCart(Constants.PRODUCT_TABLET_A,"tablets");
        cart.goToCart();
        cart.editQuantity(0);
        Assert.assertTrue(cart.isCartEmpty());
        Assert.assertFalse(cart.isProductPresent(Constants.PRODUCT_TABLET_A));
    }

    @Test(description = "TC3_diminuer automatiquement la quantité d'un produit")
    public void TC3_Diminuer_quantite_x_produit() {

        int x = 5; // quantité ajoutée
        int y = 2; // quantité supprimée
        int finalQty = x - y;
        CartDeletePage cart = new CartDeletePage(driver);
       // cart.ensureCartIsEmpty();
        //cart.goToHome();

        //ajout avec X via +
        cart.addProductToCart(Constants.PRODUCT_TABLET_A, "tablets", x);
        cart.goToCart();
        cart.editQuantity(finalQty);

        if (finalQty <= 0) {
            Assert.assertTrue(cart.isCartEmpty());
            Assert.assertFalse(cart.isProductPresent(Constants.PRODUCT_TABLET_A));
        } else {
            Assert.assertEquals(
                    cart.getQuantityFromCart(),
                    finalQty
            );
           // Assert.assertEquals( cart.getQuantityForProduct(Constants.PRODUCT_TABLET_A), finalQty );
        }
    }

    @Test(description = "TC4_Suppression totale produit - boucle CSV",
            dataProvider = "productData",
            dataProviderClass = AppDataProvider.class
    )
    public void TC4_suppression_totale_produit_boucle_csv(
            String locator,
            String productName
    ) {
        System.out.println("Test avec " + locator);
        System.out.println("Message attendu : " + productName);
        CartDeletePage cart = new CartDeletePage(driver);
       // cart.ensureCartIsEmpty();
        //cart.goToHome();
        cart.addProductToCart(productName,locator);
        cart.goToCart();
        cart.removeProductViaRemoveButton();
        Assert.assertTrue(cart.isCartEmpty());
    }


    @Test(description = "TC5_Suppression parmi plusieurs produits")
    public void TC5_suppression_parmi_plusieurs() {
        CartDeletePage cart = new CartDeletePage(driver);
        //cart.ensureCartIsEmpty();
       // cart.goToHome();
        cart.addProductToCart(Constants.PRODUCT_TABLET_A,"tablets",1);
        cart.goToHome();
        cart.addProductToCart(Constants.PRODUCT_SPEAKER_A,"speakers",1);
        cart.goToCart();
        cart.removeProductViaRemoveButton2();
        Assert.assertTrue(!cart.isCartEmpty());
        Assert.assertTrue(cart.isProductPresent(Constants.PRODUCT_SPEAKER_A));
    }

  @Test(description = "TC6_Suppression via popup")
    public void TC6_suppression_popup() {
        CartDeletePage cart = new CartDeletePage(driver);
       // cart.ensureCartIsEmpty();
       // cart.goToHome();
        cart.addProductToCart(Constants.PRODUCT_TABLET_A,"tablets");
        cart.goToCart();
        cart.removeProductFromPopup();
        Assert.assertTrue(cart.isCartEmpty());
    }


   /*
    TC5_Suppression totale produit - boucle CSV
    [Tags]    TC5_CartDeletePage
    ${content}=    Get File    ${CURDIR}/../data/remove_products.csv
    @{lines}=      Split To Lines    ${content}

    FOR    ${line}    IN    @{lines}
    @{data}=    Split String    ${line}    ,
    ${category}=    Set Variable    ${data[0]}
    ${product}=     Set Variable    ${data[1]}

    Log    Test avec ${category} - ${product}
    Ensure Cart Is Empty
    Add Product To Cart    ${category}    ${product}
    Go To Cart
    Remove Product Via Remove Button
    Page Should Contain    ${EMPTY_MSG}
        #Aller à la home
    Go To    ${URL}
    END
    */


}
