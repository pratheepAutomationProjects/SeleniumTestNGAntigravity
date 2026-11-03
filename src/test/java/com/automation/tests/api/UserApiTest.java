package com.automation.tests.api;

import com.automation.tests.base.BaseApiTest;
import com.automation.utils.api.RestUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("API Automation")
@Feature("User Management Service")
public class UserApiTest extends BaseApiTest {

    @Test(description = "Verify GET /users returns HTTP 200 and a list of users", groups = {"smoke", "regression", "api"})
    @Severity(SeverityLevel.BLOCKER)
    @Story("Retrieve list of registered users")
    public void testGetUsersList() {
        Response response = RestUtils.get("/users");

        response.then()
                .statusCode(200)
                .body("[0].id", notNullValue())
                .body("[0].email", notNullValue());

        assertThat(response.jsonPath().getList("$").size())
                .as("User list should not be empty")
                .isGreaterThan(0);
    }

    @Test(description = "Verify GET /users/{id} retrieves specific user details", groups = {"regression", "api"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Retrieve single user by ID")
    public void testGetSingleUser() {
        Response response = RestUtils.get("/users/1");

        response.then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", notNullValue());

        String username = response.jsonPath().getString("username");
        assertThat(username).as("Username should not be blank").isNotBlank();
    }

    @Test(description = "Verify POST /users creates a new user record", groups = {"smoke", "regression", "api"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create new user")
    public void testCreateUser() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "John Doe");
        payload.put("username", "johndoe");
        payload.put("email", "john.doe@example.com");

        Response response = RestUtils.post("/users", payload);

        response.then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("John Doe"));

        assertThat(response.jsonPath().getString("email"))
                .as("Created email matches payload")
                .isEqualTo("john.doe@example.com");
    }

    @Test(description = "Verify PUT /users/{id} updates user information", groups = {"regression", "api"})
    @Severity(SeverityLevel.NORMAL)
    @Story("Update user details")
    public void testUpdateUser() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "John Updated");
        payload.put("email", "updated.john@example.com");

        Response response = RestUtils.put("/users/1", payload);

        response.then()
                .statusCode(200)
                .body("name", equalTo("John Updated"));
    }

    @Test(description = "Verify DELETE /users/{id} removes a user", groups = {"regression", "api"})
    @Severity(SeverityLevel.NORMAL)
    @Story("Delete user by ID")
    public void testDeleteUser() {
        Response response = RestUtils.delete("/users/1");

        response.then()
                .statusCode(200);
    }
}
