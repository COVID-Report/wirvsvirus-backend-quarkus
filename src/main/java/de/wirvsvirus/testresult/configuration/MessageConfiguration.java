package de.wirvsvirus.testresult.configuration;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "message")
public class MessageConfiguration {

    private String from;
    private String text;
}
