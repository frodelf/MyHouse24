package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.entity.Section;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.mapper.ScoreMapper;
import com.avada.myHouse24.model.FlatDTO;
import com.avada.myHouse24.model.ScoreDTO;
import com.avada.myHouse24.model.ScoreForFilterDTO;
import com.avada.myHouse24.model.Select2Option;
import com.avada.myHouse24.services.impl.*;
import com.avada.myHouse24.validator.ScoreValidator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class ScoreController {
    private final HouseServiceImpl houseService;
    private final FlatServiceImpl flatService;
    private final FlatMapper flatMapper;
    private final ScoreValidator scoreValidator;
    private final UserServiceImpl userService;
    private final ScoreServiceImpl scoreService;
    private final ScoreMapper scoreMapper;
    private final AccountTransactionServiceImpl accountTransactionService;

    @GetMapping("/index/{page}")
    public String getAll(@PathVariable("page") int page, Model model) {
        model.addAttribute("houses", houseService.getAll());

        model.addAttribute("sumAccountTransactionForStats", accountTransactionService.getAllSum());
        model.addAttribute("balanceScoreForStats", scoreService.getAllBalance());
        model.addAttribute("sumWhereIsIncomeIsFalse", accountTransactionService.getSumWhereIsIncomeIsFalse());

        model.addAttribute("scores", scoreService.getPage(page, model));
        model.addAttribute("sections", new ArrayList<>());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("filter", new ScoreForFilterDTO());
        return "admin/account/get-all";
    }
    @GetMapping("/getSections/{id}")
    @ResponseBody
    public List<Section> getSectionsByHouseId(@PathVariable("id") int id) {
        List<Section> sections = houseService.getById(id).getSections();
        return sections;
    }
    @GetMapping("/getFlats/{id}")
    @ResponseBody
    public List<FlatDTO> getFlatsByHouseId(@PathVariable("id") int id) {
        List<FlatDTO> flats = flatMapper.toDtoList(houseService.getById(id).getFlats());
        return flats;
    }
    @GetMapping("/add")
    public String add(@ModelAttribute("scoreDto")ScoreDTO scoreDTO, Model model) {
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("sections", new ArrayList<Section>());
        model.addAttribute("flats", new ArrayList<Flat>());
        return "admin/account/add";
    }
    @PostMapping("/add")
    public String add(@ModelAttribute("scoreDto") @Valid ScoreDTO scoreDTO, BindingResult bindingResult, @RequestParam(value = "flat", defaultValue = "-1")Long flatId, Model model) {
        scoreDTO.setFlat(flatId != null  &&  flatId != -1 ? flatService.getById(flatId) : null);
        scoreValidator.validate(scoreDTO, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("houses", houseService.getAll());
            return "admin/account/add";
        }
        scoreDTO.setBalance(0.0);
        scoreService.save(scoreMapper.toEntity(scoreDTO));
        return "redirect:/admin/account/index/1";
    }
    @GetMapping("/{id}")
    public String getById(@PathVariable("id")long id, Model model){
        model.addAttribute("scoreDto", scoreService.getById(id));
        return "admin/account/index";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Long id, Model model){
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("scoreDto", scoreMapper.toDto(scoreService.getById(id)));
        model.addAttribute("sections", scoreService.getById(id).getFlat().getHouse().getSections());
        model.addAttribute("flats", scoreService.getById(id).getFlat().getHouse().getFlats());
        return "admin/account/add";
    }
    @GetMapping("/filter/{page}")
    public String filter(@PathVariable("page")int page, @ModelAttribute("filter") ScoreForFilterDTO scoreForFilterDTO, Model model) {
        model.addAttribute("scores", scoreService.getPage(page, model, scoreService.filter(scoreForFilterDTO, scoreMapper.toDtoList(scoreService.getAll()))));
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("sections", new ArrayList<>());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("filter", scoreForFilterDTO);
        return "admin/account/get-all";
    }
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id")Long id){
        scoreService.deleteById(id);
        return "redirect:/admin/account/index/1";
    }
    @GetMapping("/get-scores")
    public ResponseEntity<Map<String, Object>> getAllHouses(@RequestParam("_page") int page,
                                                            @RequestParam("_search") String search){
        int pageSize = 10;
        List<Score> scores = scoreService.forSelect(page, pageSize, search);
        List<Select2Option> select2Options = new ArrayList<>();
        for (Score score : scores) {
            select2Options.add(new Select2Option(score.getId(), score.getNumber()));
        }
        int totalResults = 10;
        Map<String, Object> response = new HashMap<>();
        response.put("results", select2Options);
        response.put("pagination", Map.of("more", (page * pageSize) < totalResults));
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getScoreByFlat/{id}")
    @ResponseBody
    public Score getScoreByFlat(@PathVariable("id")long id){
        return flatService.getById(id).getScore();
    }
    @GetMapping("/excel")
    public ResponseEntity<byte[]> excel(HttpServletResponse response) throws IOException {
        scoreService.excel(response);
        return ResponseEntity.ok().build();
    }
}