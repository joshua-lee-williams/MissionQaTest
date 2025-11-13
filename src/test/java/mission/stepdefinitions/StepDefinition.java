package mission.stepdefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import mission.api.ApiClient;
import mission.context.TestContext;
import mission.pages.CheckoutOverviewPage;
import mission.pages.CheckoutPage;
import mission.pages.HomePage;
import mission.pages.InventoryPage;
import mission.pages.ShoppingCartPage;
import mission.utils.ResponseValidator;
import mission.workflows.api.CreateUserWorkflow;
import mission.workflows.api.GetUserListWorkflow;
import mission.workflows.ui.LoginWorkflow;
import mission.workflows.ui.ShoppingWorkflow;
import org.testng.Assert;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public class StepDefinition {

    HomePage homePage = new HomePage();
    InventoryPage inventoryPage = new InventoryPage();
    ShoppingCartPage shoppingCartPage = new ShoppingCartPage();
    CheckoutPage checkoutPage = new CheckoutPage();
    CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage();
    LoginWorkflow loginWorkflow = new LoginWorkflow();
    ShoppingWorkflow shoppingWorkflow = new ShoppingWorkflow();
    private ApiClient apiClient;
    private Response response;
    private int totalUsers;
    private int totalPages;
    private List<Integer> allUserIds = new ArrayList<>();
    TestContext testContext = new TestContext();
    CreateUserWorkflow createUserWorkflow = new CreateUserWorkflow(testContext);
    GetUserListWorkflow getUserListWorkflow = new GetUserListWorkflow(testContext);
    ResponseValidator responseValidator = new ResponseValidator(testContext);

    // UI Step Definitions
    @Given("^I am on the home page$")
    public void i_am_on_the_home_page() {
        homePage.browseToHomePageByURL();
    }

    @Given("^I login with the following details$")
    public void i_login_with_the_following_details(DataTable dataTable)  {
        Map<String, String> loginData = dataTable.asMap(String.class, String.class);
        loginWorkflow.login(loginData.get("userName"), loginData.get("password"));
    }

    @Given("^I add the following items to the basket$")
    public void i_add_the_following_items_to_the_basket(DataTable dataTable)  {
        List<String> itemList = dataTable.asList(String.class);
        shoppingWorkflow.addItemsToBasket(itemList);
    }

    @Given("^I should see (\\d+) items added to the shopping cart$")
    public void i_should_see_items_added_to_the_shopping_cart(int expectedNumberOfItems)  {
        int actualNumberOfItemsInShoppingCart = inventoryPage.getNumberOfItemsInShoppingCart();
        Assert.assertEquals(actualNumberOfItemsInShoppingCart, expectedNumberOfItems,
                "Actual number of items in shopping cart does not match expected number of items.");
    }

    @Given("^I click on the shopping cart$")
    public void i_click_on_the_shopping_cart()  {
        inventoryPage.clickShoppingCartBadge();
    }

    @Given("^I verify that the QTY count for each item should be (\\d+)$")
    public void i_verify_that_the_QTY_count_for_each_item_should_be(int expectedQuantity)  {
        List<Integer> quantities = shoppingCartPage.getAllQuantities();
        shoppingWorkflow.validateShoppingCartQuantitiesMatchExpected(quantities, expectedQuantity);
    }

    @Given("^I remove the following item:$")
    public void i_remove_the_following_item(DataTable itemsToRemove)  {
        List<String> itemsToRemoveList = itemsToRemove.asList();
        shoppingWorkflow.removeItemsFromBasket(itemsToRemoveList);
    }

    @Given("^I click on the CHECKOUT button$")
    public void i_click_on_the_CHECKOUT_button()  {
        shoppingCartPage.clickCheckoutButton();
    }

    @Given("^I type \"([^\"]*)\" for First Name$")
    public void i_type_for_First_Name(String firstName)  {
        checkoutPage.enterFirstName(firstName);
    }

    @Given("^I type \"([^\"]*)\" for Last Name$")
    public void i_type_for_Last_Name(String lastName)  {
        checkoutPage.enterLastName(lastName);
    }

    @Given("^I type \"([^\"]*)\" for ZIP/Postal Code$")
    public void i_type_for_ZIP_Postal_Code(String zipcode)  {
        checkoutPage.enterZipcode(zipcode);
    }

    @When("^I click on the CONTINUE button$")
    public void i_click_on_the_CONTINUE_button()  {
        checkoutPage.clickContinueButton();
    }

    @Then("^Item total will be equal to the total of items on the list$")
    public void item_total_will_be_equal_to_the_total_of_items_on_the_list()  {
        Assert.assertTrue(shoppingWorkflow.verifySubtotalIsCorrect(), "Subtotal is not correct.");
    }

    @Then("^a Tax rate of (\\d+) % is applied to the total$")
    public void a_Tax_rate_of_is_applied_to_the_total(double taxRate)  {
        shoppingWorkflow.validateTaxRate(taxRate);
    }

    // API Step definitions
    @Given("^I wait for the user list to load$")
    public void i_wait_for_user_list_to_load()  {
        getUserListWorkflow.getUsersWithDelay();
    }

    @Then("^I should see that every user has a unique id$")
    public void i_should_see_unique_user_ids()  {
        getUserListWorkflow.validateUniqueUserIds();

    }

    @Then("^I should get a response code of (\\d+)$")
    public void iShouldGetAResponseCodeOf(int expectedResponseCode) {
        Assert.assertEquals(expectedResponseCode, response.getStatusCode());
    }

    @Then("^I should see the following response message:$")
    public void i_should_see_response_message(DataTable dataTable) throws JsonProcessingException {
        String expectedPattern = dataTable.asList(String.class).get(0);
        ResponseValidator.verifyJsonField(response, expectedPattern);
    }

    @Given("^I get the default list of users for on 1st page$")
    public void i_get_first_page_users() {
        getUserListWorkflow.getFirstPageOfUsers();
    }

    @When("^I get the list of all users within every page$")
    public void i_get_all_users_from_all_pages() {
        allUserIds = getUserListWorkflow.getAllPagesOfUsers();
    }

    @Then("^I should see total users count equals the number of user ids$")
    public void verify_total_users_equals_collected_ids() {
        Assert.assertEquals(allUserIds.size(), testContext.getTotalUsers(),
                String.format("Total users count %s does not match the number of user IDs collected %s", totalUsers, allUserIds.size()));
    }

    @Given("I make a search for user {int}")
    public void iMakeASearchForUser(int userIDToSearch) {
        log.info("Making a search for userID: " + userIDToSearch);
        response = testContext.getApiClient().get("/api/users/" + userIDToSearch);
    }

    @Then("I should see the following user data")
    public void IShouldSeeFollowingUserData(DataTable dataTable) {
        Map<String, String> expectedUserData = dataTable.asMap();
        ResponseValidator.validateExpectedUserData(testContext.getLastResponse(), expectedUserData);
    }

    @Then("I receive error code {int} in response")
    public void iReceiveErrorCodeInResponse(int expectedResponseCode) {
        int actualResponseCode = response.getStatusCode();
        Assert.assertEquals(expectedResponseCode, actualResponseCode);
    }

    @Given("^I create a user with following (.+) (.+)$")
    public void i_create_user_with_name_and_job(String name, String job)  {
        createUserWorkflow.createUserWithNameAndJob(name, job);
    }

    @Then("^response should contain the following data$")
    public void response_should_contain_data(DataTable dataTable)  {
        List<String> expectedFields = dataTable.asList(String.class);
        responseValidator.validateResponseContainsFields(expectedFields);
    }

    @Given("^I login unsuccessfully with the following data$")
    public void i_login_unsuccessfully(DataTable dataTable)  {
        // Convert DataTable to Map
        Map<String, String> loginData = dataTable.asMap(String.class, String.class);

        String email = loginData.get("Email");
        String password = loginData.getOrDefault("Password", "");
        if (password == null) { password = "";}

        log.info("=== Attempting Login ===");
        log.info("Email: {}", email);
        log.info("Password: {}", (password.isEmpty() ? "(empty)" : password));

        // Build request body
        String requestBody = "{"
                + "\"email\": \"" + email + "\"";

        // Only add password if not empty
        if (password != null && !password.isEmpty()) {
            requestBody += ",\"password\": \"" + password + "\"";
        }

        requestBody += "}";

        // Send POST request
        response = testContext.getApiClient().post("/api/login", requestBody);
    }

}
