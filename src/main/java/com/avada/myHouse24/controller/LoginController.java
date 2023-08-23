package com.avada.myHouse24.controller;

import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.services.registration.RegistrationRequest;
import com.avada.myHouse24.services.registration.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@AllArgsConstructor
public class LoginController {
    @GetMapping("/admin/login")
    public String loginAdmin() {
        return "/main/loginAdmin";
    }

}
