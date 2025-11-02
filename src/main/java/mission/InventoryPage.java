package mission;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InventoryPage extends BasePage {

    public InventoryPage() {
        PageFactory.initElements(driver, this);
        this.baseIdentifier = inventoryList;
    }

    // Page Elements (Web Elements) - using @FindBy annotations
    @FindBy(className = "inventory_list")
    private WebElement inventoryList;

    @FindBy(className = "shopping_cart_badge")
    private WebElement shoppingCartBadge;

    // Page Methods (Actions)
    public void addItemToBasket(String itemName) {
        // Build the button ID dynamically from item name
        // Example: "Sauce Labs Backpack" → "add-to-cart-sauce-labs-backpack"
        String buttonId = "add-to-cart-" + itemName.toLowerCase().replace(" ", "-");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id(buttonId)));

        addToCartButton.click();

        System.out.println("Added to basket: " + itemName);
    }

    public int getNumberOfItemsInShoppingCart() {
        return Integer.parseInt(shoppingCartBadge.getText());
    }
}