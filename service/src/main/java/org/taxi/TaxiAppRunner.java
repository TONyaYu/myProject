package org.taxi;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.taxi.config.AppConfig;

public class TaxiAppRunner {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext
                     = new AnnotationConfigApplicationContext(AppConfig.class)) {

        }
    }
}
