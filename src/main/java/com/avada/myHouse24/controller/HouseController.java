package com.avada.myHouse24.controller;

import com.avada.myHouse24.mapper.HouseMapper;
import com.avada.myHouse24.model.HouseForAddDto;
import com.avada.myHouse24.model.HouseForViewDto;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.RoleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin/house")
@RequiredArgsConstructor
public class HouseController {
    private final HouseServiceImpl houseService;
    private final AdminServiceImpl adminService;
    private final HouseMapper houseMapper;
    private final RoleServiceImpl roleService;

    @GetMapping("/index/{page}")
    public String getAll(@PathVariable("page") int page, Model model) {
        model.addAttribute("houses", houseMapper.toDtoForViewList(houseService.getPage(page - 1, model).getContent()));
        model.addAttribute("filter", new HouseForViewDto());
        model.addAttribute("housesCount", houseService.getAll().size());
        return "admin/house/get-all";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("house") HouseForAddDto house, Model model) {
        model.addAttribute("users", adminService.getAll());
        return "admin/house/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("house") @Valid HouseForAddDto house, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", adminService.getAll());
            return "admin/house/add";
        }
        houseService.add(house);
        return "admin/house/add";
    }

    @GetMapping("/filter/{page}")
    public String filter(@PathVariable("page") int page, @ModelAttribute("house") HouseForViewDto house, Model model) {
        List<HouseForViewDto> houses = houseMapper.toDtoForViewList(houseService.getAll());
        if (!house.getName().isBlank()) {
            houses = houses.stream()
                    .filter(dto -> dto.getName() != null && dto.getName().contains(house.getName()))
                    .collect(Collectors.toList());
        }
        if (!house.getAddress().isBlank()) {
            houses = houses.stream()
                    .filter(dto -> dto.getAddress() != null && dto.getAddress().contains(house.getAddress()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("houses", houseService.getPage(page, model, houses));
        model.addAttribute("filter", house);
        model.addAttribute("housesCount", houseService.getAll().size());
        return "admin/house/get-all";
    }
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") long id) {
        houseService.deleteById(id);
        return "redirect:/admin/house/index/1";
    }
    @GetMapping("/{id}")
    public String index(@PathVariable("id") long id, Model model) {
        model.addAttribute("house", houseService.getById(id));
        return "admin/house/index";
    }
}