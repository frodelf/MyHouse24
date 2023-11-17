package com.avada.myHouse24.validator;

import com.avada.myHouse24.model.CounterDataDTO;
import com.avada.myHouse24.services.impl.CounterDataServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CounterDataValidator implements Validator {

    private final CounterDataServiceImpl counterDataService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CounterDataDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CounterDataDTO counterDataDTO = (CounterDataDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate", "field.required", "Поле 'fromDate' не може бути пустим");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "status", "field.required", "Поле 'status' не може бути пустим");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "data", "field.required", "Поле 'data' не може бути пустим");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "flat", "field.required", "Поле 'flat' не може бути пустим");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "additionalService", "field.required", "Поле 'additionalService' не може бути пустим");

        String number = counterDataDTO.getNumber();
        if (counterDataService.existNumber(number) && counterDataDTO.getId() == null) {
            errors.rejectValue("number", "field.duplicate", "Счетчик з таким номером уже существует");
        }
    }
}
