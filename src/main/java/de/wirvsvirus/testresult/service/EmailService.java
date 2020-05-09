package de.wirvsvirus.testresult.service;


import de.wirvsvirus.testresult.exception.MailSendingException;
import de.wirvsvirus.testresult.model.PushMessage;

public interface EmailService {
    void sendMail(PushMessage message) throws MailSendingException;
}
