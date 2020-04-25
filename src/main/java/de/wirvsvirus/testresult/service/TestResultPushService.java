package de.wirvsvirus.testresult.service;


import de.wirvsvirus.testresult.configuration.EmailConfiguration;
import de.wirvsvirus.testresult.configuration.SmsConfiguration;
import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.exception.MailSendingException;
import de.wirvsvirus.testresult.exception.SmsSendingException;
import de.wirvsvirus.testresult.model.PushMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class TestResultPushService {
    private static final String FROM_VALUE = "Krankenhaus";
    private static final String NEGATIV_TEXT = "Wir freuen uns Ihnen mitteilen zu können, dass ihr COVID-19 Testergebnis negativ ist. Der Virus konnte bei Ihnen nicht festegestellt werden.";

    @Inject
    protected EmailConfiguration emailConfiguration;

    @Inject
    protected SmsConfiguration smsConfiguration;

    @Inject
    protected SmsServiceProvider smsService;

    @Inject
    protected EmailService emailService;

    public boolean executePush(TestResult testResult) {
        if (testResult.getStatus() != TestResult.Result.NEGATIVE
                || StringUtils.isAllEmpty(testResult.getContact())) {
            // only push for negative and when a contact is configured
            return false;
        }

        String contact = testResult.getContact().trim();
        PushMessage message = createMessage();

        if (isEmailAddress(contact)) {
            return sendEmail(contact, message);
        } else if (isMobileNumber(contact)) {
            return sendSms(contact, message);
        }
        return false;
    }

    private boolean sendSms(String contact, PushMessage message) {
        try {
            message.setContact(cleanupMobileNumber(contact));
            smsService.sendNegativeResultSms(message);
            return true;
        } catch (SmsSendingException e) {
            log.debug("sms sending failed", e);
        }
        return false;
    }

    private boolean sendEmail(String contact, PushMessage message) {
        message.setContact(contact);
        try {
            emailService.sendMail(message);
            return true;
        } catch (MailSendingException e) {
            log.debug("sending mail failed", e);
        }
        return false;
    }

    public String cleanupMobileNumber(String contact) {
        return contact.replaceAll(smsConfiguration.getMobileCleanupPattern(), "");
    }

    public boolean isMobileNumber(String contact) {
        return contact.replaceAll("\\s", "").matches(smsConfiguration.getMobilePattern());
    }

    public boolean isEmailAddress(String contact) {
        return contact.matches(emailConfiguration.getEmailRegex());
    }

    private PushMessage createMessage() {
        PushMessage message = new PushMessage();
        message.setFrom(FROM_VALUE);
        message.setText(NEGATIV_TEXT);
        return message;
    }
}
