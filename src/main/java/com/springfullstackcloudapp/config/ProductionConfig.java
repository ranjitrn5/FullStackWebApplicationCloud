package com.springfullstackcloudapp.config;

import com.springfullstackcloudapp.backend.service.EmailService;
import com.springfullstackcloudapp.backend.service.SMTPEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by ranji on 5/16/2017.
 */
@Configuration
@Profile("prod")
@PropertySource("file:///${user.home}/Desktop/Studies/UdemyFullStackCloudApplication/FullStackWebApplicationCloud/.fullstackapplication/application-prod.properties")
public class ProductionConfig {

    @Value("{stripe.prod.private.key}")
    private String stripeProdKey;

    @Bean
    public EmailService emailService(){
        return new SMTPEmailService();
    }

    @Bean
    public String stripeKey(){
        return stripeProdKey;
    }
}
