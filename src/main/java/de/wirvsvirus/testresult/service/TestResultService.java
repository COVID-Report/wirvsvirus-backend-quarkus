package de.wirvsvirus.testresult.service;

import javax.enterprise.context.ApplicationScoped;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.exception.FalseInformedException;
import lombok.AllArgsConstructor;

import static de.wirvsvirus.testresult.database.TestResult.Result.NEGATIVE;
import static de.wirvsvirus.testresult.database.TestResult.Result.PENDING;

@ApplicationScoped
@AllArgsConstructor
public class TestResultService {

	public TestResult getTestResult(String id) {
        return TestResult.findByHash(id);
    }

    public TestResult updateTestResult(TestResult updatedTestResult) throws FalseInformedException {
        TestResult previousTestResult = getTestResult(updatedTestResult.getHash());

        if (previousTestResult == null) {
			return persistUpdateAndNotifyIfRequired(updatedTestResult);
		}
        else {
            if (testResultChanged(updatedTestResult, previousTestResult)) {
                throw new FalseInformedException(previousTestResult, "Patient wurde über Ergebnis NEGATIVE informiert!");
            }
            previousTestResult.setStatus(updatedTestResult.getStatus());
            previousTestResult.setContact(updatedTestResult.getContact());
			return persistUpdateAndNotifyIfRequired(previousTestResult);
		}
	}

	private boolean testResultChanged(TestResult updatedTestResult, TestResult previousTestResult) {
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
		// trigger notification
		if (updatedTestResult.getStatus() == NEGATIVE &&
				!updatedTestResult.isNotified()) {
			updatedTestResult.setNotified(true); // TODO trigger notification
		}
	}
}
