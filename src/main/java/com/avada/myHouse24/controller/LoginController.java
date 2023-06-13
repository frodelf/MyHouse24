package com.avada.myHouse24.controller;

import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.services.registration.RegistrationRequest;
import com.avada.myHouse24.services.registration.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@AllArgsConstructor
public class LoginController {
    private UserServiceImpl userServiceImpl;
    private RegistrationService registrationService;

    @GetMapping("/cabinet/registration")
    public String registerPage() {
        return "main/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute RegistrationRequest registrationRequest) {
        if (userServiceImpl.findUserByEmail(registrationRequest.getEmail())==null) {
            log.info("IN registerUser: user {} was added", registrationRequest.getEmail());
            registrationService.register(registrationRequest);
        }
        return "redirect:/cabinet/login";
    }
//
//    @PostMapping("/cabinet/login/p")
//    public String login(@RequestParam("username") String username,
//                        @RequestParam("password") String password,
//                        HttpSession session) {
//        log.info("IN login: Executing POST method");
//        User user = userService.findUserByEmail(username);
//
//        if (user != null && user.isEnabled() && userService.verifyPassword(user, password)) {
//            log.info("IN login user: {}", user.getEmail());
//            session.setAttribute("user", user);
//            return "redirect:/secured";
//        } else {
//            log.info("IN login: Something went wrong");
//            return "redirect:/cabinet/login?error";
//        }
//    }

    @GetMapping("/login/confirm")
    public String confirmRegister(@RequestParam String token) {
        log.info("IN confirmRegister:{}", token);
        if (token == null || !registrationService.confirm(token)) {
            log.info("IN confirmRegister: token is wrong or null: {}", token);
        }
        return "redirect:/cabinet/login";
    }

    @GetMapping("/cabinet/login")
    public String login() {
        return "/main/login";
    }
}
