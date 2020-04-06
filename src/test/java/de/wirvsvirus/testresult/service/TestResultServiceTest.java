package de.wirvsvirus.testresult.service;

import de.wirvsvirus.testresult.database.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TestResultServiceTest {

    @ParameterizedTest
    @EnumSource(TestResult.Result.class)
    void testResultChanged_previousAndCurrentHaveSameResult(TestResult.Result status) {
        assertFalse(statusHasChanged(status, status));
    }

    @ParameterizedTest
    @EnumSource(value = TestResult.Result.class, names = {"PENDING"}, mode = EnumSource.Mode.EXCLUDE)
    void testResultChanged_fromPendingToActualResult(TestResult.Result status) {
        assertFalse(statusHasChanged(TestResult.Result.PENDING, status));
    }

    @ParameterizedTest
    @EnumSource(value = TestResult.Result.class, names = {"POSITIVE"}, mode = EnumSource.Mode.EXCLUDE)
    void testResultChanged_fromPositiveToSomethingDifferent(TestResult.Result status) {
        assertTrue(statusHasChanged(TestResult.Result.POSITIVE, status));
    }

    @ParameterizedTest
    @EnumSource(value = TestResult.Result.class, names = {"NEGATIVE"}, mode = EnumSource.Mode.EXCLUDE)
    void testResultChanged_fromNegativeToSomethingDifferent(TestResult.Result status) {
        assertTrue(statusHasChanged(TestResult.Result.NEGATIVE, status));
    }


    private boolean statusHasChanged(TestResult.Result previousResult, TestResult.Result currentResult) {
        return TestResultService.testResultChanged(testResultWithResult(previousResult), testResultWithResult(currentResult));
    }

    private TestResult testResultWithResult(TestResult.Result status) {
        TestResult testResult = new TestResult();
        testResult.setStatus(status);
        return testResult;
    }

    private TestResult testResultWithResultAndNotified(TestResult.Result status, boolean notified) {
        TestResult testResult = testResultWithResult(status);
        testResult.setNotified(notified);
        return testResult;
    }

    @ParameterizedTest
    @EnumSource(value = TestResult.Result.class, names = {"NEGATIVE"}, mode = EnumSource.Mode.EXCLUDE)
    void isNotificationRequired_nonNegativeResult(TestResult.Result status) {
        assertFalse(TestResultService.isNotificationRequired(testResultWithResultAndNotified(status, false)));
    }

    @ParameterizedTest
    @EnumSource(value = TestResult.Result.class)
    void isNotificationRequired_alreadyNotified(TestResult.Result status) {
        assertFalse(TestResultService.isNotificationRequired(testResultWithResultAndNotified(status, true)));
    }

    @Test
    void isNotificationRequired_negativeAndNotYetNotified() {
        assertTrue(TestResultService.isNotificationRequired(testResultWithResultAndNotified(TestResult.Result.NEGATIVE, false)));
    }

}