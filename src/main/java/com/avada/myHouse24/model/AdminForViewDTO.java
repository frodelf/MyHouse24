package com.avada.myHouse24.model;

import com.avada.myHouse24.enums.UserStatus;
import lombok.Data;

@Data
public class AdminForViewDTO {
    private String id;
    private String fullName;
    private String role;
    private String phone;
    private String email;
    private UserStatus status;
}
