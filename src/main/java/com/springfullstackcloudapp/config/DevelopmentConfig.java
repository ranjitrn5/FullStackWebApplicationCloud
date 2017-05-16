package com.springfullstackcloudapp.config;

import com.springfullstackcloudapp.backend.service.EmailService;
import com.springfullstackcloudapp.backend.service.MockEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by ranji on 5/16/2017.
 */
@Configuration
@Profile("dev")
@PropertySource("file:///${user.home}/Desktop/Studies/UdemyFullStackCloudApplication/FullStackWebApplicationCloud/.fullstackapplication/application-dev.properties")
public class DevelopmentConfig {

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }
}
