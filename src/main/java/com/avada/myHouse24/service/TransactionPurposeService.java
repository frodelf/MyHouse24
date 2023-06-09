package com.avada.myHouse24.service;

import com.avada.myHouse24.entity.TransactionPurpose;

import java.util.List;

public interface TransactionPurposeService {
    TransactionPurpose getByName(String name);
    List<TransactionPurpose> getAll();
}
