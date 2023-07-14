package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.TransactionPurpose;

import java.util.List;

public interface TransactionPurposeService {
    TransactionPurpose getByName(String name);
    TransactionPurpose getById(long id);
    List<TransactionPurpose> getAll();
    void save(TransactionPurpose transactionPurpose);
    void deleteById(long id);
}
