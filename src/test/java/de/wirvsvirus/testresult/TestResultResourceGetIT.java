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
class TestResultResourceGetIT extends BaseIntegrationTest {

    @Test
    public void testGetEndpoint() {
        savePreparedTestResult();

        given()
                .accept(ContentType.JSON)
                .pathParam("testId", "f00b44")

                .when()
                .get("/tests/{testId}")

                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("contact", equalTo("foo@bar.de"))
                .body("status", equalTo("NEGATIVE"));
    }

    private void savePreparedTestResult() {
        TestResult savedResult = new TestResult();
        savedResult.setHash("f00b44");
        savedResult.setContact("foo@bar.de");
        savedResult.setStatus(NEGATIVE);
        savedResult.setNotified(false);
        savedResult.persist();
    }

}