package mission;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    public static WebDriver driver;
    protected WebElement baseIdentifier;

    public boolean isLoaded(){
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(baseIdentifier));
            return true;
        } catch (Exception e) {
            System.out.println("Page did not load.  Element not found: " + baseIdentifier);
            return false;
        }
    }
}
