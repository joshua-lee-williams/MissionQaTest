package mission.workflows.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import mission.api.ApiClient;
import mission.context.TestContext;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ApiLoginWorkflow {
    ApiClient apiClient;
    TestContext testContext;

    public ApiLoginWorkflow(TestContext testContext) {
        this.testContext = testContext;
        this.apiClient = testContext.getApiClient();
    }

    public void loginUnsuccessfully(String email, String password) {
        if (password == null) { password = "";}
        log.info("Attempting Unsuccessful Login");
        log.info("Email: {}", email);
        log.info("Password: {}", (password.isEmpty() ? "(empty)" : password));
        try {
            // Build request body using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("email", email);
            // Only add password if not empty
            if (!password.isEmpty()) {
                loginRequest.put("password", password);
            }
            String requestBody = objectMapper.writeValueAsString(loginRequest);
            testContext.getApiClient().post("/api/login", requestBody);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize login request to JSON", e);
            throw new RuntimeException("Failed to create login request body", e);
        }
    }
}
