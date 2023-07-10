package com.avada.myHouse24.controller;

import com.avada.myHouse24.mapper.AdditionalServiceMapper;
import com.avada.myHouse24.mapper.TariffMapper;
import com.avada.myHouse24.model.TariffDTO;
import com.avada.myHouse24.services.impl.AdditionalServiceImpl;
import com.avada.myHouse24.services.impl.TariffServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tariff")
@RequiredArgsConstructor
public class TariffController {
    private final AdditionalServiceImpl additionalService;
    private final TariffServiceImpl tariffService;
    private final AdditionalServiceMapper additionalServiceMapper;
    private final TariffMapper tariffMapper;
    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("tariffs", tariffService.getAll());
        return "admin/tariff/get-all";
    }
    @GetMapping("/add")
    public String add(@ModelAttribute("tariffDTO") TariffDTO tariffDTO, Model model){
        model.addAttribute("services", additionalServiceMapper.toDtoList(additionalService.getAll()));
        return "admin/tariff/add";
    }
    @PostMapping("/add")
    public String add(@ModelAttribute("tariffDTO") @Valid TariffDTO tariffDTO, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("services", additionalServiceMapper.toDtoList(additionalService.getAll()));
            return "admin/tariff/add";
        }
        for (int i = 0; i < tariffDTO.getNames().size(); i++) {
            tariffDTO.getAdditionalServiceForTariffDTOS().get(i).setAdditionalService(additionalService.getByName(tariffDTO.getNames().get(i)));
        }
        tariffService.save(tariffDTO);
        return "redirect:/admin/tariff/index";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Long id, Model model){
        model.addAttribute("services", additionalServiceMapper.toDtoList(additionalService.getAll()));
        model.addAttribute("tariffDTO", tariffMapper.toDto(tariffService.getById(id)));
        model.addAttribute("index", tariffMapper.toDto(tariffService.getById(id)).getAdditionalServiceForTariffDTOS().size());
        return "admin/tariff/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("tariffDTO") @Valid TariffDTO tariffDTO, BindingResult bindingResult, @PathVariable("id")Long id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("services", additionalServiceMapper.toDtoList(additionalService.getAll()));
            model.addAttribute("tariffDTO", tariffMapper.toDto(tariffService.getById(id)));
            return "admin/tariff/edit";
        }
        for (int i = 0; i < tariffDTO.getNames().size(); i++) {
            tariffDTO.getAdditionalServiceForTariffDTOS().get(i).setAdditionalService(additionalService.getByName(tariffDTO.getNames().get(i)));
        }
        tariffService.save(tariffDTO);
        return "redirect:/admin/tariff/index";
    }
    @GetMapping("/{id}")
    public String getById(@PathVariable("id")Long id, Model model){
        model.addAttribute("tariffDTO", tariffMapper.toDto(tariffService.getById(id)));
        model.addAttribute("dateEdit", tariffService.getById(id).getDateEdit());
        return "/admin/tariff/getById";
    }
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id")Long id){
        tariffService.deleteById(id);
        return "redirect:/admin/tariff/index";
    }
    @GetMapping("/clone/{id}")
    public String cloneById(@PathVariable("id")Long id, Model model) {
        TariffDTO tariffDTO = tariffMapper.toDto(tariffService.getById(id));
        tariffDTO.setId(null);
        model.addAttribute("tariffDTO", tariffDTO);
        model.addAttribute("services", additionalServiceMapper.toDtoList(additionalService.getAll()));
        model.addAttribute("index", tariffMapper.toDto(tariffService.getById(id)).getAdditionalServiceForTariffDTOS().size());
        return "/admin/tariff/edit";
    }
    @PostMapping("/copy")
    public String cloneById(@ModelAttribute("tariffDTO") @Valid TariffDTO tariffDTO, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("tariffDTO", tariffDTO);
            model.addAttribute("services", additionalServiceMapper.toDtoList(additionalService.getAll()));
            model.addAttribute("index", tariffDTO.getAdditionalServiceForTariffDTOS().size());
            return "admin/tariff/edit";
        }
        for (int i = 0; i < tariffDTO.getNames().size(); i++) {
            tariffDTO.getAdditionalServiceForTariffDTOS().get(i).setAdditionalService(additionalService.getByName(tariffDTO.getNames().get(i)));
        }
        tariffService.save(tariffDTO);
        return "redirect:/admin/tariff/index";
    }
}
