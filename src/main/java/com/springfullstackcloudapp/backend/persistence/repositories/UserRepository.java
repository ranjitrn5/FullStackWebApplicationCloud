package com.springfullstackcloudapp.backend.persistence.repositories;

import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ranji on 5/17/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    /**
     * Returns a User given a username or null if not found
     * @param username The Username
     * @return a User given a username or null if not found
     */
    public User findByUsername(String username);
}
