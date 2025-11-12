package mission.workflows;

import lombok.extern.log4j.Log4j2;
import mission.api.ApiClient;
import org.testng.Assert;

@Log4j2
public class GetUserListWorkflow {
    ApiClient apiClient = new ApiClient();

    public void getUsersWithDelay() {
        log.info("Fetching users with delay of 3 seconds.");
        long startTime = System.currentTimeMillis();
        apiClient.get("/api/users?delay=3");
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        log.info("Response received after: {} ms", responseTime);
        Assert.assertEquals(apiClient.getStatusCode(), 200, "Failed to load user list");
        log.info("User list loaded successfully with delay.");
    }
}
