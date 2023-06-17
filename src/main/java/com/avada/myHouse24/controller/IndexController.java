package com.avada.myHouse24.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String mainPage(){
        return "/main/main_page";
    }
    @GetMapping("/about")
    public String about(){
        return "/main/about";
    }
    @GetMapping("/services")
    public String services(){
        return "/main/services";
    }
    @GetMapping("contacts")
    public String contacts(){
        return "/main/contacts";
    }

}
