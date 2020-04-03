package de.wirvsvirus.testresult.exception;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.model.ErrorResult;
import lombok.Getter;

public class FalseInformedException extends Exception {

    @Getter
    private final ErrorResult errorResult;

    public FalseInformedException(TestResult result, String comment) {
        super(comment);
        this.errorResult = new ErrorResult(result, comment);
    }
}
