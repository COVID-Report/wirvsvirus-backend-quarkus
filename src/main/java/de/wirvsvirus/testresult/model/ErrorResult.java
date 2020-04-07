package de.wirvsvirus.testresult.model;

import de.wirvsvirus.testresult.database.TestResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ErrorResult implements Serializable {
    private static final long serialVersionUID = 1L;

    TestResult result;
    String comment;
}
