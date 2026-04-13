package org.example.suites;

import org.example.base.BaseTest;
import org.example.pages.CartDeletePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartDeleteProductFailedTest extends BaseTest {
    @Test(description = "TC5_Remove panier vide")
    public void TC5_remove_panier_vide() {
        CartDeletePage cart = new CartDeletePage(driver);
        //cart.ensureCartIsEmpty();
        cart.goToCart();
        Assert.assertTrue(cart.isCartEmpty());
        Assert.assertTrue(cart.isRemoveBtnNotPresent());
    }

    @Test(description = "TC6_Edit panier vide")
    public void TC6_edit_panier_vide() {
        CartDeletePage cart = new CartDeletePage(driver);
        //cart.ensureCartIsEmpty();
        cart.goToCart();
        Assert.assertTrue(cart.isCartEmpty());
        Assert.assertTrue(cart.isEditBtnNotPresent());
    }

}
