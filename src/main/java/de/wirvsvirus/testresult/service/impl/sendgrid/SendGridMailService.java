package de.wirvsvirus.testresult.service.impl.sendgrid;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import de.wirvsvirus.testresult.configuration.EmailConfiguration;
import de.wirvsvirus.testresult.configuration.SendGridConfiguration;
import de.wirvsvirus.testresult.exception.MailSendingException;
import de.wirvsvirus.testresult.model.PushMessage;
import de.wirvsvirus.testresult.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class SendGridMailService implements EmailService {

    private final EmailConfiguration emailConfiguration;
    private final SendGridConfiguration sendGridConfiguration;

    @Override
    public void sendMail(PushMessage message) throws MailSendingException {
        ensureApiKeyIsConfigured();

        try {
            trySendMail(buildMail(message));
        } catch (IOException e) {
            log.warn("Error sending mail", e);
            throw new MailSendingException(e);
        }
    }

    private void trySendMail(Mail mail) throws IOException {
        SendGrid sg = new SendGrid(sendGridConfiguration.getApikey());
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        log.debug("Statuscode: {}, body: {}, headers: {}", response.getStatusCode(), response.getBody(), response.getHeaders());
    }

    private Mail buildMail(PushMessage message) {
        Email from = new Email(emailConfiguration.getSender());
        String subject = emailConfiguration.getSubject();
        Email to = new Email(message.getContact());
        Content content = new Content(emailConfiguration.getContentType(), message.getText());
        return new Mail(from, subject, to, content);
    }

    private void ensureApiKeyIsConfigured() throws MailSendingException {
        if (StringUtils.isAllBlank(sendGridConfiguration.getApikey()) ||
                "unset".equalsIgnoreCase(sendGridConfiguration.getApikey())) {
            log.warn("No sendgrid apikey configured, try setting sendgrid.apikey!");
            throw new MailSendingException("mailservice not available");
        }
    }
}
