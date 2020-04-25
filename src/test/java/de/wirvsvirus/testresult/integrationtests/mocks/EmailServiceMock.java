package de.wirvsvirus.testresult.integrationtests.mocks;

import de.wirvsvirus.testresult.model.PushMessage;
import de.wirvsvirus.testresult.service.EmailService;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class EmailServiceMock implements EmailService {

    @Override
    public void sendMail(PushMessage message)  {
        // do nothing
    }
}
