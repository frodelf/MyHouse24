package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.UnitOfMeasurement;
import com.avada.myHouse24.mapper.AdditionalServiceMapper;
import com.avada.myHouse24.mapper.UnitOfMeasurementMapper;
import com.avada.myHouse24.model.AdditionalServiceDTO;
import com.avada.myHouse24.model.AdditionalServiceListDTO;
import com.avada.myHouse24.model.Select2Option;
import com.avada.myHouse24.model.UnitOfMeasurementDTO;
import com.avada.myHouse24.services.impl.AdditionalServiceImpl;
import com.avada.myHouse24.services.impl.UnitOfMeasurementServiceImpl;
import com.avada.myHouse24.validator.AdditionalServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin/service")
@RequiredArgsConstructor
public class AdditionalServiceController {
    private final AdditionalServiceMapper additionalServiceMapper;
    private final AdditionalServiceImpl additionalServiceImpl;
    private final UnitOfMeasurementServiceImpl unitOfMeasurementService;
    private final UnitOfMeasurementMapper unitOfMeasurementMapper;

    @GetMapping("/index")
    public String getAll(Model model){
        model.addAttribute("services", additionalServiceMapper.toDtoList(additionalServiceImpl.getAll()));
        model.addAttribute("units", unitOfMeasurementMapper.toDtoList(unitOfMeasurementService.getAll()));
        return "admin/service/get-all";
    }
    @PostMapping("/add")
    public String add(@ModelAttribute("additionalServiceListDTO")AdditionalServiceListDTO additionalServiceListDTO, Model model){
        AdditionalServiceValidator additionalServiceValidator = new AdditionalServiceValidator();
        if (additionalServiceValidator.valid(additionalServiceListDTO)){
            model.addAttribute("services", additionalServiceListDTO.getServices());
            model.addAttribute("units", additionalServiceListDTO.getUnitOfMeasurementNames());
            return "admin/service/get-all";
        }
        unitOfMeasurementService.saveList(unitOfMeasurementMapper.toEntityList(additionalServiceListDTO.getUnitOfMeasurementNames()));
        additionalServiceImpl.saveList(additionalServiceMapper.toEntityList(additionalServiceListDTO.getServices()));
        return "redirect:/admin/service/index";
    }
    @GetMapping("/delete/{type}/{id}")
    public String delete(@PathVariable("id")long id, @PathVariable("type")String type){
        if(type.equals("service"))additionalServiceImpl.deleteById(id);
        else if(type.equals("unit"))unitOfMeasurementService.deleteById(id);
        return "redirect:/admin/service/index";
    }
    @GetMapping("/get-additional-services")
    public ResponseEntity<Map<String, Object>> getAllHouses(@RequestParam("_page") int page,
                                                            @RequestParam("_search") String search){
        int pageSize = 10;
        List<AdditionalService> additionalServices = additionalServiceImpl.forSelect(page, pageSize, search);
        List<Select2Option> select2Options = new ArrayList<>();
        for (AdditionalService additionalService : additionalServices) {
            select2Options.add(new Select2Option(additionalService.getId(), additionalService.getName()));

        }
        int totalResults = 10;
        Map<String, Object> response = new HashMap<>();
        response.put("results", select2Options);
        response.put("pagination", Map.of("more", (page * pageSize) < totalResults));
        return ResponseEntity.ok(response);
    }
}