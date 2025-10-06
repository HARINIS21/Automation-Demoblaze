package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Login link on homepage (opens modal)
    @FindBy(id = "login2")
    private WebElement loginMenuButton;

    // Login modal fields
    @FindBy(id = "loginusername")
    private WebElement usernameField;

    @FindBy(id = "loginpassword")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Log in')]")
    private WebElement loginButton;

    // Element displayed when user is logged in
    @FindBy(id = "nameofuser")
    private WebElement loggedInUser;

    // Open login modal
    public void openLoginModal() {
        loginMenuButton.click();
        wait.until(ExpectedConditions.visibilityOf(usernameField));
    }

    // Perform login
    public void login(String username, String password) {
        openLoginModal();
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
        loginButton.click();
    }

    // Verify login success
    public boolean isLoggedIn(String expectedUsername) {
        try {
            wait.until(ExpectedConditions.visibilityOf(loggedInUser));
            return loggedInUser.getText().contains(expectedUsername);
        } catch (Exception e) {
            return false;
        }
    }
}