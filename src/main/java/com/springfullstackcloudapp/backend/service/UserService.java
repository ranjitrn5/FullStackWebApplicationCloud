package com.springfullstackcloudapp.backend.service;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Plan;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.domains.backend.UserRole;
import com.springfullstackcloudapp.backend.persistence.repositories.PlanRepository;
import com.springfullstackcloudapp.backend.persistence.repositories.RoleRepository;
import com.springfullstackcloudapp.backend.persistence.repositories.UserRepository;
import com.springfullstackcloudapp.enums.PlansEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by ranji on 5/19/2017.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    PlanRepository planRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles){
        String encryptedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        Plan plan = new Plan(PlansEnum.BASIC);

        if(!planRepository.exists(plansEnum.getId())){
            plan = planRepository.save(plan);
        }

        user.setPlan(plan);

        for(UserRole ur: userRoles){
            roleRepository.save(ur.getRole());
        }

        user.getUserRoles().addAll(userRoles);
        userRepository.save(user);
        return user;
    }
}
