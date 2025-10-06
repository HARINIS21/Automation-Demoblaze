# Selenium Automation Framework – Demoblaze E-Commerce Site

## Project Overview
This automation framework implements the **Page Object Model (POM)** design using **Selenium WebDriver** and **TestNG**.  
It supports **data-driven testing** with Excel, **multi-browser execution**, and **screenshot-based reporting** on test failures.  
The framework automates key functionalities of the **Demoblaze E-Commerce** application, including login, registration, product search, cart management, and checkout flows.

---

## Framework Architecture

### **1. Design Highlights**
| Feature | Description |
|----------|-------------|
| **Page Object Model** | Improves maintainability and reusability through structured page classes. |
| **TestNG Integration** | Enables parameterization, parallel execution, and detailed HTML reports. |
| **Data-Driven Testing** | Uses Excel file for scalable test input and validation data. |
| **Multi-Browser Support** | Supports Chrome and Firefox execution, configurable through properties. |
| **Listeners & Reporting** | Captures screenshots on failures, logs execution results, and generates reports. |

---

##  Project Structure

```
selenium-pom-framework/
│
├── src/
│   ├── main/java/
│   │   ├── pages/              # Page classes (LoginPage, HomePage, CartPage, etc.)
│   │   ├── managers/           # DriverManager, ConfigReader
│   │   ├── utils/              # Helpers (ExcelReader, WaitUtils, ScreenshotUtils)
│   │   └── listeners/          # TestNG listeners for reporting
│   │
│   └── test/java/
│       ├── tests/              # Test classes (LoginTests, CartTests, CheckoutTests, etc.)
│       ├── suites/             # Test suites (optional grouping)
│
├── resources/
│   ├── config.properties       # Browser, URL, timeout configurations
│   ├── testdata.xlsx           # Excel-based test data
│   └── testng.xml              # TestNG suite configuration
│
└── output/
    ├── test-output/            # TestNG HTML reports
    ├── screenshots/            # Captured screenshots on failure
    └── logs/                   # Execution logs
```

---

##  Configuration

### **config.properties**
```properties
browser=chrome
url=https://www.demoblaze.com
implicit.wait=10
explicit.wait=20
```

### **testng.xml**
```xml
<suite name="Demoblaze Automation Suite" parallel="tests" thread-count="2">
    <test name="Chrome Tests">
        <parameter name="browser" value="chrome"/>
        <classes>
            <class name="tests.LoginTests"/>
            <class name="tests.SearchTests"/>
            <class name="tests.CartTests"/>
            <class name="tests.CheckoutTests"/>
        </classes>
    </test>

    <test name="Firefox Tests">
        <parameter name="browser" value="firefox"/>
        <classes>
            <class name="tests.LoginTests"/>
            <class name="tests.SearchTests"/>
            <class name="tests.CartTests"/>
            <class name="tests.CheckoutTests"/>
        </classes>
    </test>
</suite>
```

---

##  Core Components
### **1. Driver Management**
Handles WebDriver initialization and teardown:
```java
public class DriverManager {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = ConfigReader.getProperty("browser");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }
        return driver;
    }
}
```

### **2. ConfigReader**
Utility for reading `config.properties`:
```java
public class ConfigReader {
    private static Properties prop = new Properties();
    static {
        FileInputStream file = new FileInputStream("resources/config.properties");
        prop.load(file);
    }
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
```

### **3. ExcelReader**
Reads test data dynamically from Excel:
```java
public class ExcelReader {
    public Object[][] getTestData(String sheetName) {
        // Apache POI implementation to read Excel file and return Object[][]
    }
}
```

### **4. TestNG Listener**
Captures screenshots on test failures:
```java
public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        ScreenshotUtils.capture(result.getName());
    }
}
```

---

##  Test Coverage (25+ Cases)

| Module | Sample Test Scenarios |
|---------|------------------------|
| **Login** | Valid login, Invalid username/password, Empty credentials, Remember Me |
| **Registration** | New user registration, Existing email validation, Field validations |
| **Product Search** | Search by product, category, filters, sorting, no results |
| **Shopping Cart** | Add/remove product, quantity update, persistence, price check |
| **Checkout** | Guest checkout, registered checkout, address validation, order confirmation |

---

## Execution Steps

1. Clone the project.  
2. Update `config.properties` with browser and URL.  
3. Place your test data in `resources/testdata.xlsx`.  
4. Run using TestNG:
   - Via IntelliJ: Right-click `testng.xml` → **Run**
   - Via CLI:
     ```bash
     mvn clean test
     ```

---

##  Reporting & Output

- **TestNG HTML Reports:** Generated under `test-output/index.html`.  
- **Screenshots:** Captured in `/output/screenshots` for failed tests and skipped tests.  
- **Logs:** Execution details stored under `/output/logs`.

---

## 💡Future Enhancements
- Integrate **ExtentReports** for advanced reporting.
- Add **Jenkins CI/CD pipeline** for automated execution.
- Extend coverage with **API and DB validations**.

---

## Conclusion
This Selenium framework demonstrates **end-to-end automation** for Demoblaze, combining:
- Clean POM architecture  
- Config-driven flexibility  
- Data-driven testing  
- Multi-browser scalability  
- Automated reporting  
