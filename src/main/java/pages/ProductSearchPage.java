package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class ProductSearchPage extends BasePage {

    private final By categoryLinks = By.cssSelector("#itemc");
    private final By productTitles = By.cssSelector(".card-title a");
    public ProductSearchPage(WebDriver driver) {
        super(driver);
    }

    public void selectCategory(String categoryName) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(categoryLinks));
        List<WebElement> cats = driver.findElements(categoryLinks);
        for (WebElement c : cats) {
            if (c.getText().trim().equalsIgnoreCase(categoryName.trim())) {
                c.click();
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productTitles));
                waitForSeconds(1);
                return;
            }
        }
    }


    public List<String> getProductNames() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productTitles));
        return driver.findElements(productTitles).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isProductPresent(String productName) {
        return getProductNames().stream()
                .anyMatch(name -> name.equalsIgnoreCase(productName.trim()));
    }

    public List<String> filterByKeyword(String keyword) {
        return getProductNames().stream()
                .filter(name -> name.toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<String> getSortedProducts() {
        return getProductNames().stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    public boolean isSortedAsc(List<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).compareToIgnoreCase(list.get(i + 1)) > 0) return false;
        }
        return true;
    }
}