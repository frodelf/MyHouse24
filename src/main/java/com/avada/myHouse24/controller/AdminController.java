package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.mapper.AdminMapper;
import com.avada.myHouse24.model.AdminForAddDTO;
import com.avada.myHouse24.model.AdminForViewDTO;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.RoleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin/user-admin/")
@RequiredArgsConstructor
public class AdminController {
    private final RoleServiceImpl roleService;
    private final AdminServiceImpl adminService;
    private final AdminMapper adminMapper;
    @GetMapping("/index/{id}")
    public String getAll(Model model, @PathVariable("id")int id){
        model.addAttribute("allStatus", UserStatus.values());
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("filter", adminMapper.toDtoForView(new Admin()));
        model.addAttribute("admins", adminMapper.toDtoListForView(adminService.getPage(id, model).getContent()));
        return "admin/user-admin/get-all";
    }
    @GetMapping("/create")
    public String add(@ModelAttribute("admin") AdminForAddDTO admin, Model model){
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("statuses", UserStatus.values());
        return "admin/user-admin/add";
    }
    @PostMapping("/create")
    public String add(@ModelAttribute("admin") @Valid AdminForAddDTO admin, BindingResult bindingResult, Model model){
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
        return "redirect:/admin/user-admin/index/1";
    }
    @GetMapping("/{id}")
    public String index(@PathVariable("id")long id, Model model){
        model.addAttribute("admin", adminMapper.toDtoForView(adminService.getById(id)));
        return "admin/user-admin/index";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")long id){
        Admin admin = adminService.getById(id);
        admin.setStatus(UserStatus.DISABLED);
        adminService.save(admin);
        return "redirect:/admin/user-admin/index/1";
    }
    @GetMapping("/edit/{id}")
    public String edit(@ModelAttribute("adminModel") AdminForAddDTO admin, @PathVariable("id")long id, Model model){
        model.addAttribute("admin", adminMapper.toDtoForAdd(adminService.getById(id)));
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("statuses", UserStatus.values());
        return "admin/user-admin/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("adminModel") @Valid AdminForAddDTO admin, BindingResult bindingResult, @PathVariable("id")long id, Model model){
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
        return "redirect:/admin/user-admin/index/1";
    }
    @GetMapping("/filter/{id}")
    public String filter(@ModelAttribute AdminForViewDTO adminForViewDto, @PathVariable("id")int id, Model model){
        model.addAttribute("allStatus", UserStatus.values());
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("filter", adminForViewDto);
        model.addAttribute("admins", adminService.getPage(id, model, adminService.filter(adminForViewDto, adminMapper.toDtoListForView(adminService.getAll()))).getContent());
        return "admin/user-admin/get-all";
    }
}
