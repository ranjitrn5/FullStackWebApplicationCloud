package com.springfullstackcloudapp.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by ranji on 5/16/2017.
 */
public class MockEmailService extends AbstractEmailService {
    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage message) {
        LOG.debug("Simulating Email Service");
        LOG.info(message.toString());
        LOG.debug("Mail Sent");
    }
}
