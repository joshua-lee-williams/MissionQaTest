package mission;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import java.util.Map;

public class StepDefinition {

    HomePage homePage = new HomePage();

    @Given("^I am on the home page$")
    public void iAmOnTheHomePage() {
        HomePage.homePage();
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

    @Given("^I login in with the following details$")
    public void i_login_in_with_the_following_details(DataTable arg1) throws Throwable {
        Map<String, String> loginData = arg1.asMap(String.class, String.class);
        String username = loginData.get("userName");
        String password = loginData.get("Password");
        homePage.enterUsername(username);
        homePage.enterPassword(password);
        homePage.clickLoginButton();


        throw new PendingException();
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
