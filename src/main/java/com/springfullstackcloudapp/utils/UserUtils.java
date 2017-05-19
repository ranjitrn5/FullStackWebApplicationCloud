package com.springfullstackcloudapp.utils;

import com.springfullstackcloudapp.backend.persistence.domains.backend.User;

/**
 * Created by ranji on 5/19/2017.
 */
public class UserUtils {

    private UserUtils(){
        throw new AssertionError("Non instantiable");
    }

    public static User createBasicUser(){
        User user = new User();
        user.setUsername("basicUser");
        user.setPassword("secret");
        user.setEmail("me@example.com");
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
