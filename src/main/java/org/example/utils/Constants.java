package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Constants {

    public static final String BASE_URL = "https://advantageonlineshopping.com";
    public static final int TIMEOUT = 10;

    public static final String EMPTY_MSG = "Your shopping cart is empty";

    public static final String PRODUCT_TABLET_A = "HP Elite x2 1011 G1 Tablet";
    public static final String PRODUCT_TABLET_B = "HP Pro Tablet 608 G1 Tablet";
    public static final String PRODUCT_SPEAKER_A = "Bose Soundlink Bluetooth Speaker III";
    public static final String PRODUCT_NAME_XPATH ="//*[normalize-space()='%s']";
    public static final By PRODUCTS_GRID = By.cssSelector("div.categoryRight");
    public static final String PRODUCT_CATEGORY_XPATH ="//div[@id=''%s'Img']";

    public static final By REMOVE_BTN_LOCATOR = By.xpath("//a[contains(@class,'remove')]");
    public static final By EDIT_BTN_LOCATOR = By.xpath("//a[contains(@class,'edit')]");
    public static final By CATEGORY_TABLETS_LOCATOR  = By.xpath("//div[@id='tabletsImg']");

    public static By productByName(String name) {
        return By.xpath("//*[normalize-space()='" + name + "']");
    }

    public static By productByCategory(String category) {
        return By.xpath("//div[@id='"+category+"Img']");
    }

}
