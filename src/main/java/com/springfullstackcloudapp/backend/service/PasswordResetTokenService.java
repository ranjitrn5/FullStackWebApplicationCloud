package com.springfullstackcloudapp.backend.service;

import com.springfullstackcloudapp.backend.persistence.domains.backend.PasswordResetToken;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.repositories.PasswordResetTokenRepository;
import com.springfullstackcloudapp.backend.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by ranji on 5/21/2017.
 */
@Service
@Transactional(readOnly = true)
public class PasswordResetTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${token.expiration.length.minutes}")
    private int tokenExpirationInMinutes;

    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetTokenService.class);

    /**
     * Retrieves Password reset token for given token id
     * @param token The token to be returned
     * @return A password reset token if token was found or returns null
     */
    public PasswordResetToken findByToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }

    /**
     * Creates new password reset token for user identified by email
     * @param email The email uniquely identified by user
     * @return A new password reset token for user identified by given email or null if none was found
     */
    @Transactional
    public PasswordResetToken createPasswordResetToken(String email){
        PasswordResetToken passwordResetToken = null;
        User user = userRepository.findByEmail(email);

        if(null != user){
            String token = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            passwordResetToken = new PasswordResetToken(token, user, now, tokenExpirationInMinutes);

            passwordResetTokenRepository.save(passwordResetToken);

            LOG.debug("Successfully created token {} for user {}", token, user.getUsername());
        }
        else{
            LOG.warn("We couldn't find user for a given email {}", email);
        }
        return passwordResetToken;
    }

}
