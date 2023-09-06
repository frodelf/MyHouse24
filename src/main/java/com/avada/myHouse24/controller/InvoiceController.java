package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.TemplateForInvoice;
import com.avada.myHouse24.enums.InvoiceStatus;
import com.avada.myHouse24.mapper.InvoiceMapper;
import com.avada.myHouse24.mapper.TemplateForInvoiceMapper;
import com.avada.myHouse24.model.InvoiceDto;
import com.avada.myHouse24.model.TemplateForInvoiceDTO;
import com.avada.myHouse24.services.impl.*;
import com.avada.myHouse24.util.ImageUtil;
import com.avada.myHouse24.util.NumberUtil;
import com.avada.myHouse24.validator.InvoiceValidator;
import com.avada.myHouse24.validator.TemplateForInvoiceValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceMapper invoiceMapper;
    private final TariffServiceImpl tariffService;
    private final InvoiceServiceImpl invoiceService;
    private final InvoiceValidator invoiceValidator;
    private final AdditionalServiceImpl additionalService;
    private final TemplateForInvoiceServiceImpl templateForInvoiceService;
    private final TemplateForInvoiceMapper templateForInvoiceMapper;
    private final TemplateForInvoiceValidator templateForInvoiceValidator;
    private final AccountTransactionServiceImpl accountTransactionService;
    private final ScoreServiceImpl scoreService;


    @GetMapping("/index/{id}")
    public String getAll(@PathVariable("id") int id, Model model) {

        model.addAttribute("sumAccountTransactionForStats", accountTransactionService.getAllSum());
        model.addAttribute("balanceScoreForStats", scoreService.getAllBalance());
        model.addAttribute("sumWhereIsIncomeIsFalse", accountTransactionService.getSumWhereIsIncomeIsFalse());

        model.addAttribute("invoices", invoiceMapper.toDtoList(invoiceService.getPage(id, model).getContent()));
        model.addAttribute("filter", new InvoiceDto());
        return "/admin/invoice/get-all";
    }

    @GetMapping("/filter/{page}")
    public String filter(@PathVariable("page") int page, @ModelAttribute("filter") InvoiceDto filter, Model model, @RequestParam("flatNumber") String flatNumber, @RequestParam(value = "dateExample", defaultValue = "1000-01-01") Date dateExample) {
        model.addAttribute("invoices", invoiceService.getPage(page, model, invoiceService.filter(filter, flatNumber, dateExample)).getContent());
        model.addAttribute("filter", filter);
        if (!Objects.equals(dateExample.toString(), "1000-01-01"))
            model.addAttribute("dateExample", dateExample);
        if(flatNumber != null)
            model.addAttribute("flatNumber", flatNumber);
        return "/admin/invoice/get-all";
    }

    @GetMapping("/add")
    public String add(Model model) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setDate(Date.valueOf(LocalDate.now()));
        invoiceDto.setNumber(NumberUtil.generateRandomNumber());
        model.addAttribute("invoiceDto", invoiceDto);
        model.addAttribute("statuses", InvoiceStatus.getAll());
        model.addAttribute("tariffs", tariffService.getAll());
        model.addAttribute("services", additionalService.getAll());
        return "/admin/invoice/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("invoiceDto") InvoiceDto invoiceDto, BindingResult bindingResult, Model model) {
        invoiceValidator.validate(invoiceDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoiceDto", invoiceDto);
            model.addAttribute("statuses", InvoiceStatus.getAll());
            model.addAttribute("tariffs", tariffService.getAll());
            model.addAttribute("services", additionalService.getAll());
            return "/admin/invoice/add";
        }
        invoiceService.save(invoiceMapper.toEntity(invoiceDto));
        return "redirect:/admin/invoice/index/1";
    }

    @GetMapping("/deleteAllByCheckBox")
    public String deleteAllByCheckBox(@RequestParam(value = "selectedIntegers", required = false) List<Integer> selectedIntegers) {
        for (Integer selectedInteger : selectedIntegers) {
            invoiceService.deleteById(selectedInteger);
        }
        return "redirect:/admin/invoice/index/1";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") long id) {
        invoiceService.deleteById(id);
        return "redirect:/admin/invoice/index/1";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model) {
        model.addAttribute("invoiceDto", invoiceMapper.toDto(invoiceService.getById(id)));
        model.addAttribute("statuses", InvoiceStatus.getAll());
        model.addAttribute("tariffs", tariffService.getAll());
        model.addAttribute("services", additionalService.getAll());
        return "/admin/invoice/add";
    }
    @GetMapping("/copy/{id}")
    public String copy(@PathVariable("id") long id, Model model) {
        InvoiceDto invoiceDto = invoiceMapper.toDto(invoiceService.getById(id));
        invoiceDto.setId(invoiceService.getMaxId());
        model.addAttribute("invoiceDto", invoiceDto);
        model.addAttribute("statuses", InvoiceStatus.getAll());
        model.addAttribute("tariffs", tariffService.getAll());
        model.addAttribute("services", additionalService.getAll());
        return "/admin/invoice/add";
    }
    @GetMapping("/{id}")
    public String getById(@PathVariable("id")long id, Model model){
        model.addAttribute("invoiceDto", invoiceMapper.toDto(invoiceService.getById(id)));
        return "/admin/invoice/index";
    }

    @GetMapping("/template")
    public String template(@ModelAttribute("template") TemplateForInvoiceDTO template, Model model){
        model.addAttribute("templates", templateForInvoiceService.getAll());
        System.out.println(templateForInvoiceService.getTemplateWhereIsMainIsTrue());
        return "/admin/invoice/template";
    }
    @PostMapping("/template/add")
    public String templateAdd(@ModelAttribute("template") @Valid TemplateForInvoiceDTO template, BindingResult bindingResult, Model model){
        templateForInvoiceValidator.validate(template, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("templates", templateForInvoiceService.getAll());
            return "/admin/invoice/template";
        }
        template.setIsMain(false);
        templateForInvoiceService.save(templateForInvoiceMapper.toEntity(template));
        return "redirect:/admin/invoice/template";
    }
    @GetMapping("/download/{id}")
    @ResponseBody
    public byte[] download(@PathVariable("id")long id) throws IOException {
        return ImageUtil.convertFileToBytes(templateForInvoiceService.getById(id).getFileName());
    }
    @GetMapping("/doDefault/{id}")
    public String doDefault(@PathVariable("id")long id){
        TemplateForInvoice templateForInvoice = new TemplateForInvoice();
        if(templateForInvoiceService.getTemplateWhereIsMainIsTrue() != null) {
            templateForInvoice = templateForInvoiceService.getTemplateWhereIsMainIsTrue();
            templateForInvoice.setIsMain(false);
            templateForInvoiceService.save(templateForInvoice);
        }
        templateForInvoice = templateForInvoiceService.getById(id);
        templateForInvoice.setIsMain(true);
        templateForInvoiceService.save(templateForInvoice);
        return "redirect:/admin/invoice/template";
    }
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id")long id){
        templateForInvoiceService.deleteById(id);
        return "redirect:/admin/invoice/template";
    }

    @GetMapping("/print/{id}")
    public String print(@PathVariable("id")long id, Model model){
        model.addAttribute("invoice", invoiceService.getById(id));
        model.addAttribute("templates", templateForInvoiceService.getAll());
        return "/admin/invoice/print";
    }
}