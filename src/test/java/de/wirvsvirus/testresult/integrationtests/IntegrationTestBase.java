package de.wirvsvirus.testresult.integrationtests;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.service.EmailService;
import de.wirvsvirus.testresult.service.SmsServiceProvider;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;

public class IntegrationTestBase {

    public static final String POST_USER = "user";
    public static final String POST_USER_PASS = "userpass";

    @Inject
    EmailService emailServiceMock;

    @Inject
    SmsServiceProvider smsServiceProviderMock;

    @BeforeEach
    public void cleanup() {
        TestResult.deleteAll();
    }
}
