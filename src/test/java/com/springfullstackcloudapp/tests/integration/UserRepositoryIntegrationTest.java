package com.springfullstackcloudapp.tests.integration;

import com.springfullstackcloudapp.SpringfullstackcloudappApplication;
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
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ranji on 5/17/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringfullstackcloudappApplication.class)
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Rule public TestName testName = new TestName();


    /*public UserRepositoryIntegrationTest() {
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }*/

    @Test
    public void testCreateNewPlan() throws Exception{
        Plan basicPlan = createplan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Plan retrievedPlan = planRepository.findOne(PlansEnum.BASIC.getId());
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void testCreateNewRole() throws Exception{
        Role basicRole = createRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);
        Role retrievedRole = roleRepository.findOne(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void createNewUser() throws Exception{

        String username = testName.getMethodName();
        String email = testName.getMethodName()+"@devopsbuddy.com";
        User basicUser = createUser(username, email);
        User newlyCreatedUser = userRepository.findOne(basicUser.getId());
        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.getId() != 0);
        Assert.assertNotNull(newlyCreatedUser.getPlan());
        Assert.assertNotNull(newlyCreatedUser.getPlan().getId());
        Set<UserRole> newlyCreatedUserRoles = newlyCreatedUser.getUserRoles();

        for(UserRole ur: newlyCreatedUserRoles){
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }
    }

    @Test
    public void testDeleteUser() throws Exception{
        String username = testName.getMethodName();
        String email = testName.getMethodName()+"@devopsbuddy.com";
        User  basicUser = createUser(username, email);
        userRepository.delete(basicUser.getId());
    }

    @Test
    public void testGetUserByEmail() throws Exception{
        User user = createUser(testName);

        User newUser = userRepository.findByEmail(user.getEmail());
        Assert.assertNotNull(newUser);
        Assert.assertNotNull(newUser.getId());
    }

    @Test
    public void testUpdateUserPassword() throws Exception{
        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        String newPassword = UUID.randomUUID().toString();

        userRepository.updateUserPassword(user.getId(), newPassword);

        user = userRepository.findOne(user.getId());
        Assert.assertEquals(newPassword, user.getPassword());
    }
}
