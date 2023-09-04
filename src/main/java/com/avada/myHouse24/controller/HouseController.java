package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.mapper.HouseMapper;
import com.avada.myHouse24.model.HouseForAddDto;
import com.avada.myHouse24.model.HouseForViewDto;
import com.avada.myHouse24.model.Select2Option;
import com.avada.myHouse24.services.impl.*;
import com.avada.myHouse24.validator.HouseValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin/house")
@RequiredArgsConstructor
public class HouseController {
    private final HouseServiceImpl houseService;
    private final HouseValidator houseValidator;
    private final FlatServiceImpl flatService;
    private final AdminServiceImpl adminService;
    private final HouseMapper houseMapper;

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
        houseValidator.validate(house, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", adminService.getAll());
            return "admin/house/add";
        }
        houseService.add(house);
        return "redirect:/admin/house/index/1";
    }

    @GetMapping("/filter/{page}")
    public String filter(@PathVariable("page") int page, @ModelAttribute("house") HouseForViewDto house, Model model) {
        model.addAttribute("houses", houseService.getPage(page, model, houseService.filter(house, houseMapper.toDtoForViewList(houseService.getAll()))));
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
    @GetMapping("/name/{name}")
    public String getByName(@PathVariable("name") String name){
        return "redirect:/admin/house/"+houseService.getByName(name).getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")long id, Model model){
        model.addAttribute("users", adminService.getAll());
        model.addAttribute("house", houseService.getById(id));
        return "admin/house/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("house") @Valid HouseForAddDto house, BindingResult bindingResult, @PathVariable("id")Long id, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", adminService.getAll());
            model.addAttribute("house", houseService.getById(id));
            return "admin/house/edit";
        }
        houseService.add(house);
        return "redirect:/admin/house/index/1";
    }
    @GetMapping("/get-houses")
    public ResponseEntity<Map<String, Object>> getAllHouses(@RequestParam("_page") int page,
                                                            @RequestParam("_search") String search){
        int pageSize = 10;
        List<House> houses = houseService.forSelect(page, pageSize, search);
        List<Select2Option> select2Options = new ArrayList<>();
        for (House house : houses) {
            select2Options.add(new Select2Option(house.getId(), house.getName()));
        }
        int totalResults = 10;
        Map<String, Object> response = new HashMap<>();
        response.put("results", select2Options);
        response.put("pagination", Map.of("more", (page * pageSize) < totalResults));
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getHouseByFlat/{id}")
    @ResponseBody
    public Select2Option getHouseById(@PathVariable("id")long id){
        House house = flatService.getById(id).getHouse();
        return new Select2Option(house.getId(), house.getName());
    }
    @GetMapping("/getAll/{page}")
    @ResponseBody
    public List<HouseForViewDto> getPage (@PathVariable("page")int id, Model model){
        return houseMapper.toDtoForViewList(houseService.getPage(id, model).getContent());
    }
    @PostMapping("/filterGet/{page}")
    @ResponseBody
    public List<HouseForViewDto> filterPage (@PathVariable("page")int id, Model model, @ModelAttribute("house1") HouseForViewDto house){
        List<HouseForViewDto> result = houseService.getPage(id, model, houseService.filter(house, houseService.filter(house, houseMapper.toDtoForViewList(houseService.getAll())))).getContent();
        return result;
    }
}