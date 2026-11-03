package com.automation.utils.api;

import com.automation.utils.config.ConfigReader;
import com.automation.utils.logging.Log;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * REST Assured configuration client with Allure logging filters and base specifications.
 */
public final class ApiClient {

    private ApiClient() {}

    public static RequestSpecification getRequestSpec() {
        String baseUri = ConfigReader.get("api.base.url", "https://jsonplaceholder.typicode.com");
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification getRequestSpec(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification getRequestSpecWithHeaders(Map<String, String> headers) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("api.base.url", "https://jsonplaceholder.typicode.com"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL);

        if (headers != null && !headers.isEmpty()) {
            builder.addHeaders(headers);
        }
        return builder.build();
    }
}
