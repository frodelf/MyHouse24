package com.avada.myHouse24.validator;

import com.avada.myHouse24.model.ScoreDTO;
import com.avada.myHouse24.services.impl.ScoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ScoreValidator implements Validator {
    private final ScoreServiceImpl scoreService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ScoreDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ScoreDTO scoreDTO = (ScoreDTO) target;
        if (scoreService.existNumber(scoreDTO.getNumber())) {
            errors.rejectValue("number", "field.required", "Такий рахунок вже існує");
        }
        if(scoreDTO.getFlat() == null){
            errors.rejectValue("flat", "field.required", "Поле не може бути пустим");
        }
    }
}
