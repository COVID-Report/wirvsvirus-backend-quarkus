package de.wirvsvirus.testresult.integrationtests.mocks;

import de.wirvsvirus.testresult.model.PushMessage;
import de.wirvsvirus.testresult.service.SmsServiceProvider;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class SmsServiceProviderMock implements SmsServiceProvider {

    @Override
    public void sendNegativeResultSms(PushMessage message) {
        // do nothing
    }
}
