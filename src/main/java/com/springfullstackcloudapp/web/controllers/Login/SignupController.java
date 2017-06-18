package com.springfullstackcloudapp.web.controllers.Login;

import com.springfullstackcloudapp.enums.PlansEnum;
import com.springfullstackcloudapp.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by ranji on 6/18/2017.
 */
@Controller
public class SignupController {

    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);

    public static final String SIGNUP_URL_MAPPING="/signup";
    public static final String PAYLOAD_MODEL_KEY_NAME="payload";
    public static final String SUBSCRIPTION_VIEW_NAME="registration/signup";

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signUpGet(@RequestParam("planId") int planId, ModelMap model){
        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()){
            throw new IllegalArgumentException("Plan Id is not Valid");
        }
        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());
        return SUBSCRIPTION_VIEW_NAME;
    }



}
