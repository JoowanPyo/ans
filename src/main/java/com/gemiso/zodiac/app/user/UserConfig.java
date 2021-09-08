package com.gemiso.zodiac.app.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner userConfigCommandLineRunner() {
        return args -> {
            //
        };
    }
}
