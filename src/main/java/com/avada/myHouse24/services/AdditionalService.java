package com.avada.myHouse24.services;

import java.util.List;

public interface AdditionalService {
    List<com.avada.myHouse24.entity.AdditionalService> getAll();
    void save(com.avada.myHouse24.entity.AdditionalService additionalService);
    void saveList(List<com.avada.myHouse24.entity.AdditionalService> additionalServices);
    void deleteById(long id);
    com.avada.myHouse24.entity.AdditionalService getById(Long id);
    com.avada.myHouse24.entity.AdditionalService getByName(String name);
    List<com.avada.myHouse24.entity.AdditionalService> forSelect(int page, int pageSize, String search);
}
