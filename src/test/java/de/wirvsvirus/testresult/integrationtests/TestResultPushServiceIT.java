package de.wirvsvirus.testresult.integrationtests;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.service.TestResultPushService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TestResultPushServiceIT extends IntegrationTestBase {

    @Inject
    protected TestResultPushService testResultPushService;

    @ParameterizedTest
    @ValueSource(strings = {"foo@bar.com", "foo@bar.email", "1@xyz.de"})
    public void testEmailRegex_ok(String validEmailAddress) {
        assertTrue(testResultPushService.isEmailAddress(validEmailAddress));
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo@bar", "foobar.email", "+4913113333", "", " ", "1"})
    public void testEmailRegex_nok(String invalidEmailAddress) {
        assertFalse(testResultPushService.isEmailAddress(invalidEmailAddress));
    }

    @ParameterizedTest
    @ValueSource(strings = {"004912112345", "+4915112334455", "016166661551"})
    public void testMobileNumberRegex_ok(String validMobileNumber) {
        assertTrue(testResultPushService.isMobileNumber(validMobileNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"004812112345", "++4915112334455", "16166661551", "foo@bar.com", "+49abcd", "1", "", " "})
    public void testMobileNumberRegex_nok(String invalidMobileNumber) {
        assertFalse(testResultPushService.isMobileNumber(invalidMobileNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"01511239876", "015 1 1 239876"})
    public void testCleanupMobileNumberRegex(String mobileNumber) {
        assertEquals("01511239876", testResultPushService.cleanupMobileNumber(mobileNumber));
    }

    @Test
    public void notificationIsSend_for_resultIsNegative_email() {
        TestResult testResult = new TestResult();
        testResult.setContact("foo@bar.com");
        testResult.setStatus(TestResult.Result.NEGATIVE);
        assertTrue(testResultPushService.executePush(testResult));
    }

    @Test
    public void notificationIsSend_for_resultIsNegative_mobileNumber() {
        TestResult testResult = new TestResult();
        testResult.setContact("+4912341414");
        testResult.setStatus(TestResult.Result.NEGATIVE);
        assertTrue(testResultPushService.executePush(testResult));
    }


    @ParameterizedTest
    @EnumSource(value = TestResult.Result.class, names = {"NEGATIVE"}, mode = EnumSource.Mode.EXCLUDE)
    public void notificationIsNotSendWhenResultIsNotNegative(TestResult.Result nonNegativeResult) {
        TestResult testResult = new TestResult();
        testResult.setContact("foo@bar.com");
        testResult.setStatus(nonNegativeResult);
        assertFalse(testResultPushService.executePush(testResult));
    }
}
