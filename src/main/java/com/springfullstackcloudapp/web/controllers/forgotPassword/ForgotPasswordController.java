package com.springfullstackcloudapp.web.controllers.forgotPassword;

import com.springfullstackcloudapp.backend.persistence.domains.backend.PasswordResetToken;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.service.EmailService;
import com.springfullstackcloudapp.backend.service.PasswordResetTokenService;
import com.springfullstackcloudapp.utils.UserUtils;
import com.springfullstackcloudapp.backend.service.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ranji on 6/16/2017.
 */
@Controller
public class ForgotPasswordController {

    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(ForgotPasswordController.class);

    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";

    public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";

    public static final String MAIL_SENT_KEY="mailSent";
    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";
    public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME="forgotmypassword.email.text";

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Value("${webmaster.email}")
    private String webMasterEmail;

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
    public String getForgotPasswordUrl(){
        return ForgotPasswordController.EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
    public String postForgotPasswordUrl(HttpServletRequest request,
                                      @RequestParam("email") String email,
                                      ModelMap model){
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(email);
        if(null == passwordResetToken){
            LOG.warn("Coudn't find the password reset token for email {}", email);
        }
        else{
            User user = passwordResetToken.getUser();
            String token = passwordResetToken.getToken();

            String resetPasswordUrl = UserUtils.createPasswordResetUrl(request,user.getId(), token);
            LOG.info("Reset Password Url {}", resetPasswordUrl);

            String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME,request.getLocale());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("\"[Devopsbuddy]: How to reset your password");
            mailMessage.setText(emailText+"\r\n"+resetPasswordUrl);
            mailMessage.setFrom(webMasterEmail);

            emailService.sendGenericEmailMessage(mailMessage);
        }

        model.addAttribute(MAIL_SENT_KEY,"true");

        return EMAIL_ADDRESS_VIEW_NAME;
    }
}
