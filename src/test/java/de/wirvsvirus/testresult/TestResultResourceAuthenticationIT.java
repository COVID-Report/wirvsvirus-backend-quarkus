package de.wirvsvirus.testresult;

import de.wirvsvirus.testresult.database.TestResult;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.wirvsvirus.testresult.database.TestResult.Result.NEGATIVE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class TestResultResourceAuthenticationIT extends BaseIntegrationTest {

    public static final String TEST_RESULT_JSON_BODY = "{ \"hash\" : \"4711\", \"status\": \"PENDING\", \"contact\" : \"foo@bar.com\", \"notified\" : \"false\"}";


    @Test
    public void testPostEndpointWithValidUser() {
        given()
                .body(TEST_RESULT_JSON_BODY)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("testId", "4711")
                .auth().basic(POST_USER, POST_USER_PASS)
                .when()
                .post("/tests/{testId}")

                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testPostEndpointWithInvalidPassword() {
        given()
                .body(TEST_RESULT_JSON_BODY)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("testId", "4711")
                .auth().basic("user", "userpass-invalid")
                .when()
                .post("/tests/{testId}")

                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void testPostEndpointWithoutAuthentication() {
        given()
                .body(TEST_RESULT_JSON_BODY)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("testId", "4711")
                .when()
                .post("/tests/{testId}")

                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}