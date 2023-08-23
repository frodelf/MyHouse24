package com.avada.myHouse24.entity.pages;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;



@Data
@MappedSuperclass
public class Page {

    @Id
    private long id;

    private String seo_title, seo_description, seo_keywords;

}
