package tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import managers.DriverManager;
import pages.LoginPage;
import utils.ConfigReader;
import utils.ExcelReader;

public class LoginTests {

    private WebDriver driver;
    private LoginPage loginPage;
    private ExcelReader excelReader;
    private static final String SHEET_NAME = "Login";

    @BeforeClass
    public void setupClass() {
        ConfigReader.loadProperties();
        excelReader = new ExcelReader();
    }

    @BeforeMethod
    public void setup() {
        driver = DriverManager.getDriver();
        driver.get(ConfigReader.getProperty("url"));
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void teardown() {
        DriverManager.quitDriver();
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return excelReader.getTestData(SHEET_NAME);
    }

    @Test(dataProvider = "loginData")
    public void testLogin(Object usernameObj, Object passwordObj, Object descriptionObj) {

        String username = usernameObj.toString();
        String password = passwordObj.toString();
        String description = descriptionObj.toString();

        if (description.equalsIgnoreCase("Remember me functionality")) {
            System.out.println("Skipping test scenario: " + description);
            throw new SkipException("Skipping Remember Me test as not supported");
        }

        System.out.println("Running scenario: " + description);

        loginPage.login(username, password);

        boolean expectedResult;

        switch (description) {
            case "Successful login with valid credentials":
                expectedResult = true;
                break;
            default:
                expectedResult = false;
                break;
        }

        Assert.assertEquals(loginPage.isLoggedIn(username), expectedResult,
                "Test failed for scenario: " + description);
    }
}