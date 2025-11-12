package mission.workflows;
import mission.pages.HomePage;

public class LoginWorkflow {
    private HomePage homePage;

    public LoginWorkflow() {
        this.homePage = new HomePage();
    }

    public void login(String username, String password) {
        homePage.enterUsername(username);
        homePage.enterPassword(password);
        homePage.clickLoginButton();
    }

}