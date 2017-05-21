package com.springfullstackcloudapp.backend.persistence.repositories;

import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ranji on 5/17/2017.
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User,Long> {

    /**
     * Returns a User given a username or null if not found
     * @param username The Username
     * @return a User given a username or null if not found
     */
    public User findByUsername(String username);

    /**
     * Returns a User given an email or null if not found
     * @param username The user's email
     * @return a User for a  given email or null if not found
     */
    User findByEmail(String email);

    @Modifying
    @Query("update User u set u.password = :password where u.id= :userId")
    void updateUserPassword(@Param("userId") long userId, @Param("password") String password);
}
