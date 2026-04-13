package org.example.pages;

import org.example.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SignupPage extends BasePage {

    // Locators alignés 1:1 avec locators.robot
    @FindBy(id = "menuUserLink")
    private WebElement menuUserLink;

    @FindBy(css = "a.create-new-account")
    private WebElement createAccountLink;

    @FindBy(name = "usernameRegisterPage")
    private WebElement usernameInput;

    @FindBy(name = "emailRegisterPage")
    private WebElement emailInput;

    @FindBy(name = "passwordRegisterPage")
    private WebElement passwordInput;

    @FindBy(name = "confirm_passwordRegisterPage")
    private WebElement confirmPasswordInput;

    @FindBy(name = "i_agree")
    private WebElement agreeCheckbox;

    @FindBy(id = "register_btn")
    private WebElement registerButton;

    // SIGNUP_SUCCESS_INDICATOR : xpath=//span[@class='hi-user containMiniTitle ng-binding']
    @FindBy(xpath = "//span[@class='hi-user containMiniTitle ng-binding']")
    private WebElement signupSuccessIndicator;

    // SIGNUP_ERROR_MSG : css=span.regErrorLogin
    @FindBy(css = "span.regErrorLogin")
    private WebElement signupErrorMsg;

    // USERNAME_MINLEN_ERROR : xpath=//label[contains(@class,'invalid') and contains(normalize-space(.),'Use 5 character or longer')]
    @FindBy(xpath = "//label[contains(@class,'invalid') and contains(normalize-space(.),'Use 5 character or longer')]")
    private WebElement usernameMinLengthError;

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    // Navigate To Register Page
    public void navigateToRegisterPage() {
        jsClick(menuUserLink);
        wait.until(ExpectedConditions.visibilityOf(createAccountLink));
        jsClick(createAccountLink);
        wait.until(ExpectedConditions.visibilityOf(usernameInput));
    }

    // Fill Username
    public void fillUsername(String username) {
        type(usernameInput, username);
    }

    // Fill Email
    public void fillEmail(String email) {
        type(emailInput, email);
    }

    // Fill Password
    public void fillPassword(String password) {
        type(passwordInput, password);
    }

    // Fill Confirm Password + TAB → attend la checkbox
    public void fillConfirmPassword(String confirmPassword) {
        type(confirmPasswordInput, confirmPassword);
        pressEnter(confirmPasswordInput);
        wait.until(ExpectedConditions.visibilityOf(agreeCheckbox));
    }

    // Agree Terms
    public void agreeTerms() {
        if (!agreeCheckbox.isSelected()) {
            click(agreeCheckbox);
        }
    }

    // Submit Registration
    public void submitRegistration() {
        click(registerButton);
    }

    // Should See Registration Success
    // Succes = redirection hors register + affichage du username dans la barre du haut.
    public boolean isRegistrationSuccessful() {
        boolean notOnRegisterPage = !driver.getCurrentUrl().toLowerCase().contains("register");
        String indicatorText = getRegistrationIndicatorText();
        boolean hasUserText = !indicatorText.isEmpty() && !indicatorText.equalsIgnoreCase("sign in");
        return notOnRegisterPage && hasUserText;
    }

    public String getRegistrationIndicatorText() {
        // 1) Locator Robot historique (classe exacte)
        if (isElementVisible(signupSuccessIndicator, 5)) {
            String text = signupSuccessIndicator.getText().trim();
            if (!text.isEmpty()) {
                return text;
            }
        }

        // 2) Fallback UI actuelle: span utilisateur avec classes dynamiques
        for (WebElement element : driver.findElements(By.xpath("//span[contains(@class,'hi-user') or contains(@class,'containMiniTitle')]"))) {
            if (element.isDisplayed()) {
                String text = element.getText().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }

        // 3) Fallback menu user
        if (isElementVisible(menuUserLink, 5)) {
            String text = menuUserLink.getText().trim();
            if (!text.isEmpty()) {
                return text;
            }
        }

        return "";
    }

    // Should See Registration Error
    public boolean hasRegistrationError() {
        return isElementVisible(signupErrorMsg, 10);
    }

    public String getRegistrationErrorText() {
        if (!hasRegistrationError()) return "";
        return signupErrorMsg.getText().trim();
    }

    // Should Show Username Too Short Validation
    public boolean isUsernameTooShortValidationVisible() {
        return isElementVisible(usernameMinLengthError, 5);
    }

    public boolean isRegisterButtonDisabled() {
        String disabled = registerButton.getAttribute("disabled");
        String classes   = registerButton.getAttribute("class");
        return disabled != null
                || (classes != null && classes.contains("disabled"))
                || !registerButton.isEnabled();
    }

    // Should Show Password Mismatch Validation
    public boolean hasPasswordMismatchValidation() {
        if (hasRegistrationError()) return true;
        String classes = confirmPasswordInput.getAttribute("class");
        return classes != null && classes.contains("invalid");
    }

    // Helper tout-en-un
    public void registerUser(String username, String email, String password, String confirmPassword) {
        navigateToRegisterPage();
        fillUsername(username);
        fillEmail(email);
        fillPassword(password);
        fillConfirmPassword(confirmPassword);
        agreeTerms();
        submitRegistration();
    }

    // Helper interne de visibilité
    private boolean isElementVisible(WebElement element, int timeoutInSeconds) {
        try {
            wait.withTimeout(java.time.Duration.ofSeconds(timeoutInSeconds))
                    .until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
