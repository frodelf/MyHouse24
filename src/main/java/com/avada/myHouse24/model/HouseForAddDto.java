package com.avada.myHouse24.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class HouseForAddDto {
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String name;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 100, message = "Поле не може бути більше 100 символів")
    private String address;
    @NotNull(message = "Фото повино бути вибране")
    private MultipartFile image;
    @NotNull(message = "Фото повино бути вибране")
    private MultipartFile image1;
    @NotNull(message = "Фото повино бути вибране")
    private MultipartFile image2;
    @NotNull(message = "Фото повино бути вибране")
    private MultipartFile image3;
    @NotNull(message = "Фото повино бути вибране")
    private MultipartFile image4;
    private List<String> sections;
    private List<String> floors;
    private List<String> users;
}
