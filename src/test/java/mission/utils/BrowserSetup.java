package mission.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import mission.pages.BasePage;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class BrowserSetup extends BasePage {

    public static String browser = null;
    private static final String CHROME_WIN = "src\\test\\java\\BrowserDirectory\\chromedriver.exe";
    private static final String EDGE = "src\\test\\java\\BrowserDirectory\\MicrosoftWebDriver.exe";
    private static final String FIREFOX_WIN = "src\\test\\java\\BrowserDirectory\\geckodriver.exe";
    private static final String CHROME_MAC = "src/test/java/BrowserDirectory/chromedriver-Mac";


    /**
     * Browser property location /src/test/java/TestData/TestData.properties
     */


    /**
     * Function for multi browser
     */
    public void selectBrowser() {
        browser = LoadProp.getProperty("Browser");

        if (browser.equalsIgnoreCase("Chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.password_manager_leak_detection", false);
            options.addArguments("start-maximized");
            options.setExperimentalOption("prefs", prefs);
            // Initialize ChromeDriver with these options
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("Firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase(("Safari"))) {
            WebDriverManager.safaridriver().setup();
            driver = new SafariDriver();
        } else if (browser.equalsIgnoreCase("chromeMac")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("chromeHeadless")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("start-maximized");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(chromeOptions);
        } else if (browser.equalsIgnoreCase("api")) {

        } else {
            Assert.fail(MessageFormat.format("Wrong Browser: {0}", browser));
        }
    }
}
