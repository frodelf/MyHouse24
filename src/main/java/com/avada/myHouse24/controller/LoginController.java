package com.avada.myHouse24.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
@AllArgsConstructor
public class LoginController {
    @GetMapping("/admin/login")
    public String loginAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "/main/loginAdmin";
        }
        log.info("User is already authenticated.");
        log.info(String.valueOf(authentication.isAuthenticated()));
        log.info(String.valueOf(authentication.getName()));
        return "redirect:/admin";
    }
    @GetMapping("/")
    public String admin() {
        return "redirect:/admin";
    }
}
