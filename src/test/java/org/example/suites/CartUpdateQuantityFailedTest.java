package org.example.suites;

import org.example.base.BaseTest;
import org.example.pages.CartUpdateQuantityPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartUpdateQuantityFailedTest extends BaseTest {

    @Test(description = "N01 - Quantite 11 Refusee Pour La Meme Couleur", groups = {"ANOMALIE_STOCK", "KNOWN_ISSUE"})
    public void n01_quantite_11_refusee_pour_la_meme_couleur() {
        CartUpdateQuantityPage page = new CartUpdateQuantityPage(driver);

        page.openTabletsCategory();
        page.openFirstProduct();
        page.selectColorByIndex(0);
        page.setQuantityOnPdp(11);
        page.addCurrentProductToCart();
        page.openCart();

        int current = page.getCartQuantity();
        Assert.assertTrue(current <= page.getMaxQtyPerColor(), "La quantite depasse la limite par couleur");
    }

    @Test(description = "N02 - Bouton Plus Bloque A 10 Pour La Meme Couleur", groups = {"ANOMALIE_STOCK", "KNOWN_ISSUE"})
    public void n02_bouton_plus_bloque_a_10_pour_la_meme_couleur() {
        CartUpdateQuantityPage page = new CartUpdateQuantityPage(driver);

        page.openTabletsCategory();
        page.openFirstProduct();
        page.selectColorByIndex(0);
        page.setQuantityOnPdp(10);
        page.addCurrentProductToCart();
        page.openCart();

        Assert.assertEquals(page.getCartQuantity(), 10);

        page.openHome();
        page.openTabletsCategory();
        page.openFirstProduct();
        page.selectColorByIndex(0);
        page.setQuantityOnPdp(1);
        page.addCurrentProductToCart();
        page.openCart();

        int current = page.getCartQuantity();
        Assert.assertTrue(current <= page.getMaxQtyPerColor(), "La quantite depasse la limite par couleur");
    }

    @Test(description = "TC21 - Suppression Produit Via Quantite Zero", groups = {"NEGATIVE"})
    public void tc21_suppression_produit_via_quantite_zero() {
        CartUpdateQuantityPage page = new CartUpdateQuantityPage(driver);

        page.openTabletsCategory();
        page.openFirstProduct();
        page.selectFirstAvailableColor();
        page.setQuantityOnPdp(1);
        page.addCurrentProductToCart();
        page.openCart();

        page.setDisplayedQuantity(0);

        int rowCount = page.getCartRowCount();
        int qtyCount = page.getQtyInputCount();

        Assert.assertTrue(rowCount == 0 || qtyCount == 0,
                "Le produit devrait etre supprime quand la quantite passe a zero");
    }
}
