package com.cab.nearbydrivers.config;

import com.uber.h3core.H3Core;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    H3Core h3;

    @Bean
    H3Core h3Core() {
        try{
            h3 = H3Core.newInstance();
            return h3;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return h3;
    }
}