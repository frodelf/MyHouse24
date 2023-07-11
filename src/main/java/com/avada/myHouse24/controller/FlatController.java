package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Floor;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.entity.Section;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.model.FlatDTO;
import com.avada.myHouse24.model.TariffDTO;
import com.avada.myHouse24.services.impl.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/flat")
@RequiredArgsConstructor
public class FlatController {
    private final FlatServiceImpl flatService;
    private final HouseServiceImpl houseService;
    private final FloorServiceImpl floorService;
    private final ScoreServiceImpl scoreService;
    private final SectionServiceImpl sectionService;
    private final UserServiceImpl userService;
    private final TariffServiceImpl tariffService;
    private final FlatMapper flatMapper;
    @GetMapping("/index/{id}")
    public String getAll(@PathVariable("id")int id, Model model){
        FlatDTO flatDTO = new FlatDTO();
        model.addAttribute("flats", flatService.getPage(id, model));
        model.addAttribute("filter", flatDTO);
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("sections", new ArrayList<Section>());
        model.addAttribute("floors", new ArrayList<Floor>());
        model.addAttribute("flatCount", flatService.getAll().size());
        return "/admin/flat/get-all";
    }
    @GetMapping("/add")
    public String add(@ModelAttribute("flatDTO") FlatDTO flatDTO, Model model){
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("scores", scoreService.getAll());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("tariffs", tariffService.getAll());
        return "/admin/flat/add";
    }
    @GetMapping("/getFloors/{id}")
    @ResponseBody
    public List<Floor> getFloorsByHouseId(@PathVariable("id")int id){
        return houseService.getById(id).getFloors();
    }
    @GetMapping("/getSections/{id}")
    @ResponseBody
    public List<Section> getSectionsByHouseId(@PathVariable("id")int id){
        return houseService.getById(id).getSections();
    }
    @PostMapping("/add")
    public String add(@ModelAttribute("flatDTO") @Valid FlatDTO flatDTO, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("houses", houseService.getAll());
            model.addAttribute("scores", scoreService.getAll());
            model.addAttribute("users", userService.getAll());
            model.addAttribute("tariffs", tariffService.getAll());
            return "/admin/flat/add";
        }
        if(!scoreService.existNumber(flatDTO.getScoreNumber())){
            Score score = new Score();
            score.setNumber(flatDTO.getScoreNumber());
            scoreService.save(score);
        }
        flatService.save(flatMapper.toEntity(flatDTO));
        return "redirect:/admin/flat/index/1";
    }
}
