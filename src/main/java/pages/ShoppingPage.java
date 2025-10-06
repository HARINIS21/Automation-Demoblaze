package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class ShoppingPage extends BasePage {

    public ShoppingPage(WebDriver driver) {
        super(driver);
    }

    public void selectCategory(String categoryName) {
        By categoryLocator = By.cssSelector("#itemc");
        waitForElementVisible(categoryLocator, 20);

        List<WebElement> categories = driver.findElements(categoryLocator);
        for (WebElement c : categories) {
            if (c.getText().trim().equalsIgnoreCase(categoryName.trim())) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", c);
                waitForSeconds(2);
                return;
            }
        }
        throw new RuntimeException("Category not found: " + categoryName);
    }

    public void openProduct(String productName) {
        By productLocator = By.linkText(productName);
        waitForElementClickable(productLocator, 20);
        driver.findElement(productLocator).click();
        waitForSeconds(2);
    }

    public void addToCart() {
        By addToCartBtn = By.xpath("//a[text()='Add to cart']");
        waitForElementClickable(addToCartBtn, 20);
        click(addToCartBtn);
        waitForSeconds(2);

        // Accept the alert
        driver.switchTo().alert().accept();
    }

    public void goToHomePage() {
        By homeBtn = By.xpath("//a[text()='Home ']");
        waitForElementClickable(homeBtn, 20);
        click(homeBtn);
        waitForSeconds(2);
    }

    public void openCart() {
        By cartBtn = By.id("cartur");
        waitForElementClickable(cartBtn, 20);
        click(cartBtn);
        waitForSeconds(2);
    }

    public void removeFromCart(String productName) {
        By rows = By.cssSelector("#tbodyid tr");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rows));

        List<WebElement> cartRows = driver.findElements(rows);
        for (WebElement row : cartRows) {
            if (row.getText().contains(productName)) {
                row.findElement(By.linkText("Delete")).click();
                waitForSeconds(2);
                return;
            }
        }
        throw new RuntimeException("Product not found in cart: " + productName);
    }

    public List<String> getCartProducts() {
        By rows = By.cssSelector("#tbodyid tr");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rows));

        List<String> products = new ArrayList<>();
        for (WebElement row : driver.findElements(rows)) {
            products.add(row.getText());
        }
        return products;
    }

    public int getTotalPrice() {
        By totalLocator = By.id("totalp");
        wait.until(ExpectedConditions.visibilityOfElementLocated(totalLocator));
        return Integer.parseInt(driver.findElement(totalLocator).getText());
    }
}