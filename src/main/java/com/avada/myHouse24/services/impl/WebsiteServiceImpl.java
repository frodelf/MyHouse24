package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.pages.*;

import com.avada.myHouse24.repo.DocumentRepository;
import com.avada.myHouse24.repo.PageRepository;
import com.avada.myHouse24.services.WebsiteService;
import com.avada.myHouse24.util.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log
@RequiredArgsConstructor
@AllArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private ImageUtil imageUtil;

    private static final String imageSaveDir = "/pages/";

    @Override
    public MainPage getMainPage() {
        return pageRepository.getMainPage().orElseThrow();
    }

    @Override
    public AboutPage getAboutPage() {
        return pageRepository.getAboutPage().orElseThrow();
    }

    @Override
    public ServicesPage getServicesPage() {
        return pageRepository.getServicesPage().orElseThrow();
    }

    @Override
    public ContactsPage getContactsPage() {
        return pageRepository.getContactsPage().orElseThrow();
    }

    @Override
    public Page savePage(Page page) {
        return pageRepository.save(page);
    }

    @Override
    public List<AboutPage.Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public AboutPage deleteImageAndGetPage(AboutPage page, int index) {
        String images = page.getImages();
        String imageToDelete = images.split(",")[index];
        images = Arrays.stream(images.split(","))
                .filter((image) -> !image.equals(imageToDelete))
                .collect(Collectors.joining(","));
        page.setImages(images);
        return page;
    }

    @Override
    public void deleteDocument(Long document_id) {
        documentRepository.deleteById(document_id);
    }

    @Override
    public MainPage saveMainPageImages(MainPage page,
                                       MultipartFile page_slide1,
                                       MultipartFile page_slide2,
                                       MultipartFile page_slide3,
                                       MultipartFile page_block_1_img,
                                       MultipartFile page_block_2_img,
                                       MultipartFile page_block_3_img,
                                       MultipartFile page_block_4_img,
                                       MultipartFile page_block_5_img,
                                       MultipartFile page_block_6_img) throws IOException {
        page.setId(1);
        MainPage originalPage = getMainPage();

        log.info("Setting and saving images for main page...");
        if (page_slide1 != null && !page_slide1.isEmpty() && page_slide1.getOriginalFilename() != null && !page_slide1.getOriginalFilename().equals("")) {
            page.setSlide1(page_slide1.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_slide1.getOriginalFilename(), page_slide1);
        } else page.setSlide1(originalPage.getSlide1());
        if (page_slide2 != null && !page_slide2.isEmpty() && page_slide2.getOriginalFilename() != null && !page_slide2.getOriginalFilename().equals("")) {
            page.setSlide2(page_slide2.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_slide2.getOriginalFilename(), page_slide2);
        } else page.setSlide2(originalPage.getSlide2());
        if (page_slide3 != null && !page_slide3.isEmpty() && page_slide3.getOriginalFilename() != null && !page_slide3.getOriginalFilename().equals("")) {
            page.setSlide3(page_slide3.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_slide3.getOriginalFilename(), page_slide3);
        } else page.setSlide3(originalPage.getSlide3());
        if (page_block_1_img != null && !page_block_1_img.isEmpty() && page_block_1_img.getOriginalFilename() != null && !page_block_1_img.getOriginalFilename().equals("")) {
            page.setBlock_1_img(page_block_1_img.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_block_1_img.getOriginalFilename(), page_block_1_img);
        } else page.setBlock_1_img(originalPage.getBlock_1_img());
        if (page_block_2_img != null && !page_block_2_img.isEmpty() && page_block_2_img.getOriginalFilename() != null && !page_block_2_img.getOriginalFilename().equals("")) {
            page.setBlock_2_img(page_block_2_img.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_block_2_img.getOriginalFilename(), page_block_2_img);
        } else page.setBlock_2_img(originalPage.getBlock_2_img());
        if (page_block_3_img != null && !page_block_3_img.isEmpty() && page_block_3_img.getOriginalFilename() != null && !page_block_3_img.getOriginalFilename().equals("")) {
            page.setBlock_3_img(page_block_3_img.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_block_3_img.getOriginalFilename(), page_block_3_img);
        } else page.setBlock_3_img(originalPage.getBlock_3_img());
        if (page_block_4_img != null && !page_block_4_img.isEmpty() && page_block_4_img.getOriginalFilename() != null && !page_block_4_img.getOriginalFilename().equals("")) {
            page.setBlock_4_img(page_block_4_img.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_block_4_img.getOriginalFilename(), page_block_4_img);
        } else page.setBlock_4_img(originalPage.getBlock_4_img());
        if (page_block_5_img != null && !page_block_5_img.isEmpty() && page_block_5_img.getOriginalFilename() != null && !page_block_5_img.getOriginalFilename().equals("")) {
            page.setBlock_5_img(page_block_5_img.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_block_5_img.getOriginalFilename(), page_block_5_img);
        } else page.setBlock_5_img(originalPage.getBlock_5_img());
        if (page_block_6_img != null && !page_block_6_img.isEmpty() && page_block_6_img.getOriginalFilename() != null && !page_block_6_img.getOriginalFilename().equals("")) {
            page.setBlock_6_img(page_block_6_img.getOriginalFilename());
            imageUtil.saveFile(imageSaveDir, page_block_6_img.getOriginalFilename(), page_block_6_img);
        } else page.setBlock_6_img(originalPage.getBlock_6_img());

        return page;
    }

    @Override
    public AboutPage saveAboutPageInfo(AboutPage page,
                                       MultipartFile page_director_image,
                                       MultipartFile[] page_images,
                                       MultipartFile[] page_add_images,
                                       String[] document_names,
                                       MultipartFile[] document_files) throws IOException {
        page.setId(1);
        AboutPage originalPage = getAboutPage();

        //Сохранение единичного фото директора
        log.info("Saving director image");
        if (page_director_image.getSize() > 0) {
            imageUtil.saveFile(imageSaveDir, page_director_image.getOriginalFilename(), page_director_image);
            page.setDirector_image(page_director_image.getOriginalFilename());
        } else page.setDirector_image(originalPage.getDirector_image());
        log.info("Saved director image");

        //Сохранение фото из фотогалереи
        log.info("Saving images...");
        if (page_images.length > 1) {
            log.info(String.valueOf(page_images.length));
            log.info("images found on page");
            log.info(Arrays.toString(page_images));
            String imagesToSave = "";
            List<String> list = Arrays.stream(originalPage.getImages().split(",")).collect(Collectors.toList());
            for (MultipartFile image : page_images) {
                log.info("Printing image info:");
                log.info(image.toString());
                if (image.getSize() > 0) {
                    imageUtil.saveFile(imageSaveDir, image.getOriginalFilename(), image);
                    list.add(image.getOriginalFilename());
                }
            }
            imagesToSave = String.join(",", list);
            log.info("Saved images: " + imagesToSave);
            page.setImages(imagesToSave);

        } else if (page_images.length == 1 && page_images[0].getSize() > 0) {
            MultipartFile image = page_images[0];
            List<String> list = Arrays.stream(originalPage.getImages().split(",")).collect(Collectors.toList());
            imageUtil.saveFile(imageSaveDir, image.getOriginalFilename(), image);
            log.info("Saved single image: " + image.getOriginalFilename());
            page.setImages(String.join(",", list) + "," + image.getOriginalFilename());
        } else page.setImages(originalPage.getImages());

        log.info(page.toString());
        log.info(page.getImages());

        //Сохранение фото из доп.фотогалереи
        log.info("Saving additional images...");
        if (page_add_images.length > 1) {
            log.info(String.valueOf(page_add_images.length));
            log.info("images found on page");
            log.info(Arrays.toString(page_add_images));
            String imagesToSave = "";
            List<String> list = Arrays.stream(originalPage.getAdd_images().split(",")).collect(Collectors.toList());
            for (MultipartFile image : page_add_images) {
                log.info("Printing image info:");
                log.info(image.toString());
                if (image.getSize() > 0) {
                    imageUtil.saveFile(imageSaveDir, image.getOriginalFilename(), image);
                    list.add(image.getOriginalFilename());
                }
            }
            imagesToSave = String.join(",", list);
            log.info("Saved images: " + imagesToSave);
            page.setAdd_images(imagesToSave);

        } else if (page_add_images.length == 1 && page_add_images[0].getSize() > 0) {
            MultipartFile image = page_add_images[0];
            List<String> list = Arrays.stream(originalPage.getAdd_images().split(",")).collect(Collectors.toList());
            imageUtil.saveFile(imageSaveDir, image.getOriginalFilename(), image);
            log.info("Saved single image: " + image.getOriginalFilename());
            page.setAdd_images(String.join(",", list) + "," + image.getOriginalFilename());
        } else page.setAdd_images(originalPage.getAdd_images());

        log.info(page.toString());
        log.info(page.getAdd_images());

        log.info("Saving documents...");
        for (int i = 1; i < document_names.length; i++) {
            if (document_names[i].isEmpty()) continue;
            AboutPage.Document document = new AboutPage.Document();
            document.setPage(originalPage);
            document.setName(document_names[i]);
            MultipartFile fileToSave = document_files[i];
            if (fileToSave.getSize() > 0) {
                imageUtil.saveFile("/documents/", fileToSave.getOriginalFilename(), fileToSave);
                document.setFile(fileToSave.getOriginalFilename());
            } else continue;
            documentRepository.save(document);
        }

        log.info("Final page info to save: " + page);
        return page;
    }

    @Override
    public ServicesPage saveServicesPageInfo(ServicesPage page,
                                             String[] titles,
                                             String[] descriptions,
                                             MultipartFile[] service_images) {
        page.setId(1);

        List<ServicesPage.ServiceDescription> originalList = pageRepository.getServicesPage().orElseGet(ServicesPage::new).getServiceDescriptions();
        page.setServiceDescriptions(new ArrayList<>());
        for (int i = 1; i < titles.length; i++) {
            ServicesPage.ServiceDescription service = new ServicesPage.ServiceDescription();
            service.setTitle(titles[i]);
            service.setDescription(descriptions[i]);
            if (service_images.length > 0) {
                if (service_images[i].getSize() > 0) {
                    try {
                        imageUtil.saveFile(imageSaveDir + "/services/",
                                service_images[i].getOriginalFilename(),
                                service_images[i]);
                        service.setImage(service_images[i].getOriginalFilename());
                    } catch (Exception e) {
                        log.info(e.getMessage());
                        log.info(Arrays.toString(e.getStackTrace()));
                        log.info("Can't save image");
                    }
                } else if (i <= originalList.size()) service.setImage(originalList.get(i - 1).getImage());
            }
            page.getServiceDescriptions().add(service);
        }

        return page;
    }

}
