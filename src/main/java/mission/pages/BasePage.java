package mission.pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
public abstract class BasePage {
    public static WebDriver driver;
    protected WebElement baseIdentifier;

    public BasePage() {
        PageFactory.initElements(driver, this);
    }

    public void waitForPageLoad(){
        log.info("Waiting for page {} to load.", this.getClass());
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(baseIdentifier));
        } catch (Exception e) {
            log.error("Page {} did not load successfully.", this.getClass());
            throw new RuntimeException(this.getClass().getSimpleName() + " did not load! Element not found: "
                    + baseIdentifier);
        }
        log.info("Page {} loaded successfully.", this.getClass());
    }
}
