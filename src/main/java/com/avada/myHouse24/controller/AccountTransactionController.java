package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.mapper.AccountTransactionMapper;
import com.avada.myHouse24.model.AccountTransactionForViewDTO;
import com.avada.myHouse24.model.AccountTransactionInDTO;
import com.avada.myHouse24.model.AccountTransactionOutDTO;
import com.avada.myHouse24.services.impl.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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

    @GetMapping("/index/{id}")
    public ModelAndView index(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("admin/account-transaction/get-all");
        modelAndView.addObject("accountTransaction", new AccountTransactionForViewDTO());

        modelAndView.addObject("sumAccountTransactionForStats", accountTransactionService.getAllSum());
        modelAndView.addObject("balanceScoreForStats", scoreService.getAllBalance());
        modelAndView.addObject("sumWhereIsIncomeIsFalse", accountTransactionService.getSumWhereIsIncomeIsFalse());

        modelAndView.addObject("accountTransaction", new AccountTransactionForViewDTO());
        modelAndView.addObject("accountTransactionList", accountTransactionMapper.toDtoForViewList(accountTransactionService.getPage(id, modelAndView).getContent()));
        modelAndView.addObject("transactionPurposeList", transactionPurposeService.getAll());
        modelAndView.addObject("sumWhereIsIncomeIsTrue", accountTransactionService.getSumWhereIsIncomeIsTrue());

        return modelAndView;
    }


    @GetMapping("/create/in")
    public ModelAndView addIn(@ModelAttribute("accountTransactionInDTO") AccountTransactionInDTO accountTransactionInDTO) {
        ModelAndView modelAndView = new ModelAndView("admin/account-transaction/add-in");

        modelAndView.addObject("users", userService.getAll());
        modelAndView.addObject("admins", adminService.getAll());
        modelAndView.addObject("scores", scoreService.getAll());
        modelAndView.addObject("transactionPurposes", transactionPurposeService.getAllIncomeTrue());
        modelAndView.addObject("maxId", accountTransactionService.getNumber());
        modelAndView.addObject("fromDate", Date.valueOf(LocalDate.now()));

        return modelAndView;
    }

    @PostMapping("/create/in")
    public ModelAndView addIn(@ModelAttribute("accountTransactionInDTO") @Valid AccountTransactionInDTO accountTransactionInDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/account-transaction/add-in");
            modelAndView.addObject("users", userService.getAll());
            modelAndView.addObject("admins", adminService.getAll());
            modelAndView.addObject("scores", scoreService.getAll());
            modelAndView.addObject("transactionPurposes", transactionPurposeService.getAllIncomeTrue());
            modelAndView.addObject("maxId", accountTransactionService.getNumber());
            modelAndView.addObject("fromDate", Date.valueOf(LocalDate.now()));
        } else {
            accountTransactionInDTO.setIncome(true);
            accountTransactionService.save(accountTransactionMapper.toEntityForIn(accountTransactionInDTO));
            modelAndView.setViewName("redirect:/admin/account-transaction/index/1");
        }

        return modelAndView;
    }

    @GetMapping("/update/in/{id}")
    public ModelAndView updateIn(@ModelAttribute("accountTransactionInDTO") AccountTransactionInDTO accountTransactionInDTO, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("admin/account-transaction/update-in");

        modelAndView.addObject("accountTransaction", accountTransactionMapper.toDtoForIn(accountTransactionService.getById(id)));
        modelAndView.addObject("users", userService.getAll());
        modelAndView.addObject("admins", adminService.getAll());
        modelAndView.addObject("scores", scoreService.getAll());
        modelAndView.addObject("transactionPurposes", transactionPurposeService.getAllIncomeTrue());

        return modelAndView;
    }

    @PostMapping("/update/in/{id}")
    public ModelAndView updateIn(@ModelAttribute("accountTransactionInDTO") @Valid AccountTransactionInDTO accountTransactionInDTO, BindingResult bindingResult, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/account-transaction/update-in");
            modelAndView.addObject("accountTransaction", accountTransactionInDTO);
            modelAndView.addObject("users", userService.getAll());
            modelAndView.addObject("admins", adminService.getAll());
            modelAndView.addObject("scores", scoreService.getAll());
            modelAndView.addObject("transactionPurposes", transactionPurposeService.getAllIncomeTrue());
        } else {
            accountTransactionInDTO.setIncome(true);
            accountTransactionService.save(accountTransactionMapper.toEntityForIn(accountTransactionInDTO));
            modelAndView.setViewName("redirect:/admin/account-transaction/index/1");
        }
        return modelAndView;
    }

    @GetMapping("/create/out")
    public ModelAndView addOut(@ModelAttribute("accountTransactionOutDTO") AccountTransactionOutDTO accountTransactionOutDTO) {
        ModelAndView modelAndView = new ModelAndView("admin/account-transaction/add-out");

        modelAndView.addObject("users", userService.getAll());
        modelAndView.addObject("admins", adminService.getAll());
        modelAndView.addObject("scores", scoreService.getAll());
        modelAndView.addObject("transactionPurposes", transactionPurposeService.getAllIncomeFalse());
        modelAndView.addObject("maxId", accountTransactionService.getNumber());
        modelAndView.addObject("fromDate", Date.valueOf(LocalDate.now()));

        return modelAndView;
    }

    @PostMapping("/create/out")
    public ModelAndView addOut(@ModelAttribute("accountTransactionOutDTO") @Valid AccountTransactionOutDTO accountTransactionOutDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/account-transaction/add-out");
            modelAndView.addObject("users", userService.getAll());
            modelAndView.addObject("admins", adminService.getAll());
            modelAndView.addObject("scores", scoreService.getAll());
            modelAndView.addObject("transactionPurposes", transactionPurposeService.getAllIncomeFalse());
            modelAndView.addObject("maxId", accountTransactionService.getNumber());
            modelAndView.addObject("fromDate", Date.valueOf(LocalDate.now()));
        } else {
            accountTransactionOutDTO.setSum("-" + accountTransactionOutDTO.getSum());
            accountTransactionService.save(accountTransactionMapper.toEntityForOut(accountTransactionOutDTO));
            modelAndView.setViewName("redirect:/admin/account-transaction/index/1");
        }

        return modelAndView;
    }

    @GetMapping("/update/out/{id}")
    public ModelAndView updateOut(@ModelAttribute("accountTransactionOutDTO") AccountTransactionOutDTO accountTransactionOutDTO, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("admin/account-transaction/update-out");

        modelAndView.addObject("accountTransaction", accountTransactionMapper.toDtoForOut(accountTransactionService.getById(id)));
        modelAndView.addObject("admins", adminService.getAll());
        modelAndView.addObject("transactionPurposes", transactionPurposeService.getAllIncomeFalse());
        modelAndView.addObject("fromDate", Date.valueOf(LocalDate.now()));

        return modelAndView;
    }

    @PostMapping("/update/out/{id}")
    public ModelAndView updateOut(@ModelAttribute("accountTransactionOutDTO") @Valid AccountTransactionOutDTO accountTransactionOutDTO, BindingResult bindingResult, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/account-transaction/update-out");
            modelAndView.addObject("accountTransaction", accountTransactionOutDTO);
            modelAndView.addObject("admins", adminService.getAll());
            modelAndView.addObject("transactionPurposes", transactionPurposeService.getAllIncomeFalse());
        } else {
            accountTransactionOutDTO.setSum(accountTransactionOutDTO.getSum());
            accountTransactionService.save(accountTransactionMapper.toEntityForOut(accountTransactionOutDTO));
            modelAndView.setViewName("redirect:/admin/account-transaction/index/1");
        }

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") long id) {
        accountTransactionService.deleteById(id);
        return "redirect:/admin/account-transaction/index/1";
    }

    @GetMapping("/{id}")
    public ModelAndView getAccountTransaction(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView("admin/account-transaction/index");

        modelAndView.addObject("accountTransaction", accountTransactionMapper.toDtoForView(accountTransactionService.getById(id)));

        return modelAndView;
    }

    @GetMapping("/copy/in/{id}")
    public ModelAndView copyIn(@ModelAttribute("accountTransactionInDTO") AccountTransactionInDTO accountTransactionInDTO, @PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView("admin/account-transaction/update-in");

        AccountTransaction accountTransaction = accountTransactionService.getById(id);
        accountTransaction.setNumber(String.valueOf(accountTransactionService.getNumber()));
        accountTransaction.setId(accountTransactionService.getMaxId());
        modelAndView.addObject("accountTransaction", accountTransactionMapper.toDtoForIn(accountTransaction));
        modelAndView.addObject("users", userService.getAll());
        modelAndView.addObject("admins", adminService.getAll());
        modelAndView.addObject("scores", scoreService.getAll());
        modelAndView.addObject("transactionPurposes", transactionPurposeService.getAll());

        return modelAndView;
    }

    @GetMapping("/copy/out/{id}")
    public ModelAndView copyOut(@ModelAttribute("accountTransactionOutDTO") AccountTransactionOutDTO accountTransactionOutDTO, @PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView("admin/account-transaction/update-out");

        AccountTransaction accountTransaction = accountTransactionService.getById(id);
        accountTransaction.setNumber(String.valueOf(accountTransactionService.getNumber()));
        accountTransaction.setId(accountTransactionService.getMaxId());
        modelAndView.addObject("accountTransaction", accountTransactionMapper.toDtoForOut(accountTransaction));
        modelAndView.addObject("admins", adminService.getAll());
        modelAndView.addObject("transactionPurposes", transactionPurposeService.getAll());
        modelAndView.addObject("maxId", accountTransactionService.getNumber());
        modelAndView.addObject("fromDate", Date.valueOf(LocalDate.now()));

        return modelAndView;
    }

    @GetMapping("/filter/{number}")
    public ModelAndView filter(@ModelAttribute @Valid AccountTransactionForViewDTO accountTransactionForViewDTO, BindingResult bindingResult, @PathVariable("number") int number) {
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/account-transaction/index/1");
        if(bindingResult.hasErrors()){
            return modelAndView;
        }
        modelAndView.addObject("accountTransaction", accountTransactionForViewDTO);
        modelAndView.addObject("accountTransactionList", accountTransactionService.getPage(number, modelAndView, accountTransactionService.filter(accountTransactionForViewDTO, accountTransactionMapper.toDtoForViewList(accountTransactionService.getAll()))));
        modelAndView.addObject("transactionPurposeList", transactionPurposeService.getAll());
        modelAndView.addObject("sumWhereIsIncomeIsTrue", accountTransactionService.getSumWhereIsIncomeIsTrue());
        modelAndView.addObject("sumWhereIsIncomeIsFalse", accountTransactionService.getSumWhereIsIncomeIsFalse());
        return modelAndView;
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> excel(HttpServletResponse response) throws IOException {
        accountTransactionService.excel(response);
        return ResponseEntity.ok().build();
    }
}