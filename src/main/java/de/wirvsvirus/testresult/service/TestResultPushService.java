package de.wirvsvirus.testresult.service;


import de.wirvsvirus.testresult.configuration.EmailConfiguration;
import de.wirvsvirus.testresult.configuration.MessageConfiguration;
import de.wirvsvirus.testresult.configuration.SmsConfiguration;
import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.exception.MailSendingException;
import de.wirvsvirus.testresult.exception.SmsSendingException;
import de.wirvsvirus.testresult.model.PushMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TestResultPushService {

    private final MessageConfiguration messageConfiguration;
    private final EmailConfiguration emailConfiguration;
    private final SmsConfiguration smsConfiguration;
    private final SmsServiceProvider smsService;
    private final EmailService emailService;

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
        message.setFrom(messageConfiguration.getFrom());
        message.setText(messageConfiguration.getText());
        return message;
    }
}
