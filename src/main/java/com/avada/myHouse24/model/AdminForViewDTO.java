package com.avada.myHouse24.model;

import com.avada.myHouse24.enums.UserStatus;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AdminForViewDTO {
    private String id;
    @Length(max = 50)
    private String fullName;
    private String role;
    @Length(max = 10)
    private String phone;
    @Length(max = 50)
    private String email;
    private UserStatus status;
}
