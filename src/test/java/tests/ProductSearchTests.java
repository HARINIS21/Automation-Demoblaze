package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ProductSearchPage;
import utils.ExcelReader;

import java.util.List;

    public class ProductSearchTests extends BaseTest {

        @DataProvider(name = "productData")
        public Object[][] productData() {
            ExcelReader reader = new ExcelReader();
            return reader.getTestData("ProductSearch");
        }

        @Test(dataProvider = "productData")
        public void testProductSearch(String testType,
                                      String category,
                                      String productName,
                                      String filter,
                                      String sortBy,
                                      String expected) {

            ProductSearchPage page = new ProductSearchPage(driver);
            String tt = testType.trim().toLowerCase();

            switch (tt) {
                case "name":
                    page.selectCategory(category);
                    boolean found = page.isProductPresent(productName);

                    if ("found".equalsIgnoreCase(expected)) {
                        Assert.assertTrue(found, "Validation Failed: Product should be found -> " + productName);
                        System.out.println("PASS: Product found as expected -> " + productName);
                    } else {
                        Assert.assertFalse(found, "Validation Failed: Product should NOT be found -> " + productName);
                        System.out.println("PASS: Product not found as expected -> " + productName);
                    }
                    break;

                case "category":
                    page.selectCategory(category);
                    List<String> products = page.getProductNames();

                    Assert.assertTrue(products.size() > 0, "Validation Failed: Expected products in category -> " + category);
                    System.out.println("PASS: Category has products -> " + category);
                    break;

                case "filter":
                    page.selectCategory(category);
                    List<String> filtered = page.filterByKeyword(filter);

                    Assert.assertTrue(filtered.size() > 0, "Validation Failed: No products matched filter -> " + filter);
                    for (String p : filtered) {
                        Assert.assertTrue(p.toLowerCase().contains(filter.toLowerCase()),
                                "Validation Failed: Product does not match filter -> " + p);
                    }
                    System.out.println("PASS: All filtered products contain keyword -> " + filter);
                    break;

                case "sort":
                    page.selectCategory(category);
                    List<String> sorted = page.getSortedProducts();

                    Assert.assertTrue(page.isSortedAsc(sorted), "Validation Failed: Products are NOT sorted alphabetically");
                    System.out.println("PASS: Products are sorted alphabetically in category -> " + category);
                    break;

                case "noresults":
                    page.selectCategory(category);
                    boolean exists = page.isProductPresent(productName);

                    Assert.assertFalse(exists, "Validation Failed: Product should NOT exist -> " + productName);
                    System.out.println("PASS: No results found as expected for -> " + productName);
                    break;

                default:
                    Assert.fail("Unknown test type: " + testType);
            }
        }
    }