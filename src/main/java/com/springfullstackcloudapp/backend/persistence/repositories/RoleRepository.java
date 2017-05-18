package com.springfullstackcloudapp.backend.persistence.repositories;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ranji on 5/17/2017.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
