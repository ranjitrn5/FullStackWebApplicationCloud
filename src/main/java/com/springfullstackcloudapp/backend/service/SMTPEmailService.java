package com.springfullstackcloudapp.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Real Implementation of email service.
 * Created by ranji on 5/16/2017.
 */
public class SMTPEmailService extends AbstractEmailService {
    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(SMTPEmailService.class);

    @Autowired
    private MailSender mailSender;

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage message) {
        LOG.debug("Sending email for: {}",message);
        mailSender.send(message);
        LOG.info("Mail Sent.");

        
    }
}
