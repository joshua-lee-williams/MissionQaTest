package mission.context;

import lombok.Getter;
import lombok.Setter;
import mission.api.ApiClient;
import mission.workflows.api.GetUserListWorkflow;
import mission.workflows.ui.LoginWorkflow;
import org.openqa.selenium.WebDriver;
import io.restassured.response.Response;
import java.util.List;

public class TestContext {
    private ApiClient apiClient;

    public TestContext() {
        this.apiClient = new ApiClient(this);
    }

    @Setter
    @Getter
    private Response lastResponse;
    @Setter
    @Getter
    private List<Integer> allUserIds;
    @Setter
    @Getter
    private int totalUsers;
    @Setter
    @Getter
    private int totalPages;

    public ApiClient getApiClient() {
        return this.apiClient;
    }
}