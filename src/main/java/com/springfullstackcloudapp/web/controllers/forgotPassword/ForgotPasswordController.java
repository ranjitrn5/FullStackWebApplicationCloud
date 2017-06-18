package com.springfullstackcloudapp.web.controllers.forgotPassword;

import antlr.StringUtils;
import com.springfullstackcloudapp.backend.persistence.domains.backend.PasswordResetToken;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.service.EmailService;
import com.springfullstackcloudapp.backend.service.PasswordResetTokenService;
import com.springfullstackcloudapp.backend.service.UserService;
import com.springfullstackcloudapp.utils.UserUtils;
import com.springfullstackcloudapp.backend.service.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

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

    public static final String CHANGE_PASSWORD_VIEW_NAME="forgotmypassword/changepassword";

    private static final String PASSWORD_RESET_ATTRIBUTE_NAME="passwordReset";
    private static final String MESSAGE_ATTRIBUTE_NAME="message";

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

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

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
    public String changeUserPasswordGet(@RequestParam("id") long id,
                                        @RequestParam("token") String token,
                                        Locale locale,
                                        ModelMap model){

        if(org.springframework.util.StringUtils.isEmpty(token) || id == 0){
            LOG.error("Invalid user id {} or token value {}",id, token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME,"false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,"Invalid user id or token value");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
        if(null == passwordResetToken){
            LOG.warn("A token couldn't be found with value {}", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME,"false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,"Token not found");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = passwordResetToken.getUser();
        if(user.getId() != id){
            LOG.error("The user id {} passed with parameter doesn't match with the user id {} associated with token {}",
                    user.getId(),id,token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME,"false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,i18NService.getMessage("resetPassword.token.invalid", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if(LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())){
            LOG.error("Token {} has expired", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME,"false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,i18NService.getMessage("resetPassword.token.expired", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }
        model.addAttribute("principalId", user.getId());

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return CHANGE_PASSWORD_VIEW_NAME;
    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
    public String changeUserPassword(@RequestParam("principal_id") long userId,
                                     @RequestParam("password") String password,
                                     ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(null == auth){
            LOG.error("An unauthenticated user tried to invoke reset password POST method");
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME,"false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = (User)auth.getPrincipal();
        if(user.getId() != userId){
            LOG.error("Security breach user {} is trying to reset the password on behalf of user {}", user.getId(), userId);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME,"false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        userService.updatePassword(userId, password);
        LOG.info("Password successfully updated for user {}", user.getUsername());

        model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME,"true");
        return CHANGE_PASSWORD_VIEW_NAME;
    }
}
