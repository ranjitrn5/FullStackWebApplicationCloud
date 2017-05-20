package com.springfullstackcloudapp;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Role;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.domains.backend.UserRole;
import com.springfullstackcloudapp.backend.service.UserService;
import com.springfullstackcloudapp.enums.PlansEnum;
import com.springfullstackcloudapp.enums.RolesEnum;
import com.springfullstackcloudapp.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SpringfullstackcloudappApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;
	
	/* The Application Logger*/
	private static final Logger LOG = LoggerFactory.getLogger(SpringfullstackcloudappApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringfullstackcloudappApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		User user = UserUtils.createBasicUser();
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user,new Role(RolesEnum.PRO)));
		LOG.debug("Creating user with username {}", user.getUsername());
		userService.createUser(user,PlansEnum.BASIC,userRoles);
		LOG.info("User {} is created", user.getUsername());
	}
}
