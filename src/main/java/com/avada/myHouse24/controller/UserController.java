package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.mapper.UserMapper;
import com.avada.myHouse24.model.UserForAddDTO;
import com.avada.myHouse24.services.impl.RoleServiceImpl;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.util.IdUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

@Log4j2
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final UserMapper userMapper;

    @GetMapping("/index")
    public String getAll(Model model) {
        model.addAttribute("users", userMapper.toDtoListForView(userService.getAll()));
        model.addAttribute("userCount", userService.getAll().size());
        return "admin/user/get-all";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("userDTO") UserForAddDTO userDTO, Model model) {
        model.addAttribute("maxId", IdUtil.fromIdToString(userService.getMaxId()));
        model.addAttribute("status", UserStatus.values());
        return "admin/user/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userDTO") @Valid UserForAddDTO userDTO, BindingResult bindingResult, Model model,
                      @RequestParam("image") MultipartFile image) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("status", UserStatus.values());
            if (userDTO.getPassword().equals("")) model.addAttribute("passwordError", "Пароль має бути вказаний");
            if (image.isEmpty()) model.addAttribute("error", "Фото повино бути загружено");
            if (!userDTO.getPassword().equals(userDTO.getPasswordAgain()))
                model.addAttribute("passwordAgainError", "Паролі не співпадають");
            if (userService.existsById(IdUtil.fromStringToId(userDTO.getId())))
                model.addAttribute("idError", "Користувач з таким id вже існує");
            return "admin/user/add";
        }
        if (!userDTO.getPassword().equals(userDTO.getPasswordAgain()) || userDTO.getPassword().equals("")) {
            if (userDTO.getPassword().equals("")) model.addAttribute("passwordError", "Пароль має бути вказаний");
            model.addAttribute("status", UserStatus.values());
            if (!userDTO.getPassword().equals(userDTO.getPasswordAgain()))
                model.addAttribute("passwordAgainError", "Паролі не співпадають");
            return "admin/user/add";
        }
        if (userService.existsById(IdUtil.fromStringToId(userDTO.getId()))) {
            model.addAttribute("status", UserStatus.values());
            model.addAttribute("idError", "Користувач з таким id вже існує");
            return "admin/user/add";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userMapper.toEntityForAdd(userDTO);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setStatus(UserStatus.NEW);
        user.setFromDate(Date.valueOf(LocalDate.now()));
        user.setRoles(Arrays.asList(roleService.getById(1)));
        userService.save(user);
        return "redirect:/admin/user/index";
    }

    @GetMapping("/edit/{id}")
    public String edit(@ModelAttribute("userDTO") UserForAddDTO userDTO, @PathVariable("id") long id, Model model) {
        model.addAttribute("status", UserStatus.values());
        model.addAttribute("user", userMapper.toDtoForAdd(userService.getById(id)));
        return "admin/user/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("userDTO") @Valid UserForAddDTO userDTO, BindingResult bindingResult, @PathVariable("id") long id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("status", UserStatus.values());
            model.addAttribute("user", userMapper.toDtoForAdd(userService.getById(id)));
            return "admin/user/edit";
        }
        User user = userService.getById(id);
        User userResult = userMapper.toEntityForAdd(userDTO);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!userDTO.getPassword().equals("")) {
            if (!userDTO.getPassword().equals(userDTO.getPasswordAgain())) {
                model.addAttribute("status", UserStatus.values());
                model.addAttribute("passwordAgainError", "Паролі не співпадають");
                return "admin/user/add";
            }
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }
        if (userService.existsById(IdUtil.fromStringToId(userDTO.getId())) && !userDTO.getId().equals(IdUtil.fromIdToString(id))) {
            model.addAttribute("status", UserStatus.values());
            model.addAttribute("idError", "Користувач з таким id вже існує");
            return "admin/user/add";
        }
        userResult.setPassword(user.getPassword());
        userService.save(userResult);
        return "redirect:/admin/user/index";
    }

    @GetMapping("/{id}")
    public String index(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userMapper.toDtoForAdd(userService.getById(id)));
        return "admin/user/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")long id){
        userService.deleteById(id);
        return "redirect:/admin/user/index";
    }
}
