package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.model.CounterDataFilterDto;
import com.avada.myHouse24.model.Select2Option;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/admin/counter-data/")
@RequiredArgsConstructor
public class CounterDataController {
    private final HouseServiceImpl houseService;
    @GetMapping("/index/{id}")
    public String getAll(@PathVariable("id")Long id, Model model){
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("filter", new CounterDataFilterDto());
        return "/admin/counter-data/get-all";
    }
    @GetMapping("/filter/{page}")
    public String filter(@ModelAttribute("filter")CounterDataFilterDto filter, Model model){
        model.addAttribute("houses", houseService.getAll());
        return "/admin/counter-data/get-all";
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
}
