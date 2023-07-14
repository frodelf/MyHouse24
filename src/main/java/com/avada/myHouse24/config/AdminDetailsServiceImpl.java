package com.avada.myHouse24.config;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.repo.AdminRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class AdminDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            log.info("IN AdminDetailsServiceImpl user not found");
            throw new UsernameNotFoundException("User not found");
        }
        log.info("IN AdminDetailsServiceImpl user found{}", admin.getEmail());
        return admin;
    }
}
