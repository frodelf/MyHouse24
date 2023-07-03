package com.avada.myHouse24.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/admin/pay-company")
@RequiredArgsConstructor
public class PayCompanyController {
    @GetMapping("/index")
    public String index(){
        return "admin/pay-company/index";
    }

    @PostMapping("/index")
    public String indexPost(){
        return "redirect:/admin/pay-company/index";
    }
}
