package de.wirvsvirus.testresult.service.impl.sms4;


import de.wirvsvirus.testresult.exception.SmsSendingException;
import de.wirvsvirus.testresult.model.PushMessage;
import de.wirvsvirus.testresult.service.SmsServiceProvider;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

/**
 * Using https://www.sms4.de/cgi-bin/sms_http.pl
 */
@Slf4j
@ApplicationScoped
public class SmsServiceSms4 implements SmsServiceProvider {

    private static final OkHttpClient httpClient = new OkHttpClient();

    @ConfigProperty(name = "sms4.user", defaultValue = "")
    protected String sms4User;

    @ConfigProperty(name = "sms4.kdnr", defaultValue = "")
    protected String sms4Kdnr;

    @ConfigProperty(name = "sms4.password", defaultValue = "")
    protected String sms4Password;

    public void sendNegativeResultSms(PushMessage message) throws SmsSendingException {

        message.setContact(cleanupNumber(message.getContact()));
        String url = buildUrl(message);
        Request request = new Request.Builder().url(url).build();
        Call call = httpClient.newCall(request);
        Response response;
        try {
            response = call.execute();
            if (!response.isSuccessful()) {
                throw new SmsSendingException("Unexpected code " + response);
            }
            // Get response headers
            ResponseBody responseBody = response.body();
            if (!responseBody.string().contains("SMS erfolgreich versendet")) {
                throw new SmsSendingException("Not successful sent sms " + responseBody.string());
            }
            log.debug(response.body().string());
        } catch (IOException e) {
            throw new SmsSendingException(e);
        }
    }

    private static String cleanupNumber(String number) {
        if (number.startsWith("0") || number.startsWith("+"))
            return cleanupNumber(number.substring(1));
        return number;
    }

    private String buildUrl(PushMessage message) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Sms4Constants.BASE_URL).newBuilder();

        urlBuilder.addQueryParameter(Sms4Constants.TO, message.getContact());
        urlBuilder.addQueryParameter(Sms4Constants.USER, sms4User);
        urlBuilder.addQueryParameter(Sms4Constants.PSW, sms4Password);
        urlBuilder.addQueryParameter(Sms4Constants.KDNR, sms4Kdnr);
        urlBuilder.addQueryParameter(Sms4Constants.FROM, message.getFrom());
        urlBuilder.addQueryParameter(Sms4Constants.TEXT, message.getText());
        return urlBuilder.build().toString();
    }
}
