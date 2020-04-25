package de.wirvsvirus.testresult.service;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.exception.FalseInformedException;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static de.wirvsvirus.testresult.database.TestResult.Result.NEGATIVE;
import static de.wirvsvirus.testresult.database.TestResult.Result.PENDING;

@Slf4j
@ApplicationScoped
public class TestResultService {

    @Inject
    public TestResultPushService testResultPushService;

    public TestResult getTestResult(String id) {
        return TestResult.findByHash(id);
    }

    public TestResult updateTestResult(TestResult updatedTestResult) throws FalseInformedException {
        log.debug("Updating testresult for hash {} with status {}", updatedTestResult.getId(), updatedTestResult.getStatus());
        TestResult previousTestResult = getTestResult(updatedTestResult.getId());

        if (previousTestResult == null) {
            return persistUpdateAndNotifyIfRequired(updatedTestResult);
        } else {
            if (testResultChanged(previousTestResult, updatedTestResult)) {
                log.info("New test results differs from previous test result {} <> {}",
                        previousTestResult.getStatus(), updatedTestResult.getStatus());
                throw new FalseInformedException(previousTestResult, "Patient wurde über Ergebnis NEGATIVE informiert!");
            }
            previousTestResult.setStatus(updatedTestResult.getStatus());
            previousTestResult.setContact(updatedTestResult.getContact());
            return persistUpdateAndNotifyIfRequired(previousTestResult);
        }
    }

    protected static boolean testResultChanged(TestResult previousTestResult, TestResult updatedTestResult) {
        return (previousTestResult.getStatus() != updatedTestResult.getStatus())
                && previousTestResult.getStatus() != PENDING;
    }

    private TestResult persistUpdateAndNotifyIfRequired(TestResult updatedTestResult) {
        try {
            notifyIfResultIsNegativeAndNotYetNotified(updatedTestResult);
        } finally {
            updatedTestResult.persistOrUpdate();
        }
        return updatedTestResult;
    }

    private void notifyIfResultIsNegativeAndNotYetNotified(TestResult updatedTestResult) {
        if (isNotificationRequired(updatedTestResult)) {
            updatedTestResult.setNotified(testResultPushService.executePush(updatedTestResult));
        }
    }

    protected static boolean isNotificationRequired(TestResult updatedTestResult) {
        return updatedTestResult.getStatus() == NEGATIVE &&
                !updatedTestResult.isNotified();
    }
}
