package com.avada.myHouse24.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TemplateForInvoiceDTO {
    private Long id;
    @NotBlank(message = "Поле не маже бути пустим")
    private String name;
    private MultipartFile fileTemplate;
    private Boolean isMain;
}