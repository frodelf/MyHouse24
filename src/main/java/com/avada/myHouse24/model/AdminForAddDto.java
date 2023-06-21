package com.avada.myHouse24.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminForAddDto {
    private Long id;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String firstName;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String lastName;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 13, message = "Поле не може бути більше 50 символів")
    private String phone;
    @NotBlank(message = "Поле не може бути порожнім")
    private String role;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String status;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    @Email(message = "Поле повинно бути email адресою")
    private String email;
    private String password;
    private String passwordAgain;
}
