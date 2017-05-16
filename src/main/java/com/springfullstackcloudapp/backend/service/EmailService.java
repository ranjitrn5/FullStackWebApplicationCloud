package com.springfullstackcloudapp.backend.service;

import com.springfullstackcloudapp.web.domain.frontend.FeedbackPOJO;
import org.springframework.mail.SimpleMailMessage;

/**
 * Contract for Email Service
 * Created by ranji on 5/16/2017.
 */
public interface EmailService {

    /**
     * Sends an email with content present in Feedback Pojo object
     * @param feedbackPOJO The Feedback POJO object
     */
    public void sendFeedbackEmail(FeedbackPOJO feedbackPOJO);

    /**
     * Sends an email with content present in Simple Mail Message Object
     * @param message The object containing email content
     */
    public void sendGenericEmailMessage(SimpleMailMessage message);
}
