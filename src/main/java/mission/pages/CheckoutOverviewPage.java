package mission.pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Log4j2
public class CheckoutOverviewPage extends BasePage {

    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPrices;

    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(className = "summary_info")
    private WebElement summaryInfo;

    public CheckoutOverviewPage() {
        this.baseIdentifier = summaryInfo;
    }

    /**
     * Extract numeric price from text like "$29.99" or "Item total: $79.96"
     */
    private double extractPrice(String priceText) {
        // Remove all non-numeric characters except decimal point
        String cleaned = priceText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(cleaned);
    }

    /**
     * Calculate sum of all item prices
     */
    public double calculateItemsTotal() {
        double total = 0.0;
        log.info("=== Calculating Items Total ===");
        for (WebElement priceElement : itemPrices) {
            String priceText = priceElement.getText();
            double price = extractPrice(priceText);
            log.info("Item price: ${}", price);
            total += price;
        }
        log.info("Calculated total: ${}", total);
        return total;
    }

    /**
     * Get the displayed subtotal from the summary
     */
    public double getSubtotal() {
        String subtotalText = subtotalLabel.getText(); // "Item total: $79.96"
        double subtotal = extractPrice(subtotalText);
        log.info("Displayed subtotal: {} = actual subtotal: {}", subtotalText, subtotal);
        return subtotal;
    }

    /**
     * Get the displayed tax
     */
    public double getTax() {
        String taxText = taxLabel.getText(); // "Tax: $6.40"
        double tax = extractPrice(taxText);
        log.info("Displayed tax: " + taxText + " = $" + tax);
        return tax;
    }

    /**
     * Get the displayed total
     */
    public double getTotal() {
        String totalText = totalLabel.getText(); // "Total: $86.36"
        double total = extractPrice(totalText);
        log.info("Displayed total: " + totalText + " = $" + total);
        return total;
    }

    /**
     * Verify subtotal = sum of item prices
     */
    public boolean verifySubtotalIsCorrect() {
        double calculatedTotal = calculateItemsTotal();
        double displayedSubtotal = getSubtotal();

        boolean matches = Math.abs(calculatedTotal - displayedSubtotal) < 0.01;
        log.info("Subtotal match: " + matches);
        return matches;
    }

    /**
     * Verify that total = subtotal + tax
     */
    public boolean verifyTotalIsCorrect() {
        double subtotal = getSubtotal();
        double tax = getTax();
        double calculatedTotal = subtotal + tax;
        double displayedTotal = getTotal();

        log.info("=== Verifying Total ===");
        log.info("Subtotal: ${}", subtotal);
        log.info("Tax: ${}", tax);
        log.info("Calculated (subtotal + tax): ${}", String.format("%.2f", calculatedTotal));
        log.info("Displayed total: ${}", displayedTotal);

        boolean matches = Math.abs(calculatedTotal - displayedTotal) < 0.01;
        log.info("Total match: {}", matches);
        return matches;
    }

    /**
     * Verify tax rate percentage
     */
    public boolean verifyTaxRate(double expectedTaxRate) {
        double subtotal = getSubtotal();
        double tax = getTax();
        double calculatedTax = subtotal * (expectedTaxRate / 100.0);

        log.info("=== Verifying Tax Rate ===");
        log.info("Expected tax ({}%): ${}", expectedTaxRate, String.format("%.2f", calculatedTax));
        log.info("Actual tax: ${}", tax);

        boolean matches = Math.abs(calculatedTax - tax) < 0.01;
        log.info("Tax rate match: {}", matches);
        return matches;
    }
}