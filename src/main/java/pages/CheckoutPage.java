package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Open cart
    public void openCart() {
        click(By.id("cartur"));
        waitForSeconds(2);
    }

    // Click Place Order
    public void clickPlaceOrder() {
        click(By.xpath("//button[text()='Place Order']"));
        waitForSeconds(2);
    }

    // Fill checkout form
    public void fillCheckoutForm(String name, String country, String city,
                                 String card, String month, String year) {
        type(By.id("name"), name);
        type(By.id("country"), country);
        type(By.id("city"), city);
        type(By.id("card"), card);
        type(By.id("month"), month);
        type(By.id("year"), year);
    }

    // Click purchase
    public void clickPurchase() {
        click(By.xpath("//button[text()='Purchase']"));
        waitForSeconds(2);
    }

    // Get order confirmation message
    public String getConfirmationMessage() {
        String text = getText(By.cssSelector(".sweet-alert > h2"));
        click(By.xpath("//button[text()='OK']")); // Close dialog
        return text;
    }
}
