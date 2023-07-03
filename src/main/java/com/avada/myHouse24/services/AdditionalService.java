package com.avada.myHouse24.services;

import java.util.List;

public interface AdditionalService {
    public List<com.avada.myHouse24.entity.AdditionalService> getAll();
    public void save(com.avada.myHouse24.entity.AdditionalService additionalService);
    public com.avada.myHouse24.entity.AdditionalService getById(Long id);
}
