package tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import pages.ShoppingPage;
import utils.ConfigReader;
import utils.ExcelReader;

import java.util.List;

public class ShoppingCartTests extends BaseTest {

    private ExcelReader excelReader;
    private static final String SHEET_NAME = "ShoppingCart";

    @BeforeClass
    public void setupClass() {
        ConfigReader.loadProperties();
        excelReader = new ExcelReader();
    }

    @DataProvider(name = "cartData")
    public Object[][] cartData() {
        return excelReader.getTestData(SHEET_NAME);
    }

    @Test(dataProvider = "cartData")
    public void testShoppingCart(String testType, String category, String productName, String expected) {

        ShoppingPage shoppingPage = new ShoppingPage(driver);

        switch (testType.trim().toLowerCase()) {

            case "add":
                // Add product
                shoppingPage.selectCategory(category);
                shoppingPage.openProduct(productName);
                shoppingPage.addToCart();
                shoppingPage.openCart();

                // Validation
                List<String> products = shoppingPage.getCartProducts();
                Assert.assertTrue(
                        products.stream().anyMatch(p -> p.contains(productName)),
                        "Validation Failed: Product not added to cart -> " + productName
                );
                System.out.println("PASS: Product successfully added to cart -> " + productName);

                shoppingPage.goToHomePage();
                break;

            case "remove":
                shoppingPage.selectCategory(category);
                shoppingPage.openProduct(productName);
                shoppingPage.addToCart();
                shoppingPage.openCart();

                shoppingPage.removeFromCart(productName);
                List<String> afterRemove = shoppingPage.getCartProducts();

                Assert.assertFalse(
                        afterRemove.stream().anyMatch(p -> p.contains(productName)),
                        "Validation Failed: Product still present after removal -> " + productName
                );
                System.out.println("PASS: Product successfully removed from cart -> " + productName);

                shoppingPage.goToHomePage();
                break;

            case "update":
                throw new SkipException("Update product quantity not supported in Demoblaze");

            case "persistence":
                throw new SkipException("Cart persistence across sessions not supported in Demoblaze");

            case "price":
                shoppingPage.selectCategory(category);
                shoppingPage.openProduct(productName);
                shoppingPage.addToCart();
                shoppingPage.openCart();

                int total = shoppingPage.getTotalPrice();

                Assert.assertTrue(total > 0,
                        "Validation Failed: Cart total price is not greater than zero");
                System.out.println("PASS: Price calculation verified. Cart total -> " + total);

                shoppingPage.goToHomePage();
                break;

            default:
                Assert.fail("Unknown test type: " + testType);
        }
    }
}