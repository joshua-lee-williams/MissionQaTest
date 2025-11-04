package mission;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    public static WebDriver driver;
    protected WebElement baseIdentifier;

    public void waitForPageLoad(){
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(baseIdentifier));
        } catch (Exception e) {
            throw new RuntimeException(this.getClass().getSimpleName() + " did not load! Element not found: "
                    + baseIdentifier);
        }
    }
}
