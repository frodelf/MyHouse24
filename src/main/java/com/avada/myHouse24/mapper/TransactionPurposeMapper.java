package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.TransactionPurpose;
import com.avada.myHouse24.model.TransactionPurposeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionPurposeMapper {
    public TransactionPurposeDTO toDto(TransactionPurpose transactionPurpose){
        return new TransactionPurposeDTO(transactionPurpose.getId(), transactionPurpose.getName(), transactionPurpose.isIncome());
    }
    public TransactionPurpose toEntity(TransactionPurposeDTO transactionPurposeDTO){
        TransactionPurpose transactionPurpose = new TransactionPurpose();
        if(transactionPurposeDTO.getId() != null)transactionPurpose.setId(transactionPurposeDTO.getId());
        transactionPurpose.setName(transactionPurposeDTO.getName());
        transactionPurpose.setIncome(transactionPurposeDTO.getIsIncome());
        return transactionPurpose;
    }
    public List<TransactionPurposeDTO> toDtoList(List<TransactionPurpose> transactionPurposes){
        List<TransactionPurposeDTO> transactionPurposeDTOS = new ArrayList<>();
        for (TransactionPurpose transactionPurpose : transactionPurposes) {
            transactionPurposeDTOS.add(toDto(transactionPurpose));
        }
        return transactionPurposeDTOS;
    }
}
