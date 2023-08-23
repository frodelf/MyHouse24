package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.pages.*;
import com.avada.myHouse24.services.impl.WebsiteService;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/website")
@Log
public class WebsiteController {

    @Autowired
    private WebsiteService websiteService;
//    @Autowired private PageValidator validator;

    @GetMapping("/index")
    public String showEditindexPage(Model model) {
        model.addAttribute("mainPage", websiteService.getMainPage());
        log.info(Objects.requireNonNull(model.getAttribute("mainPage")).toString());
        return "admin/website/index";
    }

    @GetMapping("/about")
    public String showEditAboutPage(Model model) {
        AboutPage page = websiteService.getAboutPage();
        List<String> images = Arrays.stream(page.getImages().split(","))
                .filter((image) -> !image.equals(""))
                .collect(Collectors.toList());
        List<String> add_images = Arrays.stream(page.getAdd_images().split(","))
                .filter((image) -> !image.equals(""))
                .collect(Collectors.toList());
        model.addAttribute("page", page);
        model.addAttribute("images", images);
        model.addAttribute("add_images", add_images);
        model.addAttribute("documents", websiteService.getAllDocuments());
        return "admin/website/about";
    }

    @GetMapping("/delete-about-image/{id}")
    public String deleteAboutImage(@PathVariable int id, Model model) {
        AboutPage page = websiteService.getAboutPage();
        page = websiteService.deleteImageAndGetPage(page, id);
        websiteService.savePage(page);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/delete-about-add-image/{id}")
    public String deleteAboutAddImage(@PathVariable int id, Model model) {
        AboutPage page = websiteService.getAboutPage();
        String images = page.getAdd_images();
        List<String> imageList = new ArrayList<>(Arrays.asList(images.split(",")));
        imageList.remove(id);
        page.setAdd_images(String.join(",", images));
        websiteService.savePage(page);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/delete-document/{id}")
    public String deleteDocument(@PathVariable long id) {
        websiteService.deleteDocument(id);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/services")
    public String showEditServicesPage(Model model) {
        model.addAttribute("page", websiteService.getServicesPage());
        return "admin/website/services";
    }

    @GetMapping("/contacts")
    public String showEditContactsPage(Model model) {
        model.addAttribute("contactsPage", websiteService.getContactsPage());
        return "admin/website/contacts";
    }

    @PostMapping("/index")
    public String editindexPage(@Valid @ModelAttribute MainPage page,
                               BindingResult bindingResult,
                               @RequestPart(required = false) MultipartFile page_slide1,
                               @RequestPart(required = false) MultipartFile page_slide2,
                               @RequestPart(required = false) MultipartFile page_slide3,
                               @RequestPart(required = false) MultipartFile page_block_1_img,
                               @RequestPart(required = false) MultipartFile page_block_2_img,
                               @RequestPart(required = false) MultipartFile page_block_3_img,
                               @RequestPart(required = false) MultipartFile page_block_4_img,
                               @RequestPart(required = false) MultipartFile page_block_5_img,
                               @RequestPart(required = false) MultipartFile page_block_6_img) throws IOException {

//        validator.validate(page, bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getAllErrors().toString());
            return "admin/website/index";
        }

        page.setId(1);
        page = websiteService.saveMainPageImages(page, page_slide1, page_slide2, page_slide3, page_block_1_img,
                page_block_2_img, page_block_3_img, page_block_4_img, page_block_5_img, page_block_6_img);
        websiteService.savePage(page);

        return "redirect:/admin/website/index";
    }

    @PostMapping("/about")
    public String editAboutPage(@Valid @ModelAttribute AboutPage page,
                                BindingResult bindingResult,
                                @RequestPart(required = false) MultipartFile page_director_image,
                                @RequestPart(required = false) MultipartFile[] page_images,
                                @RequestPart(required = false) MultipartFile[] page_add_images,
                                @RequestParam(required = false) String[] document_names,
                                @RequestParam(required = false) MultipartFile[] document_files) throws IOException {

        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getAllErrors().toString());
            return "admin/website/about";
        }

        page.setId(1);
        page = websiteService.saveAboutPageInfo(page, page_director_image, page_images, page_add_images, document_names, document_files);
        websiteService.savePage(page);

        return "redirect:/admin/website/about";
    }

    @PostMapping("/services")
    public String editServicesPage(@Valid @ModelAttribute ServicesPage page,
                                   BindingResult bindingResult,
                                   @RequestParam String[] titles,
                                   @RequestParam String[] descriptions,
                                   @RequestParam MultipartFile[] service_images) {

        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getAllErrors().toString());
            return "admin/website/services";
        }

        page = websiteService.saveServicesPageInfo(page, titles, descriptions, service_images);
        websiteService.savePage(page);

        return "redirect:/admin/website/services";
    }



    @PostMapping("/contacts")
    public String editContactsPage(@Valid @ModelAttribute ContactsPage page, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getObjectName());
            log.info(bindingResult.getAllErrors().toString());
            model.addAttribute("page", page);
            return "admin/website/contacts";
        }

        page.setId(1);
        websiteService.savePage(page);
        return "redirect:/admin/website/contacts";
    }


}
