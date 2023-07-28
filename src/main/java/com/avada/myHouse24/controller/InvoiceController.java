package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Invoice;
import com.avada.myHouse24.enums.InvoiceStatus;
import com.avada.myHouse24.mapper.InvoiceMapper;
import com.avada.myHouse24.model.InvoiceDto;
import com.avada.myHouse24.services.impl.AdditionalServiceImpl;
import com.avada.myHouse24.services.impl.InvoiceServiceImpl;
import com.avada.myHouse24.services.impl.TariffServiceImpl;
import com.avada.myHouse24.util.DateUtil;
import com.avada.myHouse24.util.NumberUtil;
import com.avada.myHouse24.validator.InvoiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Controller
@RequestMapping("/admin/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceServiceImpl invoiceService;
    private final TariffServiceImpl tariffService;
    private final InvoiceValidator invoiceValidator;
    private final AdditionalServiceImpl additionalService;
    private final InvoiceMapper invoiceMapper;

    @GetMapping("/index/{id}")
    public String getAll(@PathVariable("id") int id, Model model) {
        model.addAttribute("invoices", invoiceMapper.toDtoList(invoiceService.getPage(id, model).getContent()));
        model.addAttribute("filter", new InvoiceDto());
        return "/admin/invoice/get-all";
    }

    @GetMapping("/filter/{page}")
    public String filter(@PathVariable("page") int page, @ModelAttribute("filter") InvoiceDto filter, Model model, @RequestParam("flatNumber") String flatNumber, @RequestParam(value = "dateExample", defaultValue = "1000-01-01") Date dateExample) {
//        String s = DateUtil.toMonthForMY(filter.getMonths(), new Locale("uk"));
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
        invoiceDto.setId(null);
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
}