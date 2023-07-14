package com.avada.myHouse24.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForAddDTO {
    @NotBlank(message = "Поле не може бути порожнім")
    private String id;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String firstName;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String lastName;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String fathersName;
    @NotNull(message = "Поле не може бути порожнім")
    private LocalDate birthday;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String phone;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String viber;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String telegram;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String email;
    private String password;
    private String passwordAgain;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String status;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 500, message = "Поле не може бути більше 50 символів")
    private String description;
    private String imageName;

    public UserForAddDTO(String id, String firstName, String lastName, String fathersName, LocalDate birthday, String phone, String viber, String telegram, String email, String status, String description, String imageName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fathersName = fathersName;
        this.birthday = birthday;
        this.phone = phone;
        this.viber = viber;
        this.telegram = telegram;
        this.email = email;
        this.status = status;
        this.description = description;
        this.imageName = imageName;
    }
}
