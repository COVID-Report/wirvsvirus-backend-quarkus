package de.wirvsvirus.testresult.service;

import javax.enterprise.context.ApplicationScoped;

import de.wirvsvirus.testresult.database.TestResult;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class TestResultService {
	
	public TestResult getTestResult(String id) {
		return TestResult.findByHash(id);
	}
	
	
}
