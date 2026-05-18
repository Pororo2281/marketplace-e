package com.notification.notification.Senders;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class EmailLoader {

    public String loadTemplate(String templateName) {

        try {

            ClassPathResource resource =
                    new ClassPathResource(
                            "templates/" + templateName
                    );

            return new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
