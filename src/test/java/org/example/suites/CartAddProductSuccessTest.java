package org.example.suites;

import org.example.base.BaseTest;
import org.example.pages.CartAddPage;
import org.example.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartAddProductSuccessTest extends BaseTest {

    @Test(description = "TC1 - Ajouter un produit disponible au panier avec la quantité par défaut 1")
    public void TC1_ajouter_produit_quantite_defaut() {
        CartAddPage cart = new CartAddPage(driver);
        cart.addProductToCart(Constants.PRODUCT_TABLET_B, "tablets");
        Assert.assertEquals(cart.getCartCount(), 1,
                "Le compteur du panier devrait être 1 après l'ajout d'un produit avec quantité par défaut");
    }

    @Test(description = "TC2 - Vérifier que le compteur du panier est mis à jour après chaque ajout (4 fois)")
    public void TC2_compteur_panier_mis_a_jour_apres_4_ajouts() {
        CartAddPage cart = new CartAddPage(driver);
        cart.addProductToCartNTimes(Constants.PRODUCT_TABLET_B, "tablets", 4);
        Assert.assertEquals(cart.getCartCount(), 4,
                "Le compteur du panier devrait être 4 après 4 clics sur 'Add to Cart'");
    }
    @Test(description = "TC3 - Vérification du produit ajouté dans le panier")
    public void TC3_verification_produit_dans_panier() {
        CartAddPage cart = new CartAddPage(driver);
        cart.addProductToCart(Constants.PRODUCT_TABLET_B, "tablets");
        cart.goToCart();
        Assert.assertTrue(cart.isProductInCart("HP PRO TABLET 608 G1"),
                "Le produit 'HP PRO TABLET 608 G1' devrait être présent dans le panier");
        Assert.assertEquals(cart.getCartProductQuantity(), 1,
                "La quantité du produit dans le panier devrait être 1");
    }
}
