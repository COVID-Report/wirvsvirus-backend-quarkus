package de.wirvsvirus.testresult.database;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestResult extends PanacheMongoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    String id; // NOSONAR - field has to be named id (or all class members need to be public)
    Result status;
    String contact;
    boolean notified;

    public static TestResult findByHash(String id) {
        return findById(id);
    }

    public enum Result {POSITIVE, NEGATIVE, PENDING}

}
