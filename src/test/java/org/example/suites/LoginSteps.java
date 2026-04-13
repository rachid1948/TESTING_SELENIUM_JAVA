package	 org.example.suites;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.pages.LoginPage;
import org.example.utils.ConfigReader;
import org.example.base.DriverFactory;
import org.example.utils.Constants;
import io.cucumber.java.Scenario;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class LoginSteps {

    private LoginPage loginPage;
    private WebDriver driver;

    public LoginSteps() {
        this.driver = DriverFactory.getDriver();
        this.loginPage = new LoginPage(driver);
    }
    
  
    @Given("je suis sur la page de connection")
    public void openLoginPage() {
        String baseUrl = Constants.BASE_URL;
        loginPage.open(baseUrl);
        loginPage.clickUserMenu();
    }

    @When("je saisis le username non valide {string}")
    public void jeSaisisUsernameNonValide(String username) {
    	loginPage.enterUsername(username);
    }
    @When("je saisis le username {string}")
    public void jeSaisisUsername(String username) {
        loginPage.enterUsername(username);
    }

    @And("je saisis le mot de passe non valide {string}")
    public void jeSaisisPasswordNonValide(String password) {
    	loginPage.enterPassword(password);
    }
    @And("je saisis le mot de passe {string}")
    public void jeSaisisPassword(String password) {
        loginPage.enterPassword(password);
    }

    @And("je clique sur le bouton Se connecter")
    public void jeCliqueSurSeConnecter() {
        loginPage.clickLogin();
    }


    @Then("un message d'erreur est affiché")
    public void verifyLoginError() {
    			// Basic check: error message element exists
		Assert.assertTrue("Error message should be displayed", loginPage.isErrorMessageDisplayed());
    }
    @Then("la connexion est réussie")
    public void verifyLoginSuccess() {
        // Basic check: page title or element exists
        Assert.assertTrue("User should be logged in", loginPage.isLoggedIn());
    }
    @Then("un message d'erreur champ obligatoire est affiché")
    public void verifyRequiredFieldError() {
    	// Basic check: error message element exists
    	Assert.assertTrue("Error message should be displayed for required fields", loginPage.isRequiredFieldErrorDisplayed());
    }
    
    //@Scenario("Login avec des credentials non valid")
    
}
