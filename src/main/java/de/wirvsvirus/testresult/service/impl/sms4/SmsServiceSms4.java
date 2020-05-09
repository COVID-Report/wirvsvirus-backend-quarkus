package de.wirvsvirus.testresult.service.impl.sms4;


import de.wirvsvirus.testresult.configuration.Sms4Configuration;
import de.wirvsvirus.testresult.exception.SmsSendingException;
import de.wirvsvirus.testresult.model.PushMessage;
import de.wirvsvirus.testresult.service.SmsServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

/**
 * Using https://www.sms4.de/cgi-bin/sms_http.pl
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SmsServiceSms4 implements SmsServiceProvider {

    private static final OkHttpClient httpClient = new OkHttpClient();

    private final Sms4Configuration sms4Configuration;

    public void sendNegativeResultSms(PushMessage message) throws SmsSendingException {

        message.setContact(cleanupNumber(message.getContact()));
        String url = buildUrl(message);
        Request request = new Request.Builder().url(url).build();
        try {
            Call call = httpClient.newCall(request);
            Response response = call.execute();
            if (!response.isSuccessful()) {
                log.info("Calling SMS4 not successfull: {}", response);
                throw new SmsSendingException("Unexpected code " + response);
            }
            // Get response body
            String responseBodyString = response.body().string();
            if (!responseBodyString.contains("SMS erfolgreich versendet")) {
                log.info("Non-positive result received fom SMS4 {}", responseBodyString);
                throw new SmsSendingException("Not successful sent sms " + responseBodyString);
            }
            log.debug(response.body().string());
        } catch (IOException e) {
            log.info("Exception while communicating with SMS4", e);
            throw new SmsSendingException(e);
        }
    }

    private static String cleanupNumber(String number) {
        if (number.startsWith("0") || number.startsWith("+"))
            return cleanupNumber(number.substring(1));
        return number;
    }

    private String buildUrl(PushMessage message) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(sms4Configuration.getBaseUrl()).newBuilder();

        urlBuilder.addQueryParameter(sms4Configuration.getParamTo(), message.getContact());
        urlBuilder.addQueryParameter(sms4Configuration.getParamUser(), sms4Configuration.getUser());
        urlBuilder.addQueryParameter(sms4Configuration.getParamPwd(), sms4Configuration.getPassword());
        urlBuilder.addQueryParameter(sms4Configuration.getParamKdnr(), sms4Configuration.getKdnr());
        urlBuilder.addQueryParameter(sms4Configuration.getParamFrom(), message.getFrom());
        urlBuilder.addQueryParameter(sms4Configuration.getParamText(), message.getText());
        return urlBuilder.build().toString();
    }
}
