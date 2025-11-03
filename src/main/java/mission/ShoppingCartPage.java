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

public class ShoppingCartPage extends BasePage {

    public ShoppingCartPage() {
        PageFactory.initElements(driver, this);
        this.baseIdentifier = cartList;
    }

    @FindBy(className = "cart_list")
    private WebElement cartList;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "cart_quantity")
    private List<WebElement> cartQuantities;

    @FindBy(id = "shopping_cart_container")
    private WebElement shoppingCartIcon;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;



    /**
     * Get the number of items in the cart
     */
    public int getNumberOfItemsInCart() {
        return cartItems.size();
    }

    /**
     * Get all quantities as a list of integers
     */
    public List<Integer> getAllQuantities() {
        return cartQuantities.stream()
                .map(element -> Integer.parseInt(element.getText()))
                .collect(Collectors.toList());
    }

    public void removeItemFromBasket(String itemName) {
        String buttonId = "remove-" + itemName.toLowerCase().replace(" ", "-");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement removeFromCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id(buttonId)));

        removeFromCartButton.click();

        System.out.println("Removed from basket: " + itemName);
    }

    public void clickCheckoutButton() {
        checkoutButton.click();
    }


}