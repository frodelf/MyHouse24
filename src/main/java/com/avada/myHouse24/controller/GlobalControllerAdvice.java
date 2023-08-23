package com.avada.myHouse24.controller;

import com.avada.myHouse24.enums.Theme;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final AdminServiceImpl adminService;
    @ModelAttribute("theme")
    public String theme(){
        if(adminService.getAuthAdmin().getTheme()==null)adminService.getAuthAdmin().setTheme(Theme.LIGHT);
        return adminService.getAuthAdmin().getTheme().name();
    }
}