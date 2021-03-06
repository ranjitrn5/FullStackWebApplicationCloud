package com.springfullstackcloudapp;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Role;
import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.domains.backend.UserRole;
import com.springfullstackcloudapp.backend.service.PlanService;
import com.springfullstackcloudapp.backend.service.UserService;
import com.springfullstackcloudapp.enums.PlansEnum;
import com.springfullstackcloudapp.enums.RolesEnum;
import com.springfullstackcloudapp.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Autowired
	private PlanService planService;
	
	/* The Application Logger*/
	private static final Logger LOG = LoggerFactory.getLogger(SpringfullstackcloudappApplication.class);

	@Value("${webmaster.username}")
	private String webmasterUsername;

	@Value("${webmaster.password}")
	private String webmasterPassword;

	@Value("${webmaster.email}")
	private String webmasterEmail;

	public static void main(String[] args) {
		SpringApplication.run(SpringfullstackcloudappApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {

		LOG.info("Creating Basic and Pro plans in database");
		planService.createPlan(PlansEnum.BASIC.getId());
		planService.createPlan(PlansEnum.PRO.getId());

		User user = UserUtils.createBasicUser(webmasterUsername, webmasterEmail);
		user.setPassword(webmasterPassword);
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user,new Role(RolesEnum.ADMIN)));
		LOG.debug("Creating user with username {}", user.getUsername());
		userService.createUser(user,PlansEnum.BASIC,userRoles);
		LOG.info("User {} is created", user.getUsername());
	}
}
