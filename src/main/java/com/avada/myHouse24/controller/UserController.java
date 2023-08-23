package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.mapper.UserMapper;
import com.avada.myHouse24.model.Select2Option;
import com.avada.myHouse24.model.UserForAddDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.services.impl.AmazonS3Service;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.RoleServiceImpl;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.services.registration.EmailService;
import com.avada.myHouse24.util.ImageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final AmazonS3Service amazonS3Service;
    private final HouseServiceImpl houseService;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final FlatMapper flatMapper;


    @GetMapping("/index/{id}")
    public String getAll(@PathVariable("id")int id, Model model) {
        UserForViewDTO forFilter = userMapper.toDtoForView(new User());
        forFilter.setIsDebt(null);
        model.addAttribute("filter", forFilter);
        model.addAttribute("users", userMapper.toDtoListForView(userService.getPage(id, model).getContent()));
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
        user.setImage(amazonS3Service.uploadFile(image));
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
        User userResult = userMapper.toEntityForAdd(userDTO);
        if (!userDTO.getPassword().equals("")) {
            if (!userDTO.getPassword().equals(userDTO.getPasswordAgain())) {
                model.addAttribute("status", UserStatus.values());
                model.addAttribute("passwordAgainError", "Паролі не співпадають");
                return "admin/user/add";
            }
            userResult.setPassword(userDTO.getPassword());
        }
        amazonS3Service.deleteFile(userService.getById(userResult.getId()).getImage());
        userResult.setImage(amazonS3Service.uploadFile(image));
        userService.save(userResult);
        return "redirect:/admin/user/index/1";
    }

    @GetMapping("/{id}")
    public String index(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userMapper.toDtoForAdd(userService.getById(id)));
        model.addAttribute("flats", flatMapper.toDtoList(userService.getById(id).getFlats()));
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
    public String filter(@ModelAttribute UserForViewDTO userForViewDTO, @RequestParam(value = "dateTest", required = false, defaultValue = "1000-01-01") Date date, @RequestParam(value = "house", defaultValue = "")String house,
                         @RequestParam(value = "flat", defaultValue = "")String flat, @PathVariable("page")int page, Model model) {
        model.addAttribute("filter", userForViewDTO);
        model.addAttribute("users", userService.getPage(page, model, userService.filter(userForViewDTO, userMapper.toDtoListForView(userService.getAll()), date, flat, house)));
        model.addAttribute("userCount", userService.getAll().size());
        model.addAttribute("houses", houseService.getAllName());
        if(!house.isBlank())model.addAttribute("flats", houseService.getByName(house).getFlats());
        model.addAttribute("house", house);
        model.addAttribute("flat", flat);
        model.addAttribute("allStatus", UserStatus.values());
        return "admin/user/get-all";
    }
    @GetMapping("/get-users")
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam("_page") int page,
                                                            @RequestParam("_search") String search){
        int pageSize = 10;
        List<User> users = userService.forSelect(page, pageSize, search);
        List<Select2Option> select2Options = new ArrayList<>();
        for (User user : users) {
            select2Options.add(new Select2Option(user.getId(), user.getFirstName()));
        }
        int totalResults = 10;
        Map<String, Object> response = new HashMap<>();
        response.put("results", select2Options);
        response.put("pagination", Map.of("more", (page * pageSize) < totalResults));
        return ResponseEntity.ok(response);
    }
}
