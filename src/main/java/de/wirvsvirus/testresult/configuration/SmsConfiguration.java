package de.wirvsvirus.testresult.configuration;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "sms")
public class SmsConfiguration {

    private String mobilePattern;
    private String mobileCleanupPattern;
}
