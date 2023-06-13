package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.mapper.AccountTransactionMapper;
import com.avada.myHouse24.model.AccountTransactionDTO;
import com.avada.myHouse24.model.AccountTransactionInDTO;
import com.avada.myHouse24.model.AccountTransactionOutDTO;
import com.avada.myHouse24.services.impl.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin/account-transaction")
@RequiredArgsConstructor
public class AccountTransactionController {
    private final AccountTransactionMapper accountTransactionMapper;
    private final AccountTransactionServiceImpl accountTransactionService;
    private final UserServiceImpl userService;
    private final AdminServiceImpl adminService;
    private final ScoreServiceImpl scoreService;
    private final TransactionPurposeServiceImpl transactionPurposeService;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("accountTransaction", new AccountTransactionDTO());
        model.addAttribute("accountTransactionList", accountTransactionMapper.toDtoList(accountTransactionService.getAll()));
        model.addAttribute("transactionPurposeList", transactionPurposeService.getAll());
        model.addAttribute("sumWhereIsIncomeIsTrue", accountTransactionService.getSumWhereIsIncomeIsTrue());
        model.addAttribute("sumWhereIsIncomeIsFalse", accountTransactionService.getSumWhereIsIncomeIsFalse());
        return "admin/account-transaction-all";
    }

    @GetMapping("/create/in")
    public String addIn(@ModelAttribute("accountTransactionInDTO") AccountTransactionInDTO accountTransactionInDTO, Model model) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("admins", adminService.getAll());
        model.addAttribute("scores", scoreService.getAll());
        model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
        model.addAttribute("maxId", accountTransactionService.getMaxId());
        model.addAttribute("fromDate", Date.valueOf(LocalDate.now()));

        return "admin/account-transaction-add-in";
    }

    @PostMapping("create/in")
    public String addIn(@ModelAttribute("accountTransactionInDTO") @Valid AccountTransactionInDTO accountTransactionInDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("users", userService.getAll());
            model.addAttribute("admins", adminService.getAll());
            model.addAttribute("scores", scoreService.getAll());
            model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
            model.addAttribute("maxId", accountTransactionService.getMaxId());
            model.addAttribute("fromDate", Date.valueOf(LocalDate.now()));
            return "admin/account-transaction-add-in";
        }
        accountTransactionInDTO.setIncome(true);
        accountTransactionService.save(accountTransactionMapper.toEntityForIn(accountTransactionInDTO));
        return "redirect:/admin/account-transaction/index";
    }

    @GetMapping("update/in/{id}")
    public String updateIn(@ModelAttribute("accountTransactionInDTO") AccountTransactionInDTO accountTransactionInDTO, @PathVariable("id") long id, Model model) {
        model.addAttribute("accountTransaction", accountTransactionMapper.toDtoForIn(accountTransactionService.getById(id)));
        model.addAttribute("users", userService.getAll());
        model.addAttribute("admins", adminService.getAll());
        model.addAttribute("scores", scoreService.getAll());
        model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
        return "admin/account-transaction-update-in";
    }

    @PostMapping("update/in/{id}")
    public String updateIn(@ModelAttribute("accountTransactionInDTO") @Valid AccountTransactionInDTO accountTransactionInDTO,BindingResult bindingResult, @PathVariable("id") long id, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("accountTransaction", accountTransactionInDTO);
            model.addAttribute("users", userService.getAll());
            model.addAttribute("admins", adminService.getAll());
            model.addAttribute("scores", scoreService.getAll());
            model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
            return "admin/account-transaction-update-in";
        }
        accountTransactionInDTO.setIncome(true);
        accountTransactionService.save(accountTransactionMapper.toEntityForIn(accountTransactionInDTO));
        return "redirect:/admin/account-transaction/index";
    }

    @GetMapping("/create/out")
    public String addOut(@ModelAttribute("accountTransactionOutDTO") AccountTransactionOutDTO accountTransactionOutDTO, Model model) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("admins", adminService.getAll());
        model.addAttribute("scores", scoreService.getAll());
        model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
        model.addAttribute("maxId", accountTransactionService.getMaxId());
        model.addAttribute("fromDate", Date.valueOf(LocalDate.now()));
        return "admin/account-transaction-add-out";
    }

    @PostMapping("/create/out")
    public String addOut(@ModelAttribute("accountTransactionOutDTO") @Valid AccountTransactionOutDTO accountTransactionOutDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("users", userService.getAll());
            model.addAttribute("admins", adminService.getAll());
            model.addAttribute("scores", scoreService.getAll());
            model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
            model.addAttribute("maxId", accountTransactionService.getMaxId());
            model.addAttribute("fromDate", Date.valueOf(LocalDate.now()));
            return "admin/account-transaction-add-out";
        }
        accountTransactionService.save(accountTransactionMapper.toEntityForOut(accountTransactionOutDTO));
        return "redirect:/admin/account-transaction/index";
    }

    @GetMapping("/update/out/{id}")
    public String updateOut(@ModelAttribute("accountTransactionOutDTO") AccountTransactionOutDTO accountTransactionOutDTO, @PathVariable("id") long id,  Model model){
        model.addAttribute("accountTransaction", accountTransactionMapper.toDtoForOut(accountTransactionService.getById(id)));
        model.addAttribute("admins", adminService.getAll());
        model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
        model.addAttribute("maxId", accountTransactionService.getMaxId());
        model.addAttribute("fromDate", Date.valueOf(LocalDate.now()));
        return "admin/account-transaction-update-out";
    }

    @PostMapping("/update/out/{id}")
    public String updateOut(@ModelAttribute("accountTransactionOutDTO") @Valid AccountTransactionOutDTO accountTransactionOutDTO, BindingResult bindingResult, @PathVariable("id") long id, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("accountTransaction", accountTransactionOutDTO);
            model.addAttribute("admins", adminService.getAll());
            model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
            return "admin/account-transaction-update-out";
        }
        accountTransactionService.save(accountTransactionMapper.toEntityForOut(accountTransactionOutDTO));
        return "redirect:/admin/account-transaction/index";
    }
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id")long id){
        accountTransactionService.deleteById(id);
        return "redirect:/admin/account-transaction/index";
    }
    @GetMapping("/{id}")
    public String getAccountTransaction(@PathVariable("id")long id, Model model){
        model.addAttribute("accountTransaction", accountTransactionMapper.toDto(accountTransactionService.getById(id)));
        return "admin/account-transaction";
    }

    @GetMapping("copy/in/{id}")
    public String copyIn(@ModelAttribute("accountTransactionInDTO") AccountTransactionInDTO accountTransactionInDTO, @PathVariable("id") long id, Model model) {
        AccountTransaction accountTransaction = accountTransactionService.getById(id);
        accountTransaction.setId(accountTransactionService.getMaxId());
        model.addAttribute("accountTransaction", accountTransactionMapper.toDtoForIn(accountTransaction));
        model.addAttribute("users", userService.getAll());
        model.addAttribute("admins", adminService.getAll());
        model.addAttribute("scores", scoreService.getAll());
        model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
        return "admin/account-transaction-update-in";
    }
    @GetMapping("/copy/out/{id}")
    public String copyOut(@ModelAttribute("accountTransactionOutDTO") AccountTransactionOutDTO accountTransactionOutDTO, @PathVariable("id") long id,  Model model){
        AccountTransaction accountTransaction = accountTransactionService.getById(id);
        accountTransaction.setId(accountTransactionService.getMaxId());
        model.addAttribute("accountTransaction", accountTransactionMapper.toDtoForOut(accountTransaction));
        model.addAttribute("admins", adminService.getAll());
        model.addAttribute("transactionPurposes", transactionPurposeService.getAll());
        model.addAttribute("maxId", accountTransactionService.getMaxId());
        model.addAttribute("fromDate", Date.valueOf(LocalDate.now()));
        return "admin/account-transaction-update-out";
    }
    @PostMapping("/filter")
    public ModelAndView filter(@ModelAttribute AccountTransactionDTO accountTransactionDTO){
        ModelAndView model = new ModelAndView("admin/account-transaction-all");
        model.addObject("accountTransaction", accountTransactionDTO);
        List<AccountTransactionDTO> accountTransactionDTOS = accountTransactionMapper.toDtoList(accountTransactionService.getAll());

        if(!accountTransactionDTO.getId().equals("")) accountTransactionDTOS = accountTransactionDTOS.stream()
                .filter(dto -> dto.getId().equals(accountTransactionDTO.getId()))
                .collect(Collectors.toList());

        if(!accountTransactionDTO.getDate().equals("")) System.out.println("qwerty");

        if(!accountTransactionDTO.getScoreId().equals("")) accountTransactionDTOS = accountTransactionDTOS.stream()
                .filter(dto -> dto.getScoreId().contains(accountTransactionDTO.getScoreId()))
                .collect(Collectors.toList());

        if(!accountTransactionDTO.getTransactionPurposeName().equals("")) System.out.println("qwerty");
        if(!accountTransactionDTO.getUserName().equals("")) System.out.println("qwerty");
        if(accountTransactionDTO.getAddToStats() != null) System.out.println("qwerty");
        if(accountTransactionDTO.getIsIncome() != null) System.out.println("qwerty");

        model.addObject("accountTransactionList", accountTransactionDTOS);
        model.addObject("transactionPurposeList", transactionPurposeService.getAll());
        model.addObject("sumWhereIsIncomeIsTrue", 5000000);
        model.addObject("sumWhereIsIncomeIsFalse", accountTransactionService.getSumWhereIsIncomeIsFalse());
        return model;
    }
}