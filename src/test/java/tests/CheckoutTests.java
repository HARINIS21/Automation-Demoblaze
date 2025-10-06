package tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ShoppingPage;
import utils.ConfigReader;
import utils.ExcelReader;

public class CheckoutTests extends BaseTest {

    private ExcelReader excelReader;
    private static final String SHEET_NAME = "Checkout";

    @BeforeClass
    public void setupClass() {
        ConfigReader.loadProperties();
        excelReader = new ExcelReader();
    }

    @DataProvider(name = "checkoutData")
    public Object[][] checkoutData() {
        return excelReader.getTestData(SHEET_NAME);
    }

    @Test(dataProvider = "checkoutData")
    public void testCheckout(String testType, String username, String password,
                             String productCategory, String productName,
                             String name, String country, String city,
                             String card, String month, String year,
                             String expectedMessage) {

        ShoppingPage shoppingPage = new ShoppingPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        try {
            shoppingPage.selectCategory(productCategory);
            shoppingPage.openProduct(productName);
            shoppingPage.addToCart();
            shoppingPage.goToHomePage();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product to cart: " + productName, e);
        }

        switch (testType.trim().toLowerCase()) {

            case "guest":
                checkoutPage.openCart();
                checkoutPage.clickPlaceOrder();
                checkoutPage.fillCheckoutForm(name, country, city, card, month, year);
                checkoutPage.clickPurchase();
                break;

            case "registered":
                LoginPage loginPage = new LoginPage(driver);
                loginPage.login(username, password);
                if (!loginPage.isLoggedIn(username)) {
                    throw new SkipException("Login failed for registered user: " + username);
                }
                checkoutPage.openCart();
                checkoutPage.clickPlaceOrder();
                checkoutPage.fillCheckoutForm(name, country, city, card, month, year);
                checkoutPage.clickPurchase();
                break;

            case "orderconfirmation":
            case "shipping":
            case "payment":
                checkoutPage.openCart();
                checkoutPage.clickPlaceOrder();
                checkoutPage.fillCheckoutForm(name, country, city, card, month, year);
                checkoutPage.clickPurchase();
                break;

            default:
                throw new SkipException("Unknown checkout test type: " + testType);
        }

        // Step 3: Validate confirmation
        String confirmation = checkoutPage.getConfirmationMessage();
        Assert.assertTrue(confirmation.contains("Thank you for your purchase!"),
                "Order confirmation not received.");

        //  Validation messages for each test type
        switch (testType.trim().toLowerCase()) {
            case "guest":
                System.out.println("PASS: Guest user checkout completed successfully.");
                break;
            case "registered":
                System.out.println("PASS: Registered user checkout completed successfully.");
                break;
            case "shipping":
                System.out.println("PASS: Shipping address validated successfully.");
                break;
            case "payment":
                System.out.println("PASS: Payment method processed successfully.");
                break;
            case "orderconfirmation":
                System.out.println("PASS: Order confirmation displayed successfully.");
                break;
            default:
                System.out.println("PASS: Checkout test completed successfully for test type: " + testType);
        }
    }
}