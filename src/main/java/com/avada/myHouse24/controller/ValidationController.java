//package com.avada.myHouse24.controller;
//
//import com.avada.myHouse24.service.UserService;
//import com.avada.myHouse24.service.registration.RegistrationRequest;
//import com.avada.myHouse24.service.registration.ValidationResponse;
//import com.avada.myHouse24.validators.RegistrationRequestValidator;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BeanPropertyBindingResult;
//import org.springframework.validation.Errors;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class ValidationController {
//    @Autowired UserService userService;
//    @PostMapping("/validate-registration")
//    @ResponseBody
//    public ValidationResponse validateRegistration(@RequestBody RegistrationRequest registrationRequest) {
//        RegistrationRequestValidator validator = new RegistrationRequestValidator(userService);
//        Errors errors = new BeanPropertyBindingResult(registrationRequest, "registrationRequest");
//        validator.validate(registrationRequest, errors);
//
//        ValidationResponse response = new ValidationResponse();
//        response.setValid(!errors.hasErrors());
//        if (errors.hasErrors()) {
//            for (FieldError fieldError : errors.getFieldErrors()) {
//                response.addError(fieldError.getField(), fieldError.getDefaultMessage());
//            }
//        }
//        return response;
//    }
//}