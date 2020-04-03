package de.wirvsvirus.testresult.model;

import de.wirvsvirus.testresult.database.TestResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    TestResult result;
    String comment;
}
