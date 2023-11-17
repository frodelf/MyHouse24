package com.avada.myHouse24.validator;

import com.avada.myHouse24.entity.InvoiceAdditionalService;
import com.avada.myHouse24.model.InvoiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.sql.Date;

@Component
@RequiredArgsConstructor
public class InvoiceValidator implements Validator {

    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return InvoiceDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InvoiceDto invoiceDto = (InvoiceDto) target;

        BindingResult bindingResult = (BindingResult) errors;
        if (bindingResult.hasFieldErrors("toDate")) {
            bindingResult.rejectValue("toDate", "field.required", "Поле не може бути пустим");
        }

        if (bindingResult.hasFieldErrors("fromDate")) {
            bindingResult.rejectValue("fromDate", "field.required", "Поле не може бути пустим");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "number", "field.required", "Поле не може бути пустим");
        ValidationUtils.rejectIfEmpty(errors, "date", "field.required", "Поле не може бути пустим");
        ValidationUtils.rejectIfEmpty(errors, "flat", "field.required", "Поле не може бути пустим");
        ValidationUtils.rejectIfEmpty(errors, "score", "field.required", "Поле не може бути пустим");
        ValidationUtils.rejectIfEmpty(errors, "addToStats", "field.required", "Поле не може бути пустим");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "status", "field.required", "Поле не може бути пустим");
        ValidationUtils.rejectIfEmpty(errors, "tariff", "field.required", "Поле не може бути пустим");

        int index = 0;
        if(invoiceDto.getInvoiceAdditionalServices() != null) {
            for (InvoiceAdditionalService service : invoiceDto.getInvoiceAdditionalServices()) {
                if (service == null) {
                    errors.rejectValue("invoiceAdditionalServices[" + index + "]", "field.required", "Поле не може бути пустим");
                } else {
                    ValidationUtils.rejectIfEmpty(errors, "invoiceAdditionalServices[" + index + "].additionalService", "field.required", "Поле не може бути пустим");
                    ValidationUtils.rejectIfEmpty(errors, "invoiceAdditionalServices[" + index + "].price", "field.required", "Поле не може бути пустим");
                    ValidationUtils.rejectIfEmpty(errors, "invoiceAdditionalServices[" + index + "].consumption", "field.required", "Поле не може бути пустим");
                }
                index++;
            }
        }
    }
}