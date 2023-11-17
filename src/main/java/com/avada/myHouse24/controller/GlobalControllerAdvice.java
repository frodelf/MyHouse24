package com.avada.myHouse24.controller;

import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final AdminServiceImpl adminService;
    private final UserServiceImpl userService;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @ModelAttribute("context_path")
    public String addContextPathToModel() {
        return contextPath;
    }

    @ModelAttribute("theme")
    public String theme(){
        if(adminService.getAuthAdmin().getTheme()==null)
            return "LIGHT";
        return adminService.getAuthAdmin().getTheme().name();
    }
}