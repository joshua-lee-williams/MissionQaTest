package mission.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.testng.Assert;

public class ResponseValidator {
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
}
