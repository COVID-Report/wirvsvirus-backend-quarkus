package de.wirvsvirus.testresult.service;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.exception.FalseInformedException;
import lombok.AllArgsConstructor;

import javax.enterprise.context.ApplicationScoped;

import static de.wirvsvirus.testresult.database.TestResult.Result.NEGATIVE;
import static de.wirvsvirus.testresult.database.TestResult.Result.PENDING;

@ApplicationScoped
@AllArgsConstructor
public class TestResultService {

    public TestResult getTestResult(String id) {
        return TestResult.findByHash(id);
    }

    public TestResult updateTestResult(TestResult updatedTestResult) throws FalseInformedException {
        TestResult previousTestResult = getTestResult(updatedTestResult.getId());

        if (previousTestResult == null) {
            return persistUpdateAndNotifyIfRequired(updatedTestResult);
        } else {
            if (testResultChanged(previousTestResult, updatedTestResult)) {
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
            updatedTestResult.setNotified(true); // TODO trigger notification
        }
    }

    protected static boolean isNotificationRequired(TestResult updatedTestResult) {
        return updatedTestResult.getStatus() == NEGATIVE &&
                !updatedTestResult.isNotified();
    }
}
