package com.cab.nearbydrivers.config;

import com.uber.h3core.H3Core;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.text.html.Option;
import java.util.Optional;

@Configuration
public class AppConfig {

    @Bean
    Optional<H3Core> h3Core() {
        try{
            return Optional.of(H3Core.newInstance());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}