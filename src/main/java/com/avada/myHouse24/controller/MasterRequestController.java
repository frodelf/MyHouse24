package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.MasterRequest;
import com.avada.myHouse24.enums.MasterRequestStatus;
import com.avada.myHouse24.mapper.MasterRequestMapper;
import com.avada.myHouse24.model.MasterRequestDTO;
import com.avada.myHouse24.services.impl.FlatServiceImpl;
import com.avada.myHouse24.services.impl.MasterRequestServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/master-request")
@RequiredArgsConstructor
public class MasterRequestController {
    private final FlatServiceImpl flatService;
    private final MasterRequestServiceImpl masterRequestService;
    private final MasterRequestMapper masterRequestMapper;
    @GetMapping("/index/{page}")
    public ModelAndView index(@PathVariable("page")int page){
        ModelAndView modelAndView = new ModelAndView("admin/master-request/get-all");
        modelAndView.addObject("masters", masterRequestService.getPage(page, modelAndView));
        return modelAndView;
    }
    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute("master")MasterRequestDTO masterRequest){
        ModelAndView modelAndView = new ModelAndView("admin/master-request/add");
        modelAndView.addObject("statuses", MasterRequestStatus.values());
        modelAndView.addObject("flats", flatService.getAll());
        return modelAndView;
    }
    @PostMapping("/add")
    public ModelAndView add(@ModelAttribute("master") @Valid MasterRequestDTO masterRequestDTO, BindingResult bindingResult){
        ModelAndView modelAndView;
        if(bindingResult.hasErrors()){
            modelAndView = new ModelAndView("admin/master-request/add");
            modelAndView.addObject("statuses", MasterRequestStatus.values());
            modelAndView.addObject("flats", flatService.getAll());
            return modelAndView;
        }
        masterRequestService.save(masterRequestMapper.toEntity(masterRequestDTO));
        modelAndView = new ModelAndView("redirect:/admin/master-request/index/1");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id")Long id){
        ModelAndView modelAndView = new ModelAndView("admin/master-request/add");
        modelAndView.addObject("master", masterRequestMapper.toDto(masterRequestService.getById(id)));
        modelAndView.addObject("statuses", MasterRequestStatus.values());
        modelAndView.addObject("flats", flatService.getAll());
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id")Long id){
        masterRequestService.deleteById(id);
        return new ModelAndView("redirect:/admin/master-request/index/1");
    }
}
