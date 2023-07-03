package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.mapper.AdditionalServiceMapper;
import com.avada.myHouse24.model.AdditionalServiceDTO;
import com.avada.myHouse24.model.AdditionalServiceListDTO;
import com.avada.myHouse24.services.impl.AdditionalServiceImpl;
import com.avada.myHouse24.services.impl.UnitOfMeasurementServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Log4j2
@Controller
@RequestMapping("/admin/service")
@RequiredArgsConstructor
public class AdditionalServiceController {
    private final AdditionalServiceMapper additionalServiceMapper;
    private final AdditionalServiceImpl additionalServiceImpl;
    private final UnitOfMeasurementServiceImpl unitOfMeasurementService;
    @GetMapping("/index")
    public String getAll(Model model){
        model.addAttribute("services", additionalServiceMapper.toDtoList(additionalServiceImpl.getAll()));
        model.addAttribute("units", unitOfMeasurementService.getAll());
        return "admin/service/get-all";
    }
    @PostMapping("/add")
    public String add(@ModelAttribute("additionalServiceListDTO")AdditionalServiceListDTO additionalServiceListDTO, Model model){
        boolean valid = false;
        for (AdditionalServiceDTO service : additionalServiceListDTO.getServices()) {
            if(service.getName() == null || service.getName().isBlank()){
                service.setError("Ім'я повино бути вказано");
                valid = true;
            }
            if(service.getUnitOfMeasurementName().equals("Вибрати") || service.getUnitOfMeasurementName().isBlank()){
                service.setError("Одиниці вимірювання повинні бути вказані");
                valid = true;
            }
        }
        if (valid){
            model.addAttribute("services", additionalServiceListDTO.getServices());
            model.addAttribute("units", unitOfMeasurementService.getAll());
            return "admin/service/get-all";
        }
        return "redirect:/admin/service/index";
    }
}