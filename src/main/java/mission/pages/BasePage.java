package mission.pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@Log4j2
public abstract class BasePage {
    public static WebDriver driver;
    public BasePage() {
        PageFactory.initElements(driver, this);
    }
}
