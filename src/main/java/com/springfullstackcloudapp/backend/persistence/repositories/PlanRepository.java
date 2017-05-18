package com.springfullstackcloudapp.backend.persistence.repositories;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ranji on 5/17/2017.
 */
@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer> {
}
