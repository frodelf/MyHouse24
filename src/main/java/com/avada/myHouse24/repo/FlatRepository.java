package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FlatRepository extends JpaRepository<Flat, Long> {
    @Query(value = "SELECT MAX(id) FROM Flat")
    Long findMaxId();
    Optional<Flat> findByNumber(int number);
    List<Flat> findByScoreBalanceLessThan(double balance);
    List<Flat> findByScoreBalanceLessThanAndHouseId(double balance, Long houseId);
    List<Flat> findByScoreBalanceLessThanAndHouseIdAndSectionId(double balance, Long houseId, Long sectionId);
    List<Flat> findByScoreBalanceLessThanAndHouseIdAndSectionIdAndFloorId(double balance, Long houseId, Long sectionId, Long floorId);
    List<Flat> findByHouseIdAndSectionIdAndFloorId(Long houseId, Long sectionId, Long floorId);
    List<Flat> findByHouseIdAndNumber(Long houseId, int flatNumber);
    List<Flat> findByHouseIdAndSectionId(Long houseId, Long sectionId);
    List<Flat> findByScoreBalanceLessThanAndHouseIdAndNumber(double balance, Long houseId, int flatNumber);
    List<Flat> findByHouseIdAndFloorId(Long houseId, Long floorId);
    List<Flat> findByScoreBalanceLessThanAndHouseIdAndFloorId(double balance, Long houseId, Long floorId);
    List<Flat> findFlatsByHouseId(Long Id);

    List<Flat> findByHouseId(Long Id);
}
