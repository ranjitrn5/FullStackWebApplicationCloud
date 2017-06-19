package com.springfullstackcloudapp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by ranji on 5/19/2017.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.springfullstackcloudapp.backend.persistence.repositories")
@EntityScan(basePackages = "com.springfullstackcloudapp.backend.persistence.domains.backend")
@EnableTransactionManagement
@PropertySource("file:///${user.home}/Desktop/Studies/UdemyFullStackCloudApplication/FullStackWebApplicationCloud/.fullstackapplication/application-common.properties")
public class ApplicationConfig {

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Bean
    public AmazonS3Client s3Client(){
        AWSCredentials credentials = new ProfileCredentialsProvider(awsProfileName).getCredentials();
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        Region region = Region.getRegion(Regions.US_EAST_1);
        s3Client.setRegion(region);
        return s3Client;
    }
}
