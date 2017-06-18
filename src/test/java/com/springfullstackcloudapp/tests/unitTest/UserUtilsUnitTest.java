package com.springfullstackcloudapp.tests.unitTest;

import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.utils.UserUtils;
import com.springfullstackcloudapp.web.controllers.forgotPassword.ForgotPasswordController;
import com.springfullstackcloudapp.web.domain.frontend.BasicAccountPayload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.UUID;

/**
 * Created by ranji on 6/16/2017.
 */
public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;
    private PodamFactory podamFactory;

    @Before
    public void init(){
        mockHttpServletRequest = new MockHttpServletRequest();
        podamFactory = new PodamFactoryImpl();
    }

    @Test
    public void testPasswordResetEmailUrlConstruction() throws Exception{
        mockHttpServletRequest.setServerPort(8080);

        String token = UUID.randomUUID().toString();
        long userId = 123456;

        String expectedUrl = "http://localhost:8080"+
                ForgotPasswordController.CHANGE_PASSWORD_PATH + "?id=" + userId + "&token=" +token;

        String actualUrl = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userId, token);

        Assert.assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void mapWebUserToDomainUser(){
        BasicAccountPayload webUser = podamFactory.manufacturePojo(BasicAccountPayload.class);//manufacturePojoWithFullData(BasicAccountPayload.class);
        webUser.setEmail("me@example.com");

        User user = UserUtils.fromWebUserToDomainUser(webUser);
        Assert.assertNotNull(user);

        Assert.assertEquals(webUser.getUsername(), user.getUsername());
        Assert.assertEquals(webUser.getPassword(), user.getPassword());
        Assert.assertEquals(webUser.getFirstName(), user.getFirstName());
        Assert.assertEquals(webUser.getLastName(), user.getLastName());
        Assert.assertEquals(webUser.getEmail(), user.getEmail());
        Assert.assertEquals(webUser.getPhoneNumber(), user.getPhoneNumber());
        Assert.assertEquals(webUser.getCountry(), user.getCountry());
        Assert.assertEquals(webUser.getDescription(), user.getDescription());

    }

}
