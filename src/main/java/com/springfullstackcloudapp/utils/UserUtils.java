package com.springfullstackcloudapp.utils;

import com.springfullstackcloudapp.backend.persistence.domains.backend.User;

/**
 * Created by ranji on 5/19/2017.
 */
public class UserUtils {

    private UserUtils(){
        throw new AssertionError("Non instantiable");
    }

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
}
