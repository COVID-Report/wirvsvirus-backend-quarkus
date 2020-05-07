package de.wirvsvirus.testresult.integrationtests;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.service.EmailService;
import de.wirvsvirus.testresult.service.SmsServiceProvider;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;

public class IntegrationTestBase {

    public static final String POST_USER = "user";
    public static final String POST_USER_PASS = "userpass";

    @InjectMock
    EmailService emailServiceMock;

    @InjectMock
    SmsServiceProvider smsServiceProviderMock;

    @BeforeEach
    public void cleanup() {
        TestResult.deleteAll();
    }
}
