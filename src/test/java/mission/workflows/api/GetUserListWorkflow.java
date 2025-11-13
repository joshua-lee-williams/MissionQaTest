package mission.workflows.api;

import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import mission.api.ApiClient;
import mission.context.TestContext;
import org.testng.Assert;

import java.util.*;

@Log4j2
public class GetUserListWorkflow {
    ApiClient apiClient;
    int totalUsers;
    int totalPages;
    TestContext testContext;

    public GetUserListWorkflow(TestContext testContext) {
        this.testContext = testContext;
        this.apiClient = testContext.getApiClient();
    }

    public void getFirstPageOfUsers() {
        log.info("Getting first page of users.");
        apiClient.get("/api/users?page=1");
        // Extract total users and total pages from response
        testContext.setTotalUsers(apiClient.getJsonPathInt("total"));
        testContext.setTotalPages(apiClient.getJsonPathInt("total_pages"));
        log.info("Total users: {}", testContext.getTotalUsers());
        log.info("Total pages: {}", testContext.getTotalPages());
        Assert.assertEquals(apiClient.getStatusCode(), 200,
                "Failed to get first page of users");
    }

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

    public List<Integer> getAllPagesOfUsers() {
        List<Integer> userIds = new ArrayList<>();

        log.info("Starting to fetch users from all pages");

        // Get first page to determine total pages
        Response firstPageResponse = apiClient.get("/api/users?page=1");
        int totalPages = firstPageResponse.jsonPath().getInt("total_pages");

        log.info("Total pages available: {}", totalPages);

        // Process first page
        List<Map<String, Object>> firstPageUsers = firstPageResponse.jsonPath().getList("data");
        for (Map<String, Object> user : firstPageUsers) {
            Integer userId = (Integer) user.get("id");
            userIds.add(userId);
        }

        // Process remaining pages (if any)
        for (int page = 2; page <= totalPages; page++) {
            log.info("Fetching page {} of {}", page, totalPages);

            Response response = apiClient.get("/api/users?page=" + page);
            List<Map<String, Object>> users = response.jsonPath().getList("data");

            for (Map<String, Object> user : users) {
                Integer userId = (Integer) user.get("id");
                userIds.add(userId);
            }
        }

        log.info("Collected {} total users from {} pages", userIds.size(), totalPages);
        return userIds;
    }

    public void validateUniqueUserIds() {
        log.info("=== Verifying Unique User IDs ===");

        // Get all users from the response
        List<Map<String, Object>> users = testContext.getLastResponse().jsonPath().getList("data");

        // Extract all user IDs
        List<Integer> userIds = new ArrayList<>();
        for (Map<String, Object> user : users) {
            Integer userId = (Integer) user.get("id");
            userIds.add(userId);
            log.info("User ID: {}, Name: {} {}", userId, user.get("first_name"), user.get("last_name"));
        }

        log.info("Total users: {}", userIds.size());

        // Count unique IDs using Stream distinct()
        long uniqueCount = userIds.stream().distinct().count();
        log.info("Unique IDs: {}", uniqueCount);

        // Verify all IDs are unique
        Assert.assertEquals(userIds.size(), uniqueCount,
                "Duplicate user IDs found! Some users have the same ID.");
    }
}
