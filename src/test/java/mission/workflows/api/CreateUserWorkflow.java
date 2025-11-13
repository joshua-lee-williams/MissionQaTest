package mission.workflows.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import mission.api.ApiClient;
import mission.context.TestContext;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class CreateUserWorkflow {

    ApiClient apiClient;
    TestContext testContext;

    public CreateUserWorkflow(TestContext testContext) {
        this.testContext = testContext;
        this.apiClient = testContext.getApiClient();
    }

    public void createUserWithNameAndJob(String name, String job) {
        log.info("=== Creating User ===");
        log.info("Name: {}", name);
        log.info("Job: {}", job);
        try {
            // Build JSON request body using Jackson
            Map<String, String> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("job", job);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(userData);

            // Send POST request
            testContext.getApiClient().post("/api/users", requestBody);

            // Store response for verification
            log.info("User created with status: {}", testContext.getApiClient().getStatusCode());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize user data to JSON", e);
            throw new RuntimeException("Failed to create request body", e);
        }
    }
}

