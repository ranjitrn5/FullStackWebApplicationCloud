package com.springfullstackcloudapp.web.controllers.Login;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Plan;
import com.springfullstackcloudapp.backend.persistence.domains.backend.Role;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.domains.backend.UserRole;
import com.springfullstackcloudapp.backend.service.PlanService;
import com.springfullstackcloudapp.backend.service.UserService;
import com.springfullstackcloudapp.enums.PlansEnum;
import com.springfullstackcloudapp.enums.RolesEnum;
import com.springfullstackcloudapp.utils.UserUtils;
import com.springfullstackcloudapp.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ranji on 6/18/2017.
 */
@Controller
public class SignupController {

    @Autowired
    private PlanService planService;

    @Autowired
    private UserService userService;

    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);

    public static final String SIGNUP_URL_MAPPING="/signup";
    public static final String PAYLOAD_MODEL_KEY_NAME="payload";
    public static final String SUBSCRIPTION_VIEW_NAME="registration/signup";

    public static final String DUPLICATED_USERNAME_KEY = "duplicatedUsername";
    public static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";
    public static final String SIGNED_UP_MESSAGE_KEY = "signedUp";
    public static final String ERROR_MESSAGE_KEY = "message";

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signUpGet(@RequestParam("planId") int planId, ModelMap model){
        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()){
            throw new IllegalArgumentException("Plan Id is not Valid");
        }
        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());
        return SUBSCRIPTION_VIEW_NAME;
    }

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.POST)
    public String signUpPost(@RequestParam(name = "planId", required = true) int planId,
                             @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload, ModelMap model) throws IOException{
        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()){
            model.addAttribute(SIGNED_UP_MESSAGE_KEY,"false");
            model.addAttribute(ERROR_MESSAGE_KEY,"Plan Id does not exist");
            return SUBSCRIPTION_VIEW_NAME;
        }

        this.checkForDuplicates(payload, model);

        boolean duplicates = false;

        List<String> errorMessages = new ArrayList<>();
        if(model.containsKey(DUPLICATED_USERNAME_KEY)){
            LOG.warn("Username already exists. Display error to user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY,"false");
            errorMessages.add("USername already exist");
            duplicates = true;
        }

        if(model.containsKey(DUPLICATED_EMAIL_KEY)){
            LOG.warn("Email already exists. Display error to user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY,"false");
            errorMessages.add("Email already exist");
            duplicates = true;
        }

        if(duplicates){
            model.addAttribute(ERROR_MESSAGE_KEY, errorMessages);
            return SUBSCRIPTION_VIEW_NAME;
        }

        LOG.debug("Transforming user payload into user domain object");

        User user = UserUtils.fromWebUserToDomainUser(payload);

        LOG.debug("Retrieving plan from database");
        Plan selectedPlan = planService.findPlanById(planId);
        if(null == selectedPlan){
            LOG.error("Plan id {} could not be found. Throwing exception.", planId);
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id not found");
            return SUBSCRIPTION_VIEW_NAME;
        }

        user.setPlan(selectedPlan);

        User registeredUser = null;

        Set<UserRole> roles = new HashSet<>();
        if(planId == PlansEnum.BASIC.getId()){
            roles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
            registeredUser = userService.createUser(user, PlansEnum.BASIC, roles);
        }else{
            roles.add(new UserRole(user, new Role(RolesEnum.PRO)));
            registeredUser = userService.createUser(user, PlansEnum.PRO, roles);
        }

        //Auto logs in user
        Authentication auth = new UsernamePasswordAuthenticationToken(registeredUser, null, registeredUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        LOG.info("User created successfully");

        model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");

        return SUBSCRIPTION_VIEW_NAME;


    }

    private void checkForDuplicates(ProAccountPayload payload, ModelMap model) {

        if(userService.findByUserName(payload.getUsername()) != null){
            model.addAttribute(DUPLICATED_USERNAME_KEY, true);
        }
        if(userService.findByEmail(payload.getEmail()) != null){
            model.addAttribute(DUPLICATED_EMAIL_KEY, true);
        }
    }


}
