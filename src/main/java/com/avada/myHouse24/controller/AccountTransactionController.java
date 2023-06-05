package com.avada.myHouse24.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Log4j2
@Controller
@RequestMapping("/admin/account-transaction")
public class AccountTransactionController {
    @GetMapping("/index")
    public String index(Model model){
//        model.addAttribute("account-transaction-list", );
        return "admin/account-transaction";
    }
}
