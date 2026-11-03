package com.automation.utils.api;

import com.automation.utils.logging.Log;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Convenience wrapper for HTTP actions (GET, POST, PUT, DELETE, PATCH).
 */
public final class RestUtils {

    private RestUtils() {}

    public static Response get(String endpoint) {
        Log.info("Executing GET request to: %s", endpoint);
        return RestAssured.given()
                .spec(ApiClient.getRequestSpec())
                .when()
                .get(endpoint);
    }

    public static Response get(String endpoint, Map<String, Object> queryParams) {
        Log.info("Executing GET request to: %s with queryParams: %s", endpoint, queryParams);
        return RestAssured.given()
                .spec(ApiClient.getRequestSpec())
                .queryParams(queryParams)
                .when()
                .get(endpoint);
    }

    public static Response post(String endpoint, Object body) {
        Log.info("Executing POST request to: %s", endpoint);
        return RestAssured.given()
                .spec(ApiClient.getRequestSpec())
                .body(body)
                .when()
                .post(endpoint);
    }

    public static Response put(String endpoint, Object body) {
        Log.info("Executing PUT request to: %s", endpoint);
        return RestAssured.given()
                .spec(ApiClient.getRequestSpec())
                .body(body)
                .when()
                .put(endpoint);
    }

    public static Response delete(String endpoint) {
        Log.info("Executing DELETE request to: %s", endpoint);
        return RestAssured.given()
                .spec(ApiClient.getRequestSpec())
                .when()
                .delete(endpoint);
    }

    public static Response patch(String endpoint, Object body) {
        Log.info("Executing PATCH request to: %s", endpoint);
        return RestAssured.given()
                .spec(ApiClient.getRequestSpec())
                .body(body)
                .when()
                .patch(endpoint);
    }
}
