package mission.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import mission.api.ApiClient;
import mission.context.TestContext;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

@Log4j2
public class ResponseValidator {
    ApiClient apiClient;
    TestContext testContext;

    public ResponseValidator(TestContext testContext) {
        this.testContext = testContext;
        this.apiClient = testContext.getApiClient();
    }

    public static void verifyJsonField(Response response, String expectedJson) throws JsonProcessingException {
        // Parse expected JSON
        JsonNode expected = new ObjectMapper().readTree("{" + expectedJson + "}");

        // Get actual response
        JsonNode actual = new ObjectMapper().readTree(response.asString());

        // Compare
        expected.fields().forEachRemaining(entry -> {
            String fieldName = entry.getKey();
            String expectedValue = entry.getValue().asText();
            String actualValue = actual.get(fieldName).asText();

            Assert.assertEquals(expectedValue, actualValue);
        });
    }

    public static void validateExpectedUserData(Response response, Map<String, String> expectedUserData) {
        log.info(expectedUserData.toString());
        String expectedFirstName = expectedUserData.get("first_name");
        String expectedEmail = expectedUserData.get("email");
        String actualFirstName = response.jsonPath().getString("data.first_name");
        String actualEmail = response.jsonPath().getString("data.email");
        Assert.assertEquals(expectedFirstName, actualFirstName,
                String.format("Expected first name %s does not match actual first name %s", expectedFirstName, actualFirstName));
        Assert.assertEquals(expectedEmail, actualEmail,
                String.format("Expected email %s does not match actual email %s", expectedEmail, actualEmail));
    }

    public void validateResponseContainsFields(List<String> expectedFields) {
        for (String field : expectedFields) {
            String fieldValue = testContext.getApiClient().getJsonPath(field);
            Assert.assertNotNull(fieldValue, "Field '" + field + "' not found in response");
            log.info("✓ {}: {}",  field, fieldValue);
        }
    }
}
