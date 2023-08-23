package com.avada.myHouse24.entity.pages;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="page_about")
public class AboutPage extends Page {

    //Информация
    @NotBlank
    private String title, description;
    private String director_image;

    //Фотогалерея
    private String images = "";

    //Дополнительная информация
    @NotBlank
    private String add_title, add_description;

    //Дополнительная фотогалерея
    private String add_images = "";

    //Документы
    @OneToMany
    private List<Document> documents;

    @Data
    @Entity
    @Table(name="documents")
    public static class Document {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        @NotBlank
        private String name;
        @NotBlank
        private String file;

        @ManyToOne
        private AboutPage page;
    }
}
