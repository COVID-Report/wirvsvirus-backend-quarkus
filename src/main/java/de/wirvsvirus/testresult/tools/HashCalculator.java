package de.wirvsvirus.testresult.tools;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDate;

@UtilityClass
public class HashCalculator {
	
	public static String calculcateId(String sampleId,String name , LocalDate birthday) {
		
		return DigestUtils.sha256Hex(sampleId+name+birthday.toString());
	}
}
