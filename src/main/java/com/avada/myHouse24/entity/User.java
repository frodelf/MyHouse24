package com.avada.myHouse24.entity;

import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.services.registration.RegistrationRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_Name")
    private String firstName;
    @Column(name = "last_Name")
    private String lastName;
    @Column(name = "fathers_Name")
    private String fathersName;
    private Date birthday;
    @Column(name = "oauth_provider")
    private String oauthProvider;
    @Column(name = "oauth_id")
    private Integer oauthId;
    private String phone;
    private String viber;
    private String telegram;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String description;
    private String image;
    private boolean enabled = false;
    @OneToMany
    List<Flat> flats;
    public User(RegistrationRequest request) {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.fathersName = request.getFathersName();
        this.email = request.getEmail();
        this.password = request.getPassword();
    }

    public User(Long id, String firstName, String lastName, String fathersName, String phoneNumber, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fathersName = fathersName;
        this.phone = phoneNumber;
        this.email = email;
        this.password = password;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fathersName='" + fathersName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
