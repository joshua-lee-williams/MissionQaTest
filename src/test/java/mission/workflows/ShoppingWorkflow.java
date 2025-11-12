package mission.workflows;

import lombok.extern.log4j.Log4j2;
import mission.pages.InventoryPage;
import org.testng.Assert;

import java.util.List;

@Log4j2
public class ShoppingWorkflow {
    InventoryPage inventoryPage;

    public ShoppingWorkflow() {
        this.inventoryPage = new InventoryPage();
    }

    public void addItemsToBasket(List<String> itemList) {
        for(String item : itemList) {
            log.info("Attempting to add item to basket: {}", item);
            inventoryPage.addItemToBasket(item);
            log.info("Successfully added item to basket: {}", item);
        }
    }

    public void validateShoppingCartQuantitiesMatchExpected(List<Integer> quantities, int expectedQuantity) {
        log.info("Number of items in cart: " + quantities.size());
        log.info("Quantities: " + quantities);
        for (int i = 0; i < quantities.size(); i++) {
            Assert.assertEquals(quantities.get(i).intValue(), expectedQuantity,
                    String.format("Item %s quantity mismatch!", i+1));
        }
        log.info("All items have correct quantity: " + expectedQuantity);
    }
}
