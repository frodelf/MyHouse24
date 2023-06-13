package com.avada.myHouse24.config;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.repo.UserRepository;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserServiceImpl userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByFirstName(username);
        log.info("IN UserDetailsServiceImpl user found{}", user.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
