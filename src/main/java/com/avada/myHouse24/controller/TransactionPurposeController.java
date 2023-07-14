package com.avada.myHouse24.controller;

import com.avada.myHouse24.mapper.TransactionPurposeMapper;
import com.avada.myHouse24.model.TransactionPurposeDTO;
import com.avada.myHouse24.services.impl.TransactionPurposeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping("/admin/transaction-purpose")
@RequiredArgsConstructor
public class TransactionPurposeController {
    private final TransactionPurposeServiceImpl transactionPurposeService;
    private final TransactionPurposeMapper transactionPurposeMapper;
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("purposes", transactionPurposeMapper.toDtoList(transactionPurposeService.getAll()));
        return "admin/transaction-purpose/get-all";
    }
    @GetMapping("/create")
    public String add(@ModelAttribute TransactionPurposeDTO transactionPurposeDTO) {
        return "admin/transaction-purpose/add";
    }
    @PostMapping("/create")
    public String add(@ModelAttribute @Valid TransactionPurposeDTO transactionPurposeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/transaction-purpose/add";
        }
        transactionPurposeService.save(transactionPurposeMapper.toEntity(transactionPurposeDTO));
        return "redirect:/admin/transaction-purpose/index";
    }
    @GetMapping("/edit/{id}")
    public String edit(@ModelAttribute TransactionPurposeDTO transactionPurposeDTO, Model model, @PathVariable("id")long id) {
        model.addAttribute("purpose", transactionPurposeMapper.toDto(transactionPurposeService.getById(id)));
        return "admin/transaction-purpose/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute @Valid TransactionPurposeDTO transactionPurposeDTO, BindingResult bindingResult, Model model, @PathVariable("id")long id) {
        if(bindingResult.hasErrors()){
            model.addAttribute("purpose", transactionPurposeMapper.toDto(transactionPurposeService.getById(id)));
            return "admin/transaction-purpose/edit";
        }
        transactionPurposeDTO.setId(id);
        transactionPurposeService.save(transactionPurposeMapper.toEntity(transactionPurposeDTO));
        return "redirect:/admin/transaction-purpose/index";
    }
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id")long id){
        transactionPurposeService.deleteById(id);
        return "redirect:/admin/transaction-purpose/index";
    }
}