package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import managers.DriverManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    private final String screenshotDir = System.getProperty("user.dir") + "/output/screenshots/";
    private final String failDir = screenshotDir + "failed/";
    private final String skipDir = screenshotDir + "skipped/";

    private void captureScreenshot(String testName, String folder) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            File dest = new File(folder + fileName);

            dest.getParentFile().mkdirs();

            File src = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(src, dest);

            System.out.println("Screenshot captured: " + dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to capture screenshot for test: " + testName);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        captureScreenshot(result.getName(), failDir);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        captureScreenshot(result.getName(), skipDir);
    }


    @Override public void onTestStart(ITestResult result) { }
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }
    @Override public void onStart(org.testng.ITestContext context) { }
    @Override public void onFinish(org.testng.ITestContext context) { }
}