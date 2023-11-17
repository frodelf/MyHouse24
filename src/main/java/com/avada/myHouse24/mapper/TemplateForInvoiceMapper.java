package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.TemplateForInvoice;
import com.avada.myHouse24.model.TemplateForInvoiceDTO;
import com.avada.myHouse24.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateForInvoiceMapper {

    public TemplateForInvoice toEntity(TemplateForInvoiceDTO templateForInvoiceDTO){
        TemplateForInvoice templateForInvoice = new TemplateForInvoice();
        templateForInvoice.setId(templateForInvoiceDTO.getId());
        templateForInvoice.setName(templateForInvoiceDTO.getName());
        templateForInvoice.setIsMain(templateForInvoiceDTO.getIsMain());
        templateForInvoice.setFileName(ImageUtil.fileForTemplate(templateForInvoiceDTO.getFileTemplate()));
        return templateForInvoice;
    }
}
