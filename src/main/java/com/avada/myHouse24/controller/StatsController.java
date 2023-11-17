package com.avada.myHouse24.controller;

import com.avada.myHouse24.services.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StatsController {
    private final HouseServiceImpl houseService;
    private final FlatServiceImpl flatService;
    private final ScoreServiceImpl scoreService;
    private final UserServiceImpl userService;
    private final MasterRequestServiceImpl masterRequestService;
    private final AccountTransactionServiceImpl accountTransactionService;

    @GetMapping
    public ModelAndView stats(){
        ModelAndView modelAndView = new ModelAndView("admin/stats/index");
        modelAndView.addObject("houses", houseService.count());
        modelAndView.addObject("flats", flatService.count());
        modelAndView.addObject("scores", scoreService.count());
        modelAndView.addObject("users", userService.count());
        modelAndView.addObject("masterNew", masterRequestService.count("NEW"));
        modelAndView.addObject("masterInProgress", masterRequestService.count("IN_PROGRESS"));
        modelAndView.addObject("values", accountTransactionService.getValueForStats());
        modelAndView.addObject("months", accountTransactionService.getMonthForStats());
        modelAndView.addObject("balanceScoreForStats", scoreService.getAllBalance());
        modelAndView.addObject("sumWhereIsIncomeIsFalse", accountTransactionService.getSumWhereIsIncomeIsFalse());
        modelAndView.addObject("sumAccountTransactionForStats", accountTransactionService.getAllSum());

        return modelAndView;
    }
}
