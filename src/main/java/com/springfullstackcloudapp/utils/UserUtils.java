package com.springfullstackcloudapp.utils;

import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.web.controllers.forgotPassword.ForgotPasswordController;
import com.springfullstackcloudapp.web.domain.frontend.BasicAccountPayload;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by ranji on 5/19/2017.
 */
public class UserUtils {

    /**
     * Creates a user with basic attributes set to it
     * @param username
     * @param email
     * @return A user entity
     */

    public static User createBasicUser(String username, String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(email);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("123456789123");
        user.setDescription("A Basic User");
        user.setCountry("US");
        user.setEnabled(true);
        user.setProfileImageUrl("http://blable.images.com/basicuser");

        return user;
    }

    public static String createPasswordResetUrl(HttpServletRequest request, long userId, String token) {
        String passwordResetUrl =
                request.getScheme()+
                        "://"+request.getServerName()+
                        ":"+request.getServerPort()+
                        request.getContextPath()+
                        ForgotPasswordController.CHANGE_PASSWORD_PATH+
                        "?id="+userId+
                        "&token="+token;

        return passwordResetUrl;

    }

    public static <T extends BasicAccountPayload> User fromWebUserToDomainUser(T frontendPayload) {

        User user = new User();
        user.setUsername(frontendPayload.getUsername());
        user.setPassword(frontendPayload.getPassword());
        user.setFirstName(frontendPayload.getFirstName());
        user.setLastName(frontendPayload.getLastName());
        user.setEmail(frontendPayload.getEmail());
        user.setPhoneNumber(frontendPayload.getPhoneNumber());
        user.setCountry(frontendPayload.getCountry());
        user.setEnabled(true);
        user.setDescription(frontendPayload.getDescription());

        return user;

    }
}
