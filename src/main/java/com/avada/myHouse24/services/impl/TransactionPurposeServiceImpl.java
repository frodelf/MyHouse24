package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.TransactionPurpose;
import com.avada.myHouse24.repository.TransactionPurposeRepository;
import com.avada.myHouse24.services.TransactionPurposeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionPurposeServiceImpl implements TransactionPurposeService {
    private final TransactionPurposeRepository transactionPurposeRepository;

    @Override
    public TransactionPurpose getByName(String name) {
        return transactionPurposeRepository.findByName(name).get();
    }

    @Override
    public List<TransactionPurpose> getAll() {
        return transactionPurposeRepository.findAll();
    }
}
