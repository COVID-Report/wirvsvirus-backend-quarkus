package de.wirvsvirus.testresult;

import de.wirvsvirus.testresult.database.TestResult;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.wirvsvirus.testresult.database.TestResult.Result.NEGATIVE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class TestResultResourceTest {

    public static final String TEST_RESULT_JSON_BODY = "{ \"hash\" : \"4711\", \"status\": \"PENDING\", \"contact\" : \"foo@bar.com\", \"notified\" : \"false\"}";

    @BeforeEach
    public void cleanup() {
        TestResult.deleteAll();
    }

    @Test
    public void testPostEndpointWithValidUser() {
        given()
                .body(TEST_RESULT_JSON_BODY)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("testId", "4711")
                .auth().basic("user", "userpass")
                .when()
                .post("/tests/{testId}")

                .then()
                .statusCode(204);
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
                .statusCode(401);
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
                .statusCode(401);
    }

    @Test
    public void testGetEndpoint() {
        TestResult savedResult = new TestResult();
        savedResult.setHash("f00b44");
        savedResult.setContact("foo@bar.de");
        savedResult.setStatus(NEGATIVE);
        savedResult.setNotified(false);
        savedResult.persist();

        given()
                .accept(ContentType.JSON)
                .pathParam("testId", "f00b44")

                .when()
                .get("/tests/{testId}")

                .then()
                .statusCode(200)
                .body("contact", equalTo("foo@bar.de"))
                .body("status", equalTo("NEGATIVE"));
    }

}