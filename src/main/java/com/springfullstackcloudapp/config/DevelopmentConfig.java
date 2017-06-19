package com.springfullstackcloudapp.config;

import com.springfullstackcloudapp.backend.service.EmailService;
import com.springfullstackcloudapp.backend.service.MockEmailService;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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

    @Value("${stripe.dev.private.key}")
    private String stripeDevKey;

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }

    @Bean
    public ServletRegistrationBean h2ConsoleServletRegistration(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new WebServlet());
        bean.addUrlMappings("/console/*");
        return bean;
    }

    @Bean
    public  String stripeKey(){
        return stripeDevKey;
    }
}
