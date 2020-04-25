package de.wirvsvirus.testresult.model;

import lombok.Data;

@Data
public class PushMessage {

	private String contact;
	private String text;
	private String from;
	
}
