package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.entity.UnitOfMeasurement;
import com.avada.myHouse24.mapper.AdditionalServiceMapper;
import com.avada.myHouse24.mapper.UnitOfMeasurementMapper;
import com.avada.myHouse24.model.AdditionalServiceDTO;
import com.avada.myHouse24.model.AdditionalServiceListDTO;
import com.avada.myHouse24.model.UnitOfMeasurementDTO;
import com.avada.myHouse24.services.impl.AdditionalServiceImpl;
import com.avada.myHouse24.services.impl.UnitOfMeasurementServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
        List<UnitOfMeasurement> unitOfMeasurements = unitOfMeasurementService.getAll();
        model.addAttribute("services", additionalServiceMapper.toDtoList(additionalServiceImpl.getAll()));
        model.addAttribute("units", unitOfMeasurementMapper.toDtoList(unitOfMeasurementService.getAll()));
        return "admin/service/get-all";
    }
    @PostMapping("/add")
    public String add(@ModelAttribute("additionalServiceListDTO")AdditionalServiceListDTO additionalServiceListDTO, Model model){
        boolean valid = false;
        if(additionalServiceListDTO.getServices() != null)for (AdditionalServiceDTO service : additionalServiceListDTO.getServices()) {
            if(service.getName() == null || service.getName().isBlank()){
                service.setError("Ім'я повино бути вказано");
                valid = true;
            }
            if(service.getUnitOfMeasurementName() == null || service.getUnitOfMeasurementName().equals("Вибрати") || service.getUnitOfMeasurementName().isBlank()){
                service.setError("Одиниці вимірювання повинні бути вказані");
                valid = true;
            }
        }
        if(additionalServiceListDTO.getUnitOfMeasurementNames() != null)for (UnitOfMeasurementDTO unitOfMeasurementName : additionalServiceListDTO.getUnitOfMeasurementNames()) {
            if(unitOfMeasurementName.getName() == null  ||  unitOfMeasurementName.getName().isBlank()){
                unitOfMeasurementName.setError("Назва повина бути вказано");
                valid = true;
            }
        }
        if (valid){
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
}