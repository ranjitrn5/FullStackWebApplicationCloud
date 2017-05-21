package com.springfullstackcloudapp.tests.integration;

import com.springfullstackcloudapp.SpringfullstackcloudappApplication;
import com.springfullstackcloudapp.backend.persistence.domains.backend.PasswordResetToken;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.repositories.PasswordResetTokenRepository;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by ranji on 5/20/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringfullstackcloudappApplication.class)
public class PasswordIntegrationTest extends AbstractIntegrationTest {

    @Value("${token.expiration.length.minutes}")
    private int expirationTimeInMinutes;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void init(){
        Assert.assertFalse(expirationTimeInMinutes == 0);
    }

    @Test
    public void testTokenExpirationLength() throws Exception{
        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token = UUID.randomUUID().toString();

        LocalDateTime expectedTime = now.plusMinutes(expirationTimeInMinutes);

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);

        LocalDateTime actualTime = passwordResetToken.getExpiryDate();
        Assert.assertNotNull(actualTime);
        Assert.assertEquals(expectedTime, actualTime);
    }

    @Test
    public void testTokenValue() throws Exception{
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        createPasswordResetToken(token, user, now);
        PasswordResetToken retrievedPasswordResetToken = passwordResetTokenRepository.findByToken(token);
        Assert.assertNotNull(retrievedPasswordResetToken);
        Assert.assertNotNull(retrievedPasswordResetToken.getId());
        Assert.assertNotNull(retrievedPasswordResetToken.getUser());
    }

    @Test
    public void testDeleteToken() throws Exception{
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        long tokenId = passwordResetToken.getId();
        passwordResetTokenRepository.delete(tokenId);

        PasswordResetToken shouldNotExistToken = passwordResetTokenRepository.findOne(tokenId);
        Assert.assertNull(shouldNotExistToken);

    }

    @Test
    public void testCascaseDeleteFromUserEntity() throws Exception{
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        passwordResetToken.getId();

        userRepository.delete(user.getId());

        Set<PasswordResetToken> shouldBeEmpty = passwordResetTokenRepository.findAllTokensByUserId(user.getId());
        Assert.assertTrue(shouldBeEmpty.isEmpty());
    }

    @Test
    public void testMultipleTokensAreReturnedFromQueryingByUserId() throws Exception{
        User user = createUser(testName);
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        String token1 = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        String token3 = UUID.randomUUID().toString();

        Set<PasswordResetToken> tokens = new HashSet<>();
        tokens.add(createPasswordResetToken(token1, user, now));
        tokens.add(createPasswordResetToken(token2, user, now));
        tokens.add(createPasswordResetToken(token3, user, now));

        passwordResetTokenRepository.save(tokens);

        User foundUser = userRepository.findOne(user.getId());

        Set<PasswordResetToken> actualTokens = passwordResetTokenRepository.findAllTokensByUserId(foundUser.getId());
        Assert.assertTrue(actualTokens.size() == tokens.size());

        List<String> tokenAsList = tokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
        List<String> actualTokenAsList = actualTokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
        Assert.assertEquals(tokenAsList, actualTokenAsList);
    }

    private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now){
        PasswordResetToken passwordResetToken = new PasswordResetToken(token,user,now,expirationTimeInMinutes);
        passwordResetTokenRepository.save(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getId());
        return passwordResetToken;
    }
}
