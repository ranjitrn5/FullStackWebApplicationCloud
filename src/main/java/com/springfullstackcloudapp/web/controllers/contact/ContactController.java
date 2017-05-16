package com.springfullstackcloudapp.web.controllers.contact;

import com.springfullstackcloudapp.backend.service.EmailService;
import com.springfullstackcloudapp.web.domain.frontend.FeedbackPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ranji on 5/16/2017.
 */
@Controller
public class ContactController {

    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(ContactController.class);

    private static final String FEEDBACK_MODEL_KEY="feedback";

    private static final String CONTACT_US_VIEW_NAME="contact/contact";

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String getContact(ModelMap modelMap){
        FeedbackPOJO feedbackPojo = new FeedbackPOJO();
        modelMap.addAttribute(ContactController.FEEDBACK_MODEL_KEY, feedbackPojo);
        return ContactController.CONTACT_US_VIEW_NAME;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String postContact(@ModelAttribute(FEEDBACK_MODEL_KEY) FeedbackPOJO feedback){
        LOG.debug("Feedback POJO Content {}", feedback);
        emailService.sendFeedbackEmail(feedback);
        return ContactController.CONTACT_US_VIEW_NAME;
    }

}
