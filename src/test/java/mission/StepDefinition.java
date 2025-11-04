package mission;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static mission.BasePage.driver;

public class StepDefinition {
    private static final Logger logger = LoggerFactory.getLogger(StepDefinition.class);

    HomePage homePage = new HomePage();
    InventoryPage inventoryPage = new InventoryPage();
    ShoppingCartPage shoppingCartPage = new ShoppingCartPage();
    CheckoutPage checkoutPage = new CheckoutPage();
    CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage();
    private ApiClient apiClient;
    private Response response;
    private int totalUsers;
    private int totalPages;
    private List<Integer> allUserIds = new ArrayList<>();

    private ApiClient getApiClient() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }

    @Given("^I am on the home page$")
    public void iAmOnTheHomePage() {
        HomePage.browseToHomePageByURL();
    }

    @Given("^I get the default list of users for on 1st page$")
    public void i_get_first_page_users() {
        System.out.println("=== Getting first page of users ===");

        // Get page 1
        response = getApiClient().get("/api/users?page=1");

        // Extract total users and total pages from response
        totalUsers = getApiClient().getJsonPathInt("total");
        totalPages = getApiClient().getJsonPathInt("total_pages");

        System.out.println("Total users: " + totalUsers);
        System.out.println("Total pages: " + totalPages);

        // Verify we got a successful response
        Assert.assertEquals(getApiClient().getStatusCode(), 200,
                "Failed to get first page of users");
    }

    @When("^I get the list of all users within every page$")
    public void i_get_all_users_from_all_pages() {
        System.out.println("=== Fetching all users from all pages ===");
        if (allUserIds != null) {
            allUserIds.clear(); // Clear the list before populating
        }

        // Loop through all pages
        for (int page = 1; page <= totalPages; page++) {
            System.out.println("Fetching page " + page + " of " + totalPages);

            response = apiClient.get("/api/users?page=" + page);

            // Verify successful response
            if (apiClient.getStatusCode() != 200) {
                Assert.fail("Failed to fetch page " + page);
            }

            // Extract user IDs from the data array
            List<Map<String, Object>> users = response.jsonPath().getList("data");

            for (Map<String, Object> user : users) {
                Integer userId = (Integer) user.get("id");
                allUserIds.add(userId);
                System.out.println("  User ID: " + userId +
                        ", Name: " + user.get("first_name") + " " + user.get("last_name"));
            }
        }

        System.out.println("Total user IDs collected: " + allUserIds.size());
    }

    @Then("^I should see total users count equals the number of user ids$")
    public void verify_total_users_equals_collected_ids() {
        System.out.println("=== Verification ===");
        System.out.println("Expected total users: " + totalUsers);
        System.out.println("Actual user IDs collected: " + allUserIds.size());

        Assert.assertEquals(allUserIds.size(), totalUsers,
                "Total users count does not match the number of user IDs collected!");

        System.out.println("✓ Verification passed: Total users = " + totalUsers);
    }

    @Given("I make a search for user {int}")
    public void iMakeASearchForUser(int userIDToSearch) {
        logger.info("Making a search for userID: " + userIDToSearch);
        response = getApiClient().get("/api/users/" + userIDToSearch);
    }

    @Then("I should see the following user data")
    public void IShouldSeeFollowingUserData(DataTable dt) {
        Map<String, String> expectedUserData = dt.asMap();
        System.out.println(expectedUserData);
        String expectedFirstName = expectedUserData.get("first_name");
        String expectedEmail = expectedUserData.get("email");
        String actualFirstName = response.jsonPath().getString("data.first_name");
        String actualEmail = response.jsonPath().getString("data.email");

        Assert.assertEquals(expectedFirstName, actualFirstName);
        Assert.assertEquals(expectedEmail, actualEmail);
    }

    @Then("I receive error code {int} in response")
    public void iReceiveErrorCodeInResponse(int expectedResponseCode) {
        int actualResponseCode = response.getStatusCode();
        Assert.assertEquals(expectedResponseCode, actualResponseCode);
    }

    @Given("^I create a user with following (.+) (.+)$")
    public void i_create_user_with_name_and_job(String name, String job) throws Throwable {
        System.out.println("=== Creating User ===");
        System.out.println("Name: " + name);
        System.out.println("Job: " + job);

        // Build JSON request body
        String requestBody = String.format("{\"name\": \"%s\", \"job\": \"%s\"}", name, job);

        // Send POST request
        apiClient = getApiClient();
        response = apiClient.post("/api/users", requestBody);

        // Store response for verification
        System.out.println("User created with status: " + apiClient.getStatusCode());
    }

    @Then("^response should contain the following data$")
    public void response_should_contain_data(DataTable dataTable) throws Throwable {
        List<String> expectedFields = dataTable.asList(String.class);

        for (String field : expectedFields) {
            String fieldValue = apiClient.getJsonPath(field);
            Assert.assertNotNull(fieldValue, "Field '" + field + "' not found in response");
            System.out.println("✓ " + field + ": " + fieldValue);
        }
    }

    @Given("^I login unsuccessfully with the following data$")
    public void i_login_unsuccessfully(DataTable dataTable) throws Throwable {
        // Convert DataTable to Map
        Map<String, String> loginData = dataTable.asMap(String.class, String.class);

        String email = loginData.get("Email");
        String password = loginData.getOrDefault("Password", "");
        if (password == null) { password = "";}

        System.out.println("=== Attempting Login ===");
        System.out.println("Email: " + email);
        System.out.println("Password: " + (password.isEmpty() ? "(empty)" : password));

        // Build request body
        String requestBody = "{"
                + "\"email\": \"" + email + "\"";

        // Only add password if not empty
        if (password != null && !password.isEmpty()) {
            requestBody += ",\"password\": \"" + password + "\"";
        }

        requestBody += "}";

        // Send POST request
        response = getApiClient().post("/api/login", requestBody);
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
            double expectedTaxAmount = checkoutOverviewPage.getSubtotal() * taxRate / 100;
            double actualTaxAmount = checkoutOverviewPage.getTax();
            Assert.assertTrue(Math.abs(expectedTaxAmount - actualTaxAmount) < 0.01);
        } else {
            throw new RuntimeException("Checkout overview page did not load.");
        }
    }

    @Given("^I wait for the user list to load$")
    public void i_wait_for_user_list_to_load() throws Throwable {
        System.out.println("=== Fetching Users with Delay ===");

        // ReqRes API has a delay parameter: /api/users?delay=3
        apiClient = getApiClient();

        long startTime = System.currentTimeMillis();
        response = apiClient.get("/api/users?delay=3");
        long endTime = System.currentTimeMillis();

        long responseTime = endTime - startTime;
        System.out.println("Response received after: " + responseTime + " ms");

        // Verify we got a successful response
        Assert.assertEquals(apiClient.getStatusCode(), 200,
                "Failed to load user list");

        System.out.println("✓ User list loaded successfully with delay");
    }

    @Then("^I should see that every user has a unique id$")
    public void i_should_see_unique_user_ids() throws Throwable {
        System.out.println("=== Verifying Unique User IDs ===");

        // Get all users from the response
        List<Map<String, Object>> users = response.jsonPath().getList("data");

        // Extract all user IDs
        List<Integer> userIds = new ArrayList<>();
        for (Map<String, Object> user : users) {
            Integer userId = (Integer) user.get("id");
            userIds.add(userId);
            System.out.println("User ID: " + userId +
                    ", Name: " + user.get("first_name") + " " + user.get("last_name"));
        }

        // Check for duplicates using a Set
        Set<Integer> uniqueIds = new HashSet<>(userIds);

        System.out.println("Total users: " + userIds.size());
        System.out.println("Unique IDs: " + uniqueIds.size());

        // Verify all IDs are unique
        Assert.assertEquals(userIds.size(), uniqueIds.size(),
                "Duplicate user IDs found! Some users have the same ID.");

        System.out.println("✓ All user IDs are unique");
    }

    @Then("^I should get a response code of (\\d+)$")
    public void iShouldGetAResponseCodeOf(int expectedResponseCode) {
        Assert.assertEquals(expectedResponseCode, response.getStatusCode());
    }

    @Then("^I should see the following response message:$")
    public void i_should_see_response_message(DataTable dataTable) throws Throwable {
        String expectedPattern = dataTable.asList(String.class).get(0);

        System.out.println("=== Verifying Response Message ===");
        System.out.println("Expected pattern: " + expectedPattern);

        // Parse the field name and expected value
        // Input: "error": "Missing password"
        // Output: fieldName = "error", expectedValue = "Missing password"

        String fieldName = "";
        String expectedValue = "";

        if (expectedPattern.contains(":")) {
            String[] parts = expectedPattern.split(":", 2); // Split only on first colon

            // Extract field name (remove quotes and whitespace)
            fieldName = parts[0].trim()
                    .replace("\"", "")
                    .replace("'", "")
                    .trim();

            // Extract expected value (remove quotes, commas, and whitespace)
            expectedValue = parts[1].trim()
                    .replace("\"", "")
                    .replace("'", "")
                    .replace(",", "")
                    .trim();
        } else {
            Assert.fail("Expected pattern must be in format: \"field\": \"value\"");
        }

        System.out.println("Field name: " + fieldName);
        System.out.println("Expected value: " + expectedValue);

        // Get the actual value from the JSON response using the field name
        String actualValue = apiClient.getJsonPath(fieldName);

        System.out.println("Actual value from response: " + actualValue);

        // Compare expected vs actual
        Assert.assertEquals(actualValue, expectedValue,
                "Field '" + fieldName + "' does not match! " +
                        "Expected: '" + expectedValue + "', but got: '" + actualValue + "'");

        System.out.println("✓ Field '" + fieldName + "' matches expected value: " + expectedValue);
    }


}
