package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.pages.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface WebsiteService {
    MainPage getMainPage();

    AboutPage getAboutPage();

    ServicesPage getServicesPage();

    ContactsPage getContactsPage();

    Page savePage(Page page);

    List<AboutPage.Document> getAllDocuments();

    AboutPage deleteImageAndGetPage(AboutPage page, int index);

    void deleteDocument(Long document_id);

    MainPage saveMainPageImages(MainPage page,
                                MultipartFile page_slide1,
                                MultipartFile page_slide2,
                                MultipartFile page_slide3,
                                MultipartFile page_block_1_img,
                                MultipartFile page_block_2_img,
                                MultipartFile page_block_3_img,
                                MultipartFile page_block_4_img,
                                MultipartFile page_block_5_img,
                                MultipartFile page_block_6_img) throws IOException;

    AboutPage saveAboutPageInfo(AboutPage page,
                                MultipartFile page_director_image,
                                MultipartFile[] page_images,
                                MultipartFile[] page_add_images,
                                String[] document_names,
                                MultipartFile[] document_files) throws IOException;

    ServicesPage saveServicesPageInfo(ServicesPage page,
                                      String[] titles,
                                      String[] descriptions,
                                      MultipartFile[] service_images);
}
