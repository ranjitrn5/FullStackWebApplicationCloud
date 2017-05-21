package com.springfullstackcloudapp.backend.persistence.domains.backend;

import com.springfullstackcloudapp.backend.persistence.converters.LocalDateTimeAttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by ranji on 5/20/2017.
 */
@Entity
public class PasswordResetToken implements Serializable {

    /*The Serial Version UID for Serializable Classes*/
    private static final long serialVersionUID = 1L;
    
    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetToken.class);

    private static final int DEFAULT_TOKEN_LENGTH_IN_MINUTES = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expiryDate;

    public PasswordResetToken(){

    }

    /**
     *
     * @param token User token. It must not be null
     * @param user User for which token should be created. it should not be null
     * @param expiryDate Date time this reequest was created. It should not be null
     * @param expirationInMinutes Length in minutes, for which token will be valid. If zero it will be assigned a default value of 120 minutes(2 hours
     * @throws IllegalArgumentException If user or token or expiryDateTime is null
     */

    public PasswordResetToken(String token, User user, LocalDateTime expiryDate, int expirationInMinutes) {
        if(null == token || null == user || null == expiryDate){
            throw new IllegalArgumentException("token, user and creation date time cant be null");
        }

        if(expirationInMinutes == 0){
            LOG.warn("Token expiration in minutes is zero. Assigning default value {}", DEFAULT_TOKEN_LENGTH_IN_MINUTES);
            expirationInMinutes = DEFAULT_TOKEN_LENGTH_IN_MINUTES;
        }

        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate.plusMinutes(expirationInMinutes);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordResetToken that = (PasswordResetToken) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
