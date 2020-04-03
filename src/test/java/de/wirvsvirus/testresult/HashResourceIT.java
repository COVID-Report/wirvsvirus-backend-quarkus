package de.wirvsvirus.testresult;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class HashResourceIT extends BaseIntegrationTest {

    @Test
    public void testGetHash() {
        given()
                .queryParam("sampleId", "foo")
                .queryParam("name", "bar")
                .queryParam("birthday", "2020-12-22")
                .when()
                .get("/hashes")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("\"2669be96d4f276036f8a8c609a5f8247d9f67d65151ddd78e6b364cd75e6f24a\""));
    }
}
