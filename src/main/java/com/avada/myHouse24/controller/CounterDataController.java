package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.CounterData;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.enums.CounterDataStatus;
import com.avada.myHouse24.mapper.CounterDataMapper;
import com.avada.myHouse24.model.CounterDataDTO;
import com.avada.myHouse24.model.CounterDataFilterDto;
import com.avada.myHouse24.model.Select2Option;
import com.avada.myHouse24.services.impl.CounterDataServiceImpl;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.util.NumberUtil;
import com.avada.myHouse24.validator.CounterDataValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Log4j2
@Controller
@RequestMapping("/admin/counter-data/")
@RequiredArgsConstructor
public class CounterDataController {
    private final HouseServiceImpl houseService;
    private final CounterDataServiceImpl counterDataService;
    private final CounterDataMapper counterDataMapper;
    private final CounterDataValidator counterDataValidator;

    @GetMapping("/index/{id}")
    public String getAll(@PathVariable("id") int id, Model model) {
        model.addAttribute("counters", counterDataService.getPage(id, model));
        model.addAttribute("filter", new CounterDataFilterDto());
        return "/admin/counter-data/get-all";
    }

    @GetMapping("/filter/{page}")
    public String filter(@ModelAttribute("filter") CounterDataFilterDto filter, Model model, @PathVariable("page") int page) {
        counterDataService.updateFilter(filter);
        model.addAttribute("filter", filter);
        if (filter.getHouse() != null)
            model.addAttribute("sections", houseService.getById(filter.getHouse()).getSections());
        if (filter.getHouse() != null) model.addAttribute("flats", houseService.getById(filter.getHouse()).getFlats());
        counterDataService.filter(filter);
        model.addAttribute("counters", counterDataService.getPage(page, model, counterDataService.filter(filter)));
        model.addAttribute("houses", houseService.getAll());
        return "/admin/counter-data/get-all";
    }

    @GetMapping("/add")
    public String add(Model model) {
        CounterDataDTO counterDataDTO = new CounterDataDTO();
        counterDataDTO.setFromDate(Date.valueOf(LocalDate.now()));
        counterDataDTO.setNumber(NumberUtil.generateRandomNumber());
        model.addAttribute("statuses", CounterDataStatus.getAll());
        model.addAttribute("counterDataDTO", counterDataDTO);
        return "/admin/counter-data/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("counterDataDTO") CounterDataDTO counterDataDTO, BindingResult bindingResult, Model model) {
        counterDataValidator.validate(counterDataDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", CounterDataStatus.getAll());
            return "/admin/counter-data/add";
        }
        counterDataService.save(counterDataMapper.toEntity(counterDataDTO));
        return "redirect:/admin/counter-data/index/1";
    }

    @GetMapping("/copy/{id}")
    public String copy(@PathVariable("id") long id, Model model) {
        CounterDataDTO counterDataDTOForExample = counterDataMapper.toDto(counterDataService.getById(id));
        CounterDataDTO counterDataDTO = new CounterDataDTO();
        counterDataDTO.setFlat(counterDataDTOForExample.getFlat());
        counterDataDTO.setAdditionalService(counterDataDTOForExample.getAdditionalService());
        counterDataDTO.setStatus(counterDataDTOForExample.getStatus());
        counterDataDTO.setFromDate(Date.valueOf(LocalDate.now()));
        counterDataDTO.setNumber(NumberUtil.generateRandomNumber());
        model.addAttribute("statuses", CounterDataStatus.getAll());
        model.addAttribute("counterDataDTO", counterDataDTO);
        return "/admin/counter-data/add";
    }

    @PostMapping("/copy")
    public String copy(@ModelAttribute("counterDataDTO") CounterDataDTO counterDataDTO, BindingResult bindingResult, Model model) {
        counterDataValidator.validate(counterDataDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", CounterDataStatus.getAll());
            return "/admin/counter-data/add";
        }
        counterDataService.save(counterDataMapper.toEntity(counterDataDTO));
        return "redirect:/admin/counter-data/copy/" + counterDataService.getMaxId();
    }

    @GetMapping("/counter-list/{id}")
    public String getById(@PathVariable("id") long id, Model model) {
        CounterData counterData = counterDataService.getById(id);
        model.addAttribute("statuses", CounterDataStatus.getAll());
        model.addAttribute("filter", new CounterDataFilterDto());
        model.addAttribute("counters", counterDataMapper.toDtoList(counterData.getFlat().getCounterData()));
        model.addAttribute("id", id);
        return "/admin/counter-data/counter-list";
    }

    @GetMapping("/counter-list/filter/{id}")
    public String filter(@ModelAttribute("filter") CounterDataFilterDto filter, @PathVariable("id") long id, Model model,
                         @RequestParam("number") String number, @RequestParam("status") String status, @RequestParam(value = "date", defaultValue = "1000-01-01") Date date) {
        counterDataService.updateFilter(filter);
        CounterData counterData = counterDataService.getById(id);

        if (filter.getHouse() != null) {
            model.addAttribute("sections", houseService.getById(filter.getHouse()).getSections());
            model.addAttribute("flats", houseService.getById(filter.getHouse()).getFlats());
        }
        model.addAttribute("statuses", CounterDataStatus.getAll());
        model.addAttribute("status", status);
        model.addAttribute("number", number);
        if (!Objects.equals(date.toString(), "1000-01-01"))
            model.addAttribute("date", date);
        model.addAttribute("filter", filter);
        model.addAttribute("counters", counterDataService.filter(filter, status, number, date, counterData.getFlat().getCounterData()));
        model.addAttribute("id", id);
        return "/admin/counter-data/counter-list";
    }

    @GetMapping("/edit/{id}")
    public String editById(@PathVariable("id") long id, Model model) {
        CounterDataDTO counterDataDTO = counterDataMapper.toDto(counterDataService.getById(id));
        counterDataDTO.setFromDate(Date.valueOf(LocalDate.now()));
        model.addAttribute("statuses", CounterDataStatus.getAll());
        model.addAttribute("counterDataDTO", counterDataDTO);
        return "/admin/counter-data/add";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") long id, Model model) {
        counterDataService.deleteById(id);
        return "redirect:/admin/counter-data/index/1";
    }
    @GetMapping("/{id}")
    public String index(@PathVariable("id")long id, Model model){
        model.addAttribute("counter", counterDataService.getById(id));
        return "/admin/counter-data/index";
    }

}