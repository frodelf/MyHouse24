package com.avada.myHouse24.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountTransactionOutDTO {
    @NotBlank(message = "Поле не повино бути пустим")
    @Length(max = 10, message = "Довжина поля повинна бути до 10 символів")
    private String id;
    private Date date;
    @Pattern(regexp = "^(?!Виберите\\.\\.\\.).*$", message = "Поле не вибрано")
    private String transactionPurposeName;
    private boolean isIncome;
    @NotBlank(message = "Поле не повино бути пустим")
    @Length(max = 10, message = "Довжина поля повинна бути до 10 символів")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Поле повинно містити десяткове число")
    private String sum;
    @Pattern(regexp = "^(?!Виберите\\.\\.\\.).*$", message = "Поле не вибрано")
    private String adminName;
    @NotBlank(message = "Поле не повино бути пустим")
    @Length(max = 500, message = "Довжина поля повинна бути до 500 символів")
    private String comment;
    private boolean addToStats;
}
