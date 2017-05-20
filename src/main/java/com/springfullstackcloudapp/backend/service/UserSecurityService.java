package com.springfullstackcloudapp.backend.service;

import com.springfullstackcloudapp.backend.persistence.domains.backend.User;
import com.springfullstackcloudapp.backend.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * In this interface we have used UserDetailsService and UserDetails interface that is provided by Spring and which runs at runtime
 * Created by ranji on 5/20/2017.
 */
@Service
public class UserSecurityService implements UserDetailsService {
    
    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(null == user){
            LOG.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username "+username+" not found");
        }
        return user;
    }
}
