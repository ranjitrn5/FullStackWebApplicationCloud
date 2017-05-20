package com.springfullstackcloudapp.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by ranji on 5/19/2017.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.springfullstackcloudapp.backend.persistence.repositories")
@EntityScan(basePackages = "com.springfullstackcloudapp.backend.persistence.repositories")
@EnableTransactionManagement
public class ApplicationConfig {
}