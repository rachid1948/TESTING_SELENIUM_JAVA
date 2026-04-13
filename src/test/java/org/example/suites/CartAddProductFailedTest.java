package org.example.suites;

import org.example.base.BaseTest;
import org.example.pages.CartAddPage;
import org.example.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartAddProductFailedTest extends BaseTest {

    // @Test(description = "TC_F1 - Tenter d'ajouter un produit avec une quantité de 99")
    // public void TC_F1_ajouter_produit_quantite_99() {
    //     CartAddPage cart = new CartAddPage(driver);
    //     cart.navigateToProduct(Constants.PRODUCT_TABLET_B, "tablets");
    //     cart.setQuantity(99);
    //     int actualQty = cart.getProductQuantityFieldValue();
    //     Assert.assertEquals(actualQty, 10,
    //             "La quantité saisie (99) doit être automatiquement limitée à 10 par le site. "
    //             + "Quantité réelle affichée dans le champ : " + actualQty);
    // }
}
