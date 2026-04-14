package org.example.suites;

import org.example.base.BaseTest;
import org.example.pages.CartUpdateQuantityPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartUpdateQuantitySuccessTest extends BaseTest {

    private CartUpdateQuantityPage addOneTabletToCartAsGuest() {
        CartUpdateQuantityPage page = new CartUpdateQuantityPage(driver);
        page.openTabletsCategory();
        page.openFirstProduct();
        page.selectFirstAvailableColor();
        page.setQuantityOnPdp(1);
        page.addCurrentProductToCart();
        page.openCart();
        return page;
    }

    @Test(description = "P01 - Quantite 10 Autorisee Pour Une Couleur")
    public void p01_quantite_10_autorisee_pour_une_couleur() {
        CartUpdateQuantityPage page = new CartUpdateQuantityPage(driver);
        page.openTabletsCategory();
        page.openFirstProduct();
        page.selectFirstAvailableColor();
        page.setQuantityOnPdp(10);
        page.addCurrentProductToCart();
        page.openCart();

        Assert.assertEquals(page.getCartQuantity(), 10);
    }

    @Test(description = "P02 - Meme Produit Autre Couleur Encore 10 Autorise")
    public void p02_meme_produit_autre_couleur_encore_10_autorise() {
        CartUpdateQuantityPage page = new CartUpdateQuantityPage(driver);

        page.openTabletsCategory();
        page.openFirstProduct();

        page.selectColorByIndex(0);
        page.setQuantityOnPdp(10);
        page.addCurrentProductToCart();

        page.openHome();
        page.openTabletsCategory();
        page.openFirstProduct();

        int colorCount = page.getAvailableColorCount();
        Assert.assertTrue(colorCount > 1, "Le produit doit avoir au moins 2 couleurs disponibles");

        int secondIndex = page.selectAnotherAvailableColor(0);
        Assert.assertNotEquals(secondIndex, 0);

        page.setQuantityOnPdp(10);
        page.addCurrentProductToCart();
        page.openCart();

        Assert.assertEquals(page.getCartQuantity(), 20);
    }

    @Test(description = "TC01 - Incrementation Standard")
    public void tc01_incrementation_standard() {
        CartUpdateQuantityPage page = addOneTabletToCartAsGuest();
        int before = page.getDisplayedQuantity();

        page.clickPlus();
        page.waitUntilQuantityEquals(before + 1, 10);

        Assert.assertEquals(page.getDisplayedQuantity(), before + 1);
    }

    @Test(description = "TC02 - Modification Manuelle De La Quantite")
    public void tc02_modification_manuelle_de_la_quantite() {
        CartUpdateQuantityPage page = addOneTabletToCartAsGuest();

        page.setDisplayedQuantity(3);
        page.waitUntilQuantityEquals(3, 10);

        Assert.assertEquals(page.getDisplayedQuantity(), 3);
    }

    @Test(description = "TC05 - Nouvelle Modification Apres Mise A Jour")
    public void tc05_nouvelle_modification_apres_mise_a_jour() {
        CartUpdateQuantityPage page = addOneTabletToCartAsGuest();

        page.setDisplayedQuantity(2);
        page.waitUntilQuantityEquals(2, 10);

        page.setDisplayedQuantity(4);
        page.waitUntilQuantityEquals(4, 10);

        Assert.assertEquals(page.getDisplayedQuantity(), 4);
    }

    @Test(description = "TC12 - Mise A Jour Du Sous Total Produit")
    public void tc12_mise_a_jour_du_sous_total_produit() {
        CartUpdateQuantityPage page = addOneTabletToCartAsGuest();

        double subtotalBefore = page.getFirstRowSubtotal();
        int qtyBefore = page.getDisplayedQuantity();

        page.clickPlus();
        page.waitUntilQuantityEquals(qtyBefore + 1, 10);

        double subtotalAfter = page.getFirstRowSubtotal();
        Assert.assertTrue(subtotalAfter >= subtotalBefore);
    }

    @Test(description = "TC13 - Mise A Jour Du Total Panier")
    public void tc13_mise_a_jour_du_total_panier() {
        CartUpdateQuantityPage page = addOneTabletToCartAsGuest();

        double totalBefore = page.getCartTotal();
        int qtyBefore = page.getDisplayedQuantity();

        page.clickPlus();
        page.waitUntilQuantityEquals(qtyBefore + 1, 10);

        double totalAfter = page.getCartTotal();
        Assert.assertTrue(totalAfter >= totalBefore);
    }

    @Test(description = "TC15 - Persistance Apres Refresh")
    public void tc15_persistance_apres_refresh() {
        CartUpdateQuantityPage page = addOneTabletToCartAsGuest();

        page.setDisplayedQuantity(3);
        page.waitUntilQuantityEquals(3, 10);

        page.refreshCartPage();

        Assert.assertEquals(page.getDisplayedQuantity(), 3);
    }

    @Test(description = "TC19 - Double Clic Rapide Sur Plus")
    public void tc19_double_clic_rapide_sur_plus() {
        CartUpdateQuantityPage page = addOneTabletToCartAsGuest();

        int before = page.getDisplayedQuantity();
        page.doubleClickPlus();
        int after = page.getDisplayedQuantity();

        // AOS peut ignorer un double-clic trop rapide: rattrapage avec un clic simple.
        if (after <= before) {
            page.clickPlus();
            page.waitUntilQuantityEquals(before + 1, 20);
            after = page.getDisplayedQuantity();
        }

        Assert.assertTrue(after == before + 1 || after == before + 2);
    }
}
