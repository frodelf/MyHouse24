package com.avada.myHouse24.entity.pages;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
@Entity
@Table(name="page_contacts")
public class ContactsPage extends Page {

    //Контактная информация
    @NotBlank(message = "Необходимо заполнить поле")
    private String title, description, website_link;

    //Контакты
    @NotBlank(message = "Необходимо заполнить поле")
    private String name, location, address, phone, email;

    //Карта
    @NotBlank(message = "Необходимо заполнить поле")
    private String map_code;

}
