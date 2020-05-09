package de.wirvsvirus.testresult.configuration;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "sms4")
public class Sms4Configuration {

    private String user;
    private String kdnr;
    private String password;

    private String baseUrl;
    private String paramUser;
    private String paramPwd;
    private String paramKdnr;
    private String paramTo;
    private String paramFrom;
    private String paramText;
}
