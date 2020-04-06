package de.wirvsvirus.testresult.integrationtests;

import de.wirvsvirus.testresult.database.TestResult;
import org.junit.jupiter.api.BeforeEach;

public class IntegrationTestBase {

    public static final String POST_USER = "user";
    public static final String POST_USER_PASS = "userpass";

    @BeforeEach
    public void cleanup() {
        TestResult.deleteAll();
    }
}
