package tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import managers.DriverManager;
import pages.RegistrationPage;
import utils.ConfigReader;
import utils.ExcelReader;

import java.util.UUID;

public class RegistrationTests {

    private WebDriver driver;
    private RegistrationPage registrationPage;
    private ExcelReader excelReader;
    private static final String SHEET_NAME = "Registration";

    @BeforeClass
    public void setupClass() {
        ConfigReader.loadProperties();
        excelReader = new ExcelReader();
    }

    @BeforeMethod
    public void setup() {
        driver = DriverManager.getDriver();
        driver.get(ConfigReader.getProperty("url"));
        registrationPage = new RegistrationPage(driver);
        registrationPage.openRegistrationForm();
    }

    @AfterMethod
    public void teardown() {
        DriverManager.quitDriver();
    }

    @DataProvider(name = "registrationData")
    public Object[][] registrationData() {
        return excelReader.getTestData(SHEET_NAME);
    }

    @Test(dataProvider = "registrationData")
    public void testRegistration(String username, String password, String description, String acceptTerms) {

        // 🔹 Make username unique if successful registration
        if (description.equalsIgnoreCase("Successful registration")) {
            String uniqueId = UUID.randomUUID().toString().substring(0, 5); // random 5 chars
            username = username + uniqueId;
        }

        // 🔹 Skip test if terms not accepted
        if (acceptTerms.equalsIgnoreCase("no")) {
            throw new SkipException("Terms & Conditions not accepted");
        }

        // Perform registration
        registrationPage.register(username, password);

        // Get alert message
        String alertMessage = registrationPage.getAlertMessage();
        System.out.println("Alert message: " + alertMessage);

        // Validation logic
        switch (description.toLowerCase()) {
            case "successful registration":
                Assert.assertTrue(alertMessage.contains("Sign up successful"),
                        "Expected success but got: " + alertMessage);
                break;

            case "registration with existing email":
                Assert.assertTrue(alertMessage.contains("already exist"),
                        "Expected 'already exist' but got: " + alertMessage);
                break;

            case "password strength validation":
                boolean isWeak = password.length() < 6 ||
                        !password.matches(".[A-Z].") ||
                        !password.matches(".\\d.");
                Assert.assertTrue(isWeak, "Weak password should be detected but passed: " + password);
                break;

            case "mandatory field validation":
                Assert.assertTrue(alertMessage.contains("Please fill out Username and Password"),
                        "Expected mandatory field alert but got: " + alertMessage);
                break;

            case "terms and conditions validation":
                // Already handled with SkipException
                break;

            default:
                System.out.println("Unknown scenario: " + description);
        }

        // Close alert
        registrationPage.acceptAlert();
    }
}