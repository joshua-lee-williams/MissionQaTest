package mission;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static mission.BasePage.driver;

public class StepDefinition {
    private static final Logger logger = LoggerFactory.getLogger(StepDefinition.class);

    HomePage homePage = new HomePage();
    InventoryPage inventoryPage = new InventoryPage();
    ShoppingCartPage shoppingCartPage = new ShoppingCartPage();
    CheckoutPage checkoutPage = new CheckoutPage();
    CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage();

    @Given("^I am on the home page$")
    public void iAmOnTheHomePage() {
        HomePage.browseToHomePageByURL();
    }

    @Given("^I get the default list of users for on 1st page$")
    public void iGetTheDefaultListofusers() {

    }

    @Given("^I get the default list of users for on (\\d+)st page$")
    public void i_get_the_default_list_of_users_for_on_st_page(int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("I get the list of all users within every page")
    public void iGetTheListOfAllUsers() {
    }

    @Then("I should see total users count equals the number of user ids")
    public void iShouldMatchTotalCount() {

    }

    @Given("I make a search for user (.*)")
    public void iMakeASearchForUser(String sUserID) {

    }

    @Then("I should see the following user data")
    public void IShouldSeeFollowingUserData(DataTable dt) {
    }

    @Then("I receive error code (.*) in response")
    public void iReceiveErrorCodeInResponse(int responseCode) {

    }

    @Given("I create a user with following (.*) (.*)")
    public void iCreateUserWithFollowing(String sUsername, String sJob) {
    }

    @Then("response should contain the following data")
    public void iReceiveErrorCodeInResponse(DataTable dt) {

    }

    @Given("I login unsuccessfully with the following data")
    public void iLoginSuccesfullyWithFollowingData(DataTable dt) {

    }

    @Given("^I login with the following details$")
    public void i_login_with_the_following_details(DataTable arg1) throws Throwable {
        System.out.println("========== LOGIN STARTING ==========");
        System.err.println("ERROR STREAM TEST - THIS SHOULD SHOW IN RED");
        Map<String, String> loginData = arg1.asMap(String.class, String.class);
        String username = loginData.get("userName");
        String password = loginData.get("Password");
        homePage.enterUsername(username);
        homePage.enterPassword(password);
        homePage.clickLoginButton();
    }

    @Given("^I add the following items to the basket$")
    public void i_add_the_following_items_to_the_basket(DataTable arg1) throws Throwable {
        List<String> itemList = arg1.asList(String.class);
        for(String item : itemList) {
            logger.info("Attempting to add: " + item);
            inventoryPage.addItemToBasket(item);
            Thread.sleep(1000);
            logger.info("Successfully added: " + item);
        }
        Thread.sleep(10000);
    }

    @Given("^I should see (\\d+) items added to the shopping cart$")
    public void i_should_see_items_added_to_the_shopping_cart(int expectedNumberOfItems) throws Throwable {
        int actualNumberOfItemsInShoppingCart = inventoryPage.getNumberOfItemsInShoppingCart();
        Assert.assertEquals(actualNumberOfItemsInShoppingCart, expectedNumberOfItems);
    }

    @Given("^I click on the shopping cart$")
    public void i_click_on_the_shopping_cart() throws Throwable {
        inventoryPage.clickShoppingCartBadge();
    }

    @Given("^I verify that the QTY count for each item should be (\\d+)$")
    public void i_verify_that_the_QTY_count_for_each_item_should_be(int expectedQuantity) throws Throwable {
        if (shoppingCartPage.isLoaded()) {
            List<Integer> quantities = shoppingCartPage.getAllQuantities();

            System.out.println("Number of items in cart: " + quantities.size());
            System.out.println("Quantities: " + quantities);

            for (int i = 0; i < quantities.size(); i++) {
                Assert.assertEquals(quantities.get(i).intValue(), expectedQuantity,
                        "Item " + (i+1) + " quantity mismatch!");
            }

            System.out.println("All items have correct quantity: " + expectedQuantity);
        } else {
            throw new RuntimeException("Shopping Cart page did not load.");
        }
    }

    @Given("^I remove the following item:$")
    public void i_remove_the_following_item(DataTable itemsToRemove) throws Throwable {
        List<String> itemsToRemoveList = itemsToRemove.asList();
        for(String item:itemsToRemoveList) {
            shoppingCartPage.removeItemFromBasket(item);
        }
    }

    @Given("^I click on the CHECKOUT button$")
    public void i_click_on_the_CHECKOUT_button() throws Throwable {
        if (shoppingCartPage.isLoaded()) {
            shoppingCartPage.clickCheckoutButton();
        } else {
            throw new RuntimeException("Shopping Cart page did not load.");
        }
    }

    @Given("^I type \"([^\"]*)\" for First Name$")
    public void i_type_for_First_Name(String firstName) throws Throwable {
        if (checkoutPage.isLoaded()) {
            checkoutPage.enterFirstName(firstName);
        } else {
            throw new RuntimeException("Checkout page did not load.");
        }
    }

    @Given("^I type \"([^\"]*)\" for Last Name$")
    public void i_type_for_Last_Name(String lastName) throws Throwable {
        if (checkoutPage.isLoaded()) {
            checkoutPage.enterLastName(lastName);
        } else {
            throw new RuntimeException("Checkout page did not load.");
        }
    }

    @Given("^I type \"([^\"]*)\" for ZIP/Postal Code$")
    public void i_type_for_ZIP_Postal_Code(String zipcode) throws Throwable {
        if (checkoutPage.isLoaded()) {
            checkoutPage.enterZipcode(zipcode);
        } else {
            throw new RuntimeException("Checkout page did not load.");
        }
    }

    @When("^I click on the CONTINUE button$")
    public void i_click_on_the_CONTINUE_button() throws Throwable {
        if (checkoutPage.isLoaded()) {
            checkoutPage.clickContinueButton();
        } else {
            throw new RuntimeException("Checkout page did not load.");
        }
    }

    @Then("^Item total will be equal to the total of items on the list$")
    public void item_total_will_be_equal_to_the_total_of_items_on_the_list() throws Throwable {
        if (checkoutOverviewPage.isLoaded()) {
            Assert.assertTrue(checkoutOverviewPage.verifySubtotalIsCorrect());
        } else {
            throw new RuntimeException("Checkout overview page did not load.");
        }
    }

    @Then("^a Tax rate of (\\d+) % is applied to the total$")
    public void a_Tax_rate_of_is_applied_to_the_total(double taxRate) throws Throwable {
        if (checkoutOverviewPage.isLoaded()) {
            taxRate = taxRate / 100;
            Assert.assertTrue(checkoutOverviewPage.getTax() == checkoutOverviewPage.getSubtotal() * taxRate);
        } else {
            throw new RuntimeException("Checkout overview page did not load.");
        }
    }

    @Given("^I wait for the user list to load$")
    public void iWaitForUserListToLoad() {

    }

    @Then("I should see that every user has a unique id")
    public void iShouldSeeThatEveryUserHasAUniqueID() {

        // Please not that we need to check all values are unique in the list.
    }

    @Then("^I should get a response code of (\\d+)$")
    public void iShouldGetAResponseCodeOf(int responseCode) {
    }

    @And("^I should see the following response message:$")
    public void iShouldSeeTheFollowingResponseMessage() {
    }


}
