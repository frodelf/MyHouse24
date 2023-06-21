package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.mapper.AdminMapper;
import com.avada.myHouse24.model.AdminForAddDto;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping("/admin/user-admin/")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceImpl adminService;
    private final AdminMapper adminMapper;
    @GetMapping("/index")
    public String getAll(Model model){
        model.addAttribute("admins", adminMapper.toDtoListForView(adminService.getAll()));
        return "admin/user-admin/get-all";
    }
    @GetMapping("/create")
    public String add(@ModelAttribute("admin") AdminForAddDto admin){
        return "admin/user-admin/add";
    }
    @PostMapping("/create")
    public String add(@ModelAttribute("admin") @Valid AdminForAddDto admin, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            if(admin.getPassword().isBlank())model.addAttribute("passwordError", "Поле не повинно бути пустим");
            if(!admin.getPassword().equals(admin.getPasswordAgain()))model.addAttribute("passwordAgainError", "Паролі не співпадають");
            return "admin/user-admin/add";
        }
        if(admin.getPassword().isBlank()  ||  !admin.getPassword().equals(admin.getPasswordAgain())){
            if(admin.getPassword().isBlank())model.addAttribute("passwordError", "Поле не повинно бути пустим");
            if(!admin.getPassword().equals(admin.getPasswordAgain()))model.addAttribute("passwordAgainError", "Паролі не співпадають");
            return "admin/user-admin/add";
        }
        Admin result = adminMapper.toEntityForAdd(admin);
        adminService.save(result);
        return "redirect:/admin/user-admin/index";
    }
    @GetMapping("/{id}")
    public String index(@PathVariable("id")long id, Model model){
        model.addAttribute("admin", adminMapper.toDtoForView(adminService.getById(id)));
        return "admin/user-admin/index";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")long id){
        Admin admin = adminService.getById(id);
        admin.setStatus("DISABLED");
        adminService.save(admin);
        return "redirect:/admin/user-admin/index";
    }
    @GetMapping("/edit/{id}")
    public String edit(@ModelAttribute("adminModel") AdminForAddDto admin, @PathVariable("id")long id, Model model){
        model.addAttribute("admin", adminMapper.toDtoForAdd(adminService.getById(id)));
        return "admin/user-admin/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("adminModel") @Valid AdminForAddDto admin, BindingResult bindingResult, @PathVariable("id")long id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("admin", adminMapper.toDtoForAdd(adminService.getById(id)));
            if(!admin.getPassword().isBlank() && !admin.getPassword().equals(admin.getPasswordAgain()))model.addAttribute("passwordAgainError", "Паролі не співпадають");
            return "admin/user-admin/edit";
        }
        Admin result = adminService.getById(id);
        if(admin.getPassword().isBlank())admin.setPassword(result.getPassword());
        admin.setId(id);
        result = adminMapper.toEntityForAdd(admin);
        adminService.save(result);
        return "redirect:/admin/user-admin/index";
    }
}
