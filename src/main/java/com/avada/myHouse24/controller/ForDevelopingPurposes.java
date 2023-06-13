package com.avada.myHouse24.controller;

import com.avada.myHouse24.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Slf4j
@Controller
public class ForDevelopingPurposes {
    @GetMapping("/secured")
    public String secured() {
        return "redirect:/admin/account-transaction/index";
    }
}
