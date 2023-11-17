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
    long count();
    List<Flat> findByScoreBalanceLessThan(double balance);
    List<Flat> findByScoreBalanceLessThanAndHouseIdAndSectionId(double balance, Long houseId, Long sectionId);
    List<Flat> findByScoreBalanceLessThanAndHouseIdAndSectionIdAndFloorId(double balance, Long houseId, Long sectionId, Long floorId);
    List<Flat> findByHouseIdAndSectionIdAndFloorId(Long houseId, Long sectionId, Long floorId);
    List<Flat> findByHouseIdAndSectionId(Long houseId, Long sectionId);
    List<Flat> findByHouseIdAndFloorId(Long houseId, Long floorId);
    List<Flat> findByScoreBalanceLessThanAndHouseIdAndFloorId(double balance, Long houseId, Long floorId);
    List<Flat> findFlatsByHouseId(Long Id);
    List<Flat> findByScoreBalanceLessThanAndHouseId(double balance, Long houseId);

    List<Flat> findByHouseId(Long Id);
}
