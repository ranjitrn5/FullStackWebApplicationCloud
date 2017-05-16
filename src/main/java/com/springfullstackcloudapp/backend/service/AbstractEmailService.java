package com.springfullstackcloudapp.backend.service;

import com.springfullstackcloudapp.web.domain.frontend.FeedbackPOJO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by ranji on 5/16/2017.
 */
public abstract class AbstractEmailService implements EmailService {

    @Value("${default.to.address}")
    private String defaultToAddress;
    /**
     * Creates a Simple Mail Message from Feedback Pojo.
     * @param feedback The feedback pojo object
     * @return
     * Created by ranji on 5/16/2017.
     */
    protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackPojo(FeedbackPOJO feedback){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(defaultToAddress);
        message.setFrom(feedback.getEmail());
        message.setSubject("[Devops Buddy]: Feedback received from "+feedback.getFirstName()+" "+feedback.getLastName()+"!");
        message.setText(feedback.getFeedback());
        return message;

    }

    @Override
    public void sendFeedbackEmail(FeedbackPOJO feedbackPOJO){
        sendGenericEmailMessage(prepareSimpleMailMessageFromFeedbackPojo(feedbackPOJO));
    }
}
