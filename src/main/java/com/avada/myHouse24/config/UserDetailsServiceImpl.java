package com.avada.myHouse24.config;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow();
        if (user == null) {
            log.info("IN UserDetailsServiceImpl user found");
            throw new UsernameNotFoundException("User not found");
        }
        log.info("IN UserDetailsServiceImpl user found{}", user.getEmail());
        return user;
    }
}
