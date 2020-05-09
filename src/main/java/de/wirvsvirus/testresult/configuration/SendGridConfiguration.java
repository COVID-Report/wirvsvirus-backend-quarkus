package de.wirvsvirus.testresult.configuration;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "sendgrid")
public class SendGridConfiguration {

    private String apikey;
}
