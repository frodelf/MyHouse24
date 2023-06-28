package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.mapper.UserMapper;
import com.avada.myHouse24.model.UserForAddDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.RoleServiceImpl;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.services.registration.EmailService;
import com.avada.myHouse24.util.ImageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final HouseServiceImpl houseService;
    private final UserMapper userMapper;
    private final EmailService emailService;


    @GetMapping("/index/{id}")
    public String getAll(@PathVariable("id")int id, Model model) {
        UserForViewDTO forFilter = userMapper.toDtoForView(new User());
        forFilter.setIsDebt(null);
        model.addAttribute("filter", forFilter);
        model.addAttribute("users", userMapper.toDtoListForView(userService.getPage(id-1, model).getContent()));
        model.addAttribute("userCount", userService.getAll().size());
        model.addAttribute("houses", houseService.getAllName());
        model.addAttribute("allStatus", UserStatus.values());
        return "admin/user/get-all";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("userDTO") UserForAddDTO userDTO, Model model) {
        model.addAttribute("maxId", userService.getMaxId());
        model.addAttribute("status", UserStatus.values());
        return "admin/user/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userDTO") @Valid UserForAddDTO userDTO, BindingResult bindingResult, Model model,
                      @RequestParam("image") MultipartFile image) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("status", UserStatus.values());
            if (userDTO.getPassword().equals("")) model.addAttribute("passwordError", "Пароль має бути вказаний");
            if (image.isEmpty()) model.addAttribute("error", "Фото повино бути загружено");
            if (!userDTO.getPassword().equals(userDTO.getPasswordAgain()))
                model.addAttribute("passwordAgainError", "Паролі не співпадають");
            return "admin/user/add";
        }
        if (!userDTO.getPassword().equals(userDTO.getPasswordAgain()) || userDTO.getPassword().equals("")) {
            if (userDTO.getPassword().equals("")) model.addAttribute("passwordError", "Пароль має бути вказаний");
            model.addAttribute("status", UserStatus.values());
            if (!userDTO.getPassword().equals(userDTO.getPasswordAgain()))
                model.addAttribute("passwordAgainError", "Паролі не співпадають");
            return "admin/user/add";
        }
        User user = userMapper.toEntityForAdd(userDTO);
        user.setPassword(userDTO.getPassword());
        user.setStatus(UserStatus.NEW);
        user.setFromDate(Date.valueOf(LocalDate.now()));
        user.setRoles(roleService.getById(1));
        user.setImage(ImageUtil.imageForUser(user, image));
        userService.save(user);
        return "redirect:/admin/user/index/1";
    }

    @GetMapping("/edit/{id}")
    public String edit(@ModelAttribute("userDTO") UserForAddDTO userDTO, @PathVariable("id") long id, Model model) {
        model.addAttribute("status", UserStatus.values());
        model.addAttribute("user", userMapper.toDtoForAdd(userService.getById(id)));
        return "admin/user/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("userDTO") @Valid UserForAddDTO userDTO, BindingResult bindingResult, @PathVariable("id") long id, Model model,
                       @RequestParam("image") MultipartFile image) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("status", UserStatus.values());
            model.addAttribute("user", userMapper.toDtoForAdd(userService.getById(id)));
            return "admin/user/edit";
        }
        User user = userService.getById(id);
        User userResult = userMapper.toEntityForAdd(userDTO);
        if (!userDTO.getPassword().equals("")) {
            if (!userDTO.getPassword().equals(userDTO.getPasswordAgain())) {
                model.addAttribute("status", UserStatus.values());
                model.addAttribute("passwordAgainError", "Паролі не співпадають");
                return "admin/user/add";
            }
            user.setPassword(userDTO.getPassword());
        }
        userResult.setPassword(user.getPassword());
        userResult.setImage(ImageUtil.imageForUser(user, image));
        userService.save(userResult);
        return "redirect:/admin/user/index/1";
    }

    @GetMapping("/{id}")
    public String index(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userMapper.toDtoForAdd(userService.getById(id)));
        return "admin/user/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        User user = userService.getById(id);
        user.setStatus(UserStatus.DISABLED);
        userService.save(user);
        return "redirect:/admin/user/index/1";
    }

    @GetMapping("/invite")
    public String inviteMessage() {
        return "admin/user/invite";
    }

    @PostMapping("/invite")
    public String inviteMessage(@RequestParam("phone") String phone, @RequestParam("email") String email, Model model) {
        if (email.isBlank()) {
            model.addAttribute("error", "Електрона адреса повина бути вказана");
            return "admin/user/invite";
        }
        emailService.send(email, "invite");
        return "redirect:/admin/user/index/1";
    }

    @GetMapping("/filter/{page}")
    public String filter(@ModelAttribute UserForViewDTO userForViewDTO, @RequestParam(value = "dateTest", required = false, defaultValue = "1000-01-01") Date date, @RequestParam(value = "houses", defaultValue = "")String house,
                         @RequestParam(value = "flat", defaultValue = "")String flat, @PathVariable("page")int page, Model model) {
        if (date.equals(new Date(2023, 01, 01)))
            userForViewDTO.setDate(date);

        List<UserForViewDTO> users = userMapper.toDtoListForView(userService.getAll());
        if(date != null && date.getYear() != -900){
            userForViewDTO.setDate(date);
        }
        if (!userForViewDTO.getId().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getId() != null && dto.getId().contains(userForViewDTO.getId()))
                    .collect(Collectors.toList());
        }

        if (!userForViewDTO.getFullName().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getFullName() != null && dto.getFullName().contains(userForViewDTO.getFullName()))
                    .collect(Collectors.toList());
        }

        if (!userForViewDTO.getPhone().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getPhone() != null && dto.getPhone().contains(userForViewDTO.getPhone()))
                    .collect(Collectors.toList());
        }

        if (!userForViewDTO.getEmail().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getEmail() != null && dto.getEmail().contains(userForViewDTO.getEmail()))
                    .collect(Collectors.toList());
        }

        if (!house.isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getHouses().containsAll(userForViewDTO.getHouses()))
                    .collect(Collectors.toList());
        }

        if (!flat.isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getFlats().containsAll(userForViewDTO.getFlats()))
                    .collect(Collectors.toList());
        }
        if (userForViewDTO.getDate() != null) {
            users = users.stream()
                    .filter(dto -> dto.getDate() != null && dto.getDate().equals(date))
                    .collect(Collectors.toList());
        }

        if (!userForViewDTO.getStatus().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getStatus() != null && dto.getStatus().equals(userForViewDTO.getStatus()))
                    .collect(Collectors.toList());
        }

        if (userForViewDTO.getIsDebt() != null) {
            users = users.stream()
                    .filter(dto -> dto.getIsDebt() != null && dto.getIsDebt().equals(userForViewDTO.getIsDebt()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("filter", userForViewDTO);
        model.addAttribute("users", userService.getPage(page-1, model, users));
        model.addAttribute("userCount", userService.getAll().size());
        model.addAttribute("houses", houseService.getAllName());
        if(!house.isBlank())model.addAttribute("flats", houseService.getByName(house).getFlats());
        model.addAttribute("house", house);
        model.addAttribute("flat", flat);
        model.addAttribute("allStatus", UserStatus.values());
        return "admin/user/get-all";
    }
}
