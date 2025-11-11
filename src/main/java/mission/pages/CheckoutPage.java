package mission.pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Log4j2
public class CheckoutPage extends BasePage {

    public CheckoutPage() {
        this.baseIdentifier = firstNameField;
    }

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement zipcodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    public void enterFirstName(String firstName) {
        firstNameField.click();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.click();
        lastNameField.sendKeys(lastName);
    }

    public void enterZipcode(String zipcode) {
        zipcodeField.click();
        zipcodeField.sendKeys(zipcode);
    }

    public void clickContinueButton() {
        continueButton.click();
    }
}