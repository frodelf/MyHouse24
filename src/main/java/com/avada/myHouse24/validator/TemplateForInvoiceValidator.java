package com.avada.myHouse24.validator;

import com.avada.myHouse24.model.TemplateForInvoiceDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
public class TemplateForInvoiceValidator implements Validator {
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("xls", "xlsx");
    private static final long MAX_FILE_SIZE = 30 * 1024 * 1024;

    @Override
    public boolean supports(Class<?> clazz) {
        return TemplateForInvoiceDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TemplateForInvoiceDTO template = (TemplateForInvoiceDTO) target;

        if (template.getFileTemplate() == null || template.getFileTemplate().isEmpty()) {
            errors.rejectValue("fileTemplate", "File.empty", "Файл пустий");
            return;
        }

        MultipartFile file = template.getFileTemplate();

        if (file.getSize() > MAX_FILE_SIZE) {
            errors.rejectValue("fileTemplate", "File.sizeExceeded", "Файл не може бути більше 30 MB");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            errors.rejectValue("fileTemplate", "File.invalidExtension", "Файл повиннен бути xlsx або xls");
        }

    }
}
