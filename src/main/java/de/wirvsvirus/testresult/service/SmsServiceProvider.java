package de.wirvsvirus.testresult.service;

import de.wirvsvirus.testresult.exception.SmsSendingException;
import de.wirvsvirus.testresult.model.PushMessage;

public interface SmsServiceProvider {
    void sendNegativeResultSms(PushMessage message) throws SmsSendingException;
}
