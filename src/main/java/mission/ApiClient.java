package mission;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

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
        System.out.println("=== GET Request ===");
        System.out.println("URL: " + baseUrl + endpoint);

        response = initializeRequest().get(endpoint);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * GET request with query parameters
     */
    public Response get(String endpoint, Map<String, Object> queryParams) {
        System.out.println("=== GET Request with Query Params ===");
        System.out.println("URL: " + baseUrl + endpoint);
        System.out.println("Query Params: " + queryParams);

        response = initializeRequest()
                .queryParams(queryParams)
                .get(endpoint);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * POST request with body
     */
    public Response post(String endpoint, String requestBody) {
        System.out.println("=== POST Request ===");
        System.out.println("URL: " + baseUrl + endpoint);
        System.out.println("Request Body: " + requestBody);

        response = initializeRequest()
                .body(requestBody)
                .post(endpoint);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * POST request with object (automatically converted to JSON)
     */
    public Response post(String endpoint, Object requestBody) {
        System.out.println("=== POST Request ===");
        System.out.println("URL: " + baseUrl + endpoint);

        response = initializeRequest()
                .body(requestBody)
                .post(endpoint);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * PUT request
     */
    public Response put(String endpoint, String requestBody) {
        System.out.println("=== PUT Request ===");
        System.out.println("URL: " + baseUrl + endpoint);
        System.out.println("Request Body: " + requestBody);

        response = initializeRequest()
                .body(requestBody)
                .put(endpoint);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * PUT request with object
     */
    public Response put(String endpoint, Object requestBody) {
        System.out.println("=== PUT Request ===");
        System.out.println("URL: " + baseUrl + endpoint);

        response = initializeRequest()
                .body(requestBody)
                .put(endpoint);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * PATCH request
     */
    public Response patch(String endpoint, String requestBody) {
        System.out.println("=== PATCH Request ===");
        System.out.println("URL: " + baseUrl + endpoint);
        System.out.println("Request Body: " + requestBody);

        response = initializeRequest()
                .body(requestBody)
                .patch(endpoint);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * DELETE request
     */
    public Response delete(String endpoint) {
        System.out.println("=== DELETE Request ===");
        System.out.println("URL: " + baseUrl + endpoint);

        response = initializeRequest().delete(endpoint);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        return response;
    }

    /**
     * Add custom header to the request
     */
    public ApiClient addHeader(String key, String value) {
        if (request != null) {
            request.header(key, value);
        }
        return this;
    }

    /**
     * Add multiple custom headers
     */
    public ApiClient addHeaders(Map<String, String> headers) {
        if (request != null) {
            request.headers(headers);
        }
        return this;
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
     * Get response body as string
     */
    public String getResponseBody() {
        return response.getBody().asString();
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

    /**
     * Verify status code
     */
    public boolean verifyStatusCode(int expectedStatusCode) {
        int actualStatusCode = getStatusCode();
        System.out.println("Expected Status Code: " + expectedStatusCode);
        System.out.println("Actual Status Code: " + actualStatusCode);
        return actualStatusCode == expectedStatusCode;
    }

    /**
     * Get list as an array from JSON response
     */
    public List<Object> getJsonPathList(String jsonPath) {
        return response.jsonPath().getList(jsonPath);
    }

    /**
     * Get object from JSON response
     */
    public <T> T getJsonPathObject(String jsonPath, Class<T> type) {
        return response.jsonPath().getObject(jsonPath, type);
    }
}