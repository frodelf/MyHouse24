package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.TemplateForInvoice;
import com.avada.myHouse24.model.TemplateForInvoiceDTO;
import com.avada.myHouse24.services.impl.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class TemplateForInvoiceMapper {
    private final AmazonS3Service amazonS3Service;

    public TemplateForInvoice toEntity(TemplateForInvoiceDTO templateForInvoiceDTO){
        TemplateForInvoice templateForInvoice = new TemplateForInvoice();
        templateForInvoice.setId(templateForInvoiceDTO.getId());
        templateForInvoice.setName(templateForInvoiceDTO.getName());
        templateForInvoice.setIsMain(templateForInvoiceDTO.getIsMain());
        templateForInvoice.setFileName(amazonS3Service.uploadFile(templateForInvoiceDTO.getFileTemplate()));
        return templateForInvoice;
    }
}
