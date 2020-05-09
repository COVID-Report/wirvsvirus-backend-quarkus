package de.wirvsvirus.testresult.exception;

public class SmsSendingException extends Exception{
	private static final long serialVersionUID = 1L;

	public SmsSendingException(String string) {
		super(string);
	}

	public SmsSendingException(Throwable e) {
		super(e);
	}


}
