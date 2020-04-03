package de.wirvsvirus.testresult;

import de.wirvsvirus.testresult.database.TestResult;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@QuarkusTest
class TestResultResourcePostIT extends BaseIntegrationTest {

    public static final String NEGATIVE = "NEGATIVE";
    public static final String POSITIVE = "POSITIVE";

    private final String TEST_HASH = "hash1";

    @Test
    public void testPostNegativeResult_noResultInDbYet() {

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(testResultJson(NEGATIVE))
                .pathParam("testId", TEST_HASH)
                .auth().basic(POST_USER, POST_USER_PASS)
                .when()
                .post("/tests/{testId}")

                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("hash", equalTo(TEST_HASH))
                .body("status", equalTo("NEGATIVE"))
                .body("contact",  nullValue())
                .body("notified", equalTo(true));
    }

    @Test
    public void testPostNegativeResult_negativeResultAlreadyIn() {

        saveNegativeTestResult(TestResult.Result.NEGATIVE);
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(testResultJson(NEGATIVE))
                .pathParam("testId", TEST_HASH)
                .auth().basic(POST_USER, POST_USER_PASS)
                .when()
                .post("/tests/{testId}")

                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("hash", equalTo(TEST_HASH))
                .body("status", equalTo("NEGATIVE"))
                .body("contact",  nullValue())
                .body("notified", equalTo(true));
    }

    @Test
    public void testPostNegativeResult_pendingResultAlreadyIn() {

        saveNegativeTestResult(TestResult.Result.PENDING);
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(testResultJson(NEGATIVE))
                .pathParam("testId", TEST_HASH)
                .auth().basic(POST_USER, POST_USER_PASS)
                .when()
                .post("/tests/{testId}")

                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("hash", equalTo(TEST_HASH))
                .body("status", equalTo("NEGATIVE"))
                .body("contact",  nullValue())
                .body("notified", equalTo(true));
    }
    @Test
    public void testPostPositiveResult_negativeResultAlreadyInDb() {

        saveNegativeTestResult(TestResult.Result.NEGATIVE);
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(testResultJson(POSITIVE))
                .pathParam("testId", TEST_HASH)
                .auth().basic(POST_USER, POST_USER_PASS)
                .when()
                .post("/tests/{testId}")

                .then()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    private Map<String, String> testResultJson(String status) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("status", status);
        return jsonMap;
    }


    private void saveNegativeTestResult(TestResult.Result status) {
        TestResult savedResult = new TestResult();
        savedResult.setHash(TEST_HASH);
        savedResult.setContact("foo@bar.de");
        savedResult.setStatus(status);
        savedResult.setNotified(false);
        savedResult.persist();
    }


}