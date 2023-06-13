package com.avada.myHouse24.model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountTransactionInDTO {
    @NotBlank(message = "Поле не повино бути пустим")
    private String id;
    private Date date;
    @NotBlank(message = "Поле не повино бути пустим")
    @Length(max = 50, message = "Довжина поля повинна бути до 50 символів")
    @Pattern(regexp = "^(?!Виберите\\.\\.\\.).*$", message = "Поле не вибрано")
    private String transactionPurposeName;
    @NotBlank(message = "Поле не повино бути пустим")
    @Length(max = 50, message = "Довжина поля повинна бути до 50 символів")
    @Pattern(regexp = "^(?!Виберите\\.\\.\\.).*$", message = "Поле не вибрано")
    private String userName;
    @NotBlank(message = "Поле не повино бути пустим")
    @Pattern(regexp = "^(?!Виберите\\.\\.\\.).*$", message = "Поле не вибрано")
    private String scoreId;
    private boolean isIncome;
    @NotBlank(message = "Поле не повино бути пустим")
    @Length(max = 10, message = "Довжина поля повинна бути до 10 символів")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Поле повинно містити десяткове число")
    private String sum;
    @NotBlank(message = "Поле не повино бути пустим")
    @Length(max = 50, message = "Довжина поля повинна бути до 50 символів")
    @Pattern(regexp = "^(?!Виберите\\.\\.\\.).*$", message = "Поле не вибрано")
    private String adminName;
    @NotBlank(message = "Поле не повино бути пустим")
    @Length(max = 50, message = "Довжина поля повинна бути до 50 символів")
    private String comment;
    private boolean addToStats;
}
