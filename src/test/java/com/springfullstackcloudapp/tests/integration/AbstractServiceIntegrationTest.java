package com.springfullstackcloudapp.tests.integration;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Role;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.domains.backend.UserRole;
import com.springfullstackcloudapp.backend.service.UserService;
import com.springfullstackcloudapp.enums.PlansEnum;
import com.springfullstackcloudapp.enums.RolesEnum;
import com.springfullstackcloudapp.utils.UserUtils;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ranji on 5/21/2017.
 */
public abstract class AbstractServiceIntegrationTest {

    @Autowired
    protected UserService userService;

    protected User createuser(TestName testName){
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@devopsbuddy.com";

        Set<UserRole> userRoles = new HashSet<>();
        User basicUser = UserUtils.createBasicUser(username, email);
        userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
        User user = userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
        return user;

    }
}
