package com.avada.myHouse24.validator;

import com.avada.myHouse24.model.HouseForAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HouseValidator implements Validator {
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "pdf");
    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB

    @Override
    public boolean supports(Class<?> clazz) {
        return HouseForAddDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HouseForAddDto houseDto = (HouseForAddDto) target;

        validateFileExtension(houseDto.getImage(), "image", errors);
        validateFileExtension(houseDto.getImage1(), "image1", errors);
        validateFileExtension(houseDto.getImage2(), "image2", errors);
        validateFileExtension(houseDto.getImage3(), "image3", errors);
        validateFileExtension(houseDto.getImage4(), "image4", errors);

        validateFileSize(houseDto.getImage(), "image", errors);
        validateFileSize(houseDto.getImage1(), "image1", errors);
        validateFileSize(houseDto.getImage2(), "image2", errors);
        validateFileSize(houseDto.getImage3(), "image3", errors);
        validateFileSize(houseDto.getImage4(), "image4", errors);

        validateFile(houseDto.getImage(), "image", errors);
        validateFile(houseDto.getImage1(), "image1", errors);
        validateFile(houseDto.getImage2(), "image2", errors);
        validateFile(houseDto.getImage3(), "image3", errors);
        validateFile(houseDto.getImage4(), "image4", errors);

        if (houseDto.getSections() == null || houseDto.getSections().isEmpty()) {
            errors.rejectValue("sections", "sections.empty", "Будинок повинен містити як мінімум одну секцію");
        }

        if (houseDto.getFloors() == null || houseDto.getFloors().isEmpty()) {
            errors.rejectValue("floors", "floors.empty", "Будинок повинен мітить як мінімум один поверх");
        }

        if (houseDto.getUsers() == null || houseDto.getUsers().isEmpty()) {
            errors.rejectValue("users", "users.empty", "Хтось із амінів повинен бути вказаний");
        }
    }

    private void validateFile(MultipartFile file, String fieldName, Errors errors) {
        if (file == null || file.isEmpty() || file.getOriginalFilename().isBlank()) {
            errors.rejectValue(fieldName, "file.required", "Файл не може бути порожнім");
            return;
        }
    }

    private void validateFileExtension(MultipartFile file, String fieldName, Errors errors) {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                String extension = getFileExtension(originalFilename);
                if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                    errors.rejectValue(fieldName, "file.extension.invalid", "Допустимі розширення файлів: jpg, jpeg, png, pdf");
                }
            }
        }
    }

    private void validateFileSize(MultipartFile file, String fieldName, Errors errors) {
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > MAX_FILE_SIZE) {
                errors.rejectValue(fieldName, "file.size.exceeded", "Максимальний розмір файлу: 20MB");
            }
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }
}
