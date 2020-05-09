package de.wirvsvirus.testresult.configuration;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "email")
public class EmailConfiguration {

    private String sender;
    private String subject;
    private String contentType;
    private String emailRegex;
}
