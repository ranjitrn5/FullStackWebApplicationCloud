package com.springfullstackcloudapp.tests.integration;

import com.springfullstackcloudapp.backend.persistence.domains.backend.PasswordResetToken;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.service.PasswordResetTokenService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ranji on 5/21/2017.
 */
public class PasswordResetTokenServiceTest extends AbstractServiceIntegrationTest {

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Rule public TestName testName = new TestName();

    @Test
    public void testCreateNewTokenForUserEmail() throws Exception{
        User user = createuser(testName);

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user.getEmail());
        Assert.assertNotNull(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getToken());
    }

    @Test
    public void testfindByToken() throws Exception{

        User user = createuser(testName);

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user.getEmail());
        Assert.assertNotNull(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getToken());
        PasswordResetToken token = passwordResetTokenService.findByToken(passwordResetToken.getToken());
        Assert.assertNotNull(token);
    }

    /**
     * Retrieve password rest token for given token id
     * @param token The token to be returned
     * @returnA password reset token if one was found or null was found
     * @throws Exception
     */
    public PasswordResetToken findByToken(String token) throws Exception{
        return passwordResetTokenService.findByToken(token);
    }
}
