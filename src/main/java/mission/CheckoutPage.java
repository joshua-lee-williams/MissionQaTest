package mission;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutPage extends BasePage {

    public CheckoutPage() {
        PageFactory.initElements(driver, this);
        this.baseIdentifier = firstNameField;
    }

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    public void enterFirstName(String firstName) {
        firstNameField.click();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.click();
        lastNameField.sendKeys(lastName);
    }
}