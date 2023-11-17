package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.entity.TariffAdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TariffAdditionalServiceRepository extends JpaRepository<TariffAdditionalService, Long> {
    List<TariffAdditionalService> findAllByTariff(Tariff tariff);
    void deleteAllByTariff(Tariff tariff);
    boolean existsByAdditionalService_Id(Long id);
}
