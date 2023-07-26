package com.avada.myHouse24.controller;

import com.avada.myHouse24.mapper.InvoiceMapper;
import com.avada.myHouse24.model.InvoiceDto;
import com.avada.myHouse24.services.impl.InvoiceServiceImpl;
import com.avada.myHouse24.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceServiceImpl invoiceService;
    private final InvoiceMapper invoiceMapper;
    @GetMapping("/index/{id}")
    public String getAll(@PathVariable("id")int id, Model model){
        model.addAttribute("invoices", invoiceMapper.toDtoList(invoiceService.getPage(id, model).getContent()));
        model.addAttribute("filter", new InvoiceDto());
        return "/admin/invoice/get-all";
    }
    @GetMapping("/filter/{page}")
    public String filter(@PathVariable("page")int page, @ModelAttribute("filter")InvoiceDto filter, Model model, @RequestParam("flatNumber") String flatNumber, @RequestParam("month") String month){
        model.addAttribute("invoices", invoiceService.getPage(page, model, invoiceService.filter(filter)).getContent());
        model.addAttribute("filter", filter);
        return "/admin/invoice/get-all";
    }
    @GetMapping("/add")
    public String add(Model model){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setDate(Date.valueOf(LocalDate.now()));
        invoiceDto.setNumber(NumberUtil.generateRandomNumber());
        model.addAttribute("invoiceDto", invoiceDto);
        return "/admin/invoice/add";
    }
}
