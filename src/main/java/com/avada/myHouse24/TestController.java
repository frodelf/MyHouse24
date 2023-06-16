package com.avada.myHouse24;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@Controller
@RequestMapping("/admin/test")
@RequiredArgsConstructor
public class TestController {
    private final UserServiceImpl userService;
    @GetMapping("/")
    public String test(){
        return "admin/test";
    }
    @PostMapping("/")
    public String test(@RequestParam("image")MultipartFile image) throws IOException {
        User user = userService.getById(1);
        ImageUtil.imageForUser(user, image);
        return "redirect:/admin/test/";
    }
}
