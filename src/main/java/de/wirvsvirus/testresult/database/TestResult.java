package de.wirvsvirus.testresult.database;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResult extends PanacheMongoEntity{
	
	String hash;
	Result status;
	String contact;
	boolean notified;
	public enum Result { POSITIVE, NEGATIVE, PENDING}
	
	public static TestResult findByHash(String hash) {
		return find("hash",hash).firstResult();
	}

}
