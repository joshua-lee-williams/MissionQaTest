package mission.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import mission.utils.LoadProp;

import java.util.List;

@Log4j2
public class ApiClient {

    private String baseUrl;
    private String apiKey;
    private Response response;
    private RequestSpecification request;

    public ApiClient() {
        this.baseUrl = "https://reqres.in";
        this.apiKey = LoadProp.getProperty("apiKey");
        RestAssured.baseURI = baseUrl;
    }

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.apiKey = LoadProp.getProperty("apiKey");
        RestAssured.baseURI = baseUrl;
    }

    /**
     * Initialize a new request with default headers
     */
    private RequestSpecification initializeRequest() {
        request = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("x-api-key", apiKey);
        return request;
    }

    /**
     * GET request
     */
    public Response get(String endpoint) {
        log.info("=== GET Request ===");
        log.info("URL: " + baseUrl + endpoint);

        response = initializeRequest().get(endpoint);

        log.info("Status Code: " + response.getStatusCode());
        log.info("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * POST request with body
     */
    public Response post(String endpoint, String requestBody) {
        log.info("=== POST Request ===");
        log.info("URL: " + baseUrl + endpoint);
        log.info("Request Body: " + requestBody);

        response = initializeRequest()
                .body(requestBody)
                .post(endpoint);

        log.info("Status Code: " + response.getStatusCode());
        log.info("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * Get the last response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Get status code from last response
     */
    public int getStatusCode() {
        return response.getStatusCode();
    }

    /**
     * Extract value from JSON response using JSON path
     */
    public String getJsonPath(String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    /**
     * Extract integer from JSON response
     */
    public int getJsonPathInt(String jsonPath) {
        return response.jsonPath().getInt(jsonPath);
    }
    
}