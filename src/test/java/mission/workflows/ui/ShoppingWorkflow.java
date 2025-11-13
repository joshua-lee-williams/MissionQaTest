package mission.workflows.ui;

import lombok.extern.log4j.Log4j2;
import mission.pages.CheckoutOverviewPage;
import mission.pages.InventoryPage;
import mission.pages.ShoppingCartPage;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Log4j2
public class ShoppingWorkflow {
    InventoryPage inventoryPage;
    ShoppingCartPage shoppingCartPage;
    CheckoutOverviewPage checkoutOverviewPage;

    public ShoppingWorkflow() {
        this.checkoutOverviewPage = new CheckoutOverviewPage();
        this.inventoryPage = new InventoryPage();
        this.shoppingCartPage = new ShoppingCartPage();
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

    public void removeItemsFromBasket(List<String> itemsToRemoveList) {
        for(String item:itemsToRemoveList) {
            log.info("Removing item {} from basket.", item);
            shoppingCartPage.removeItemFromBasket(item);
            log.info("Successfully removed item {} from basket.", item);
        }
    }

    public boolean verifySubtotalIsCorrect() {
        BigDecimal calculatedTotal = new BigDecimal(checkoutOverviewPage.calculateItemsTotal());
        BigDecimal displayedSubtotal = new BigDecimal(checkoutOverviewPage.getSubtotal());
        boolean matches = (calculatedTotal.compareTo(displayedSubtotal) == 0);
        return matches;
    }

    public void validateTaxRate(double taxRate) {
        BigDecimal subtotal = new BigDecimal(String.valueOf(checkoutOverviewPage.getSubtotal()));
        BigDecimal rate = new BigDecimal(String.valueOf(taxRate));
        BigDecimal actualTaxAmount = new BigDecimal(String.valueOf(checkoutOverviewPage.getTax()));
        BigDecimal expectedTaxAmount = subtotal.multiply(rate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        Assert.assertTrue((expectedTaxAmount.compareTo(actualTaxAmount) == 0),
                String.format("Expected tax amount %s does not match actual tax amount %s.",
                        expectedTaxAmount, actualTaxAmount));
    }
}
