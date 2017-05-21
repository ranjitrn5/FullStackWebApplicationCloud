package com.springfullstackcloudapp.tests.integration;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Plan;
import com.springfullstackcloudapp.backend.persistence.domains.backend.Role;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.domains.backend.UserRole;
import com.springfullstackcloudapp.backend.persistence.repositories.PlanRepository;
import com.springfullstackcloudapp.backend.persistence.repositories.RoleRepository;
import com.springfullstackcloudapp.backend.persistence.repositories.UserRepository;
import com.springfullstackcloudapp.enums.PlansEnum;
import com.springfullstackcloudapp.enums.RolesEnum;
import com.springfullstackcloudapp.utils.UserUtils;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ranji on 5/20/2017.
 */
public abstract class AbstractIntegrationTest {

    @Autowired
    protected PlanRepository planRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected UserRepository userRepository;

    protected Plan createplan(PlansEnum plansEnum){
        return new Plan(plansEnum);
    }

    protected Role createRole(RolesEnum rolesEnum){
        return new Role(rolesEnum);
    }

    protected User createUser(String username, String email){
        Plan basicPlan = createplan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser(username, email);
        basicUser.setPlan(basicPlan);

        Role basicRole = createRole(RolesEnum.BASIC);
        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser, basicRole);

        basicUser.getUserRoles().addAll(userRoles);

        for(UserRole ur:userRoles){
            roleRepository.save(ur.getRole());
        }

        basicUser = userRepository.save(basicUser);
        return basicUser;
    }

    protected User createUser(TestName testName){
        return createUser(testName.getMethodName(),testName.getMethodName()+"@devopsbuddy.com");
    }



}
