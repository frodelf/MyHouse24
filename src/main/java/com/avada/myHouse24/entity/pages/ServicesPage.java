package com.avada.myHouse24.entity.pages;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name="page_service")
public class ServicesPage extends Page {

    @ElementCollection
    List<ServiceDescription> serviceDescriptions;

    @Data
    @Embeddable
    public static class ServiceDescription {
        private String title;
        private String description;
        private String image;
    }
}
