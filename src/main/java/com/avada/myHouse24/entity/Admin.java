package com.avada.myHouse24.entity;

import com.avada.myHouse24.enums.Theme;
import com.avada.myHouse24.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "admin")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Admin implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private Theme theme;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @ManyToOne
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(role.getPermissions());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !status.equals(UserStatus.INACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.equals(UserStatus.DISABLED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !status.equals(UserStatus.NEW);
    }

    public boolean hasPermission(String page) {
        if (role != null) {
            for (Permission permission : role.getPermissions()) {
                if (permission.getPage().equals(page)) {
                    return true;
                }
            }
        }
        return false;
    }
}