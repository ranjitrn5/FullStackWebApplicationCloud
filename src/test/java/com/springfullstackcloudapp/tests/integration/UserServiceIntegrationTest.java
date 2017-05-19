package com.springfullstackcloudapp.tests.integration;

import com.springfullstackcloudapp.SpringfullstackcloudappApplication;
import com.springfullstackcloudapp.backend.persistence.domains.backend.Role;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.domains.backend.UserRole;
import com.springfullstackcloudapp.backend.service.UserService;
import com.springfullstackcloudapp.enums.PlansEnum;
import com.springfullstackcloudapp.enums.RolesEnum;
import com.springfullstackcloudapp.utils.UserUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ranji on 5/19/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SpringfullstackcloudappApplication.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCreateNewUser() throws Exception{
        Set<UserRole> userRoles = new HashSet<>();
        User basicUser = UserUtils.createBasicUser();
        userRoles.add(new UserRole(basicUser,new Role(RolesEnum.BASIC)));
        User user=userService.createUser(basicUser, PlansEnum.BASIC,userRoles);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
    }
}
