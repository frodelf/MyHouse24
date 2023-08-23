package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Message;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.model.FlatForMessageDTO;
import com.avada.myHouse24.repo.FlatRepository;
import com.avada.myHouse24.repo.HouseRepository;
import com.avada.myHouse24.repo.MessageRepository;
import com.avada.myHouse24.services.MessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class MessagesServiceImpl implements MessagesService {
    private final HouseRepository houseRepository;
    private final MessageRepository messageRepository;
    private final FlatRepository flatRepository;
    private final FlatMapper flatMapper;

    @Override
    public Message findById(long id) {
        return messageRepository.findById(id).orElseThrow( /*() -> new NotFoundException()) */);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message save(Message msg) {
        return messageRepository.save(msg);
    }

    @Override
    public void deleteById(long id) {
        messageRepository.deleteById(id);
    }
    public List<FlatForMessageDTO> findFlatDTOsWithDebt() {
        log.info("Finding flats with debt.");
        return flatMapper.toDtoForMsgList(flatRepository.findByScoreBalanceLessThan(0));
    }

    public List<FlatForMessageDTO> findFlatDTOsWithNegativeBalanceByHouse(Long id) {
        log.info("Finding flats with negative balance by house: {}", id);
        return flatMapper.toDtoForMsgList(flatRepository.findByScoreBalanceLessThanAndHouseId(0d, id));
    }

    public List<FlatForMessageDTO> findFlatDTOsWithNegativeBalanceByHouseAndSection(Long houseId, Long sectionId) {
        log.info("Finding flats with negative balance by house: {} and section: {}", houseId, sectionId);
        return flatMapper.toDtoForMsgList(flatRepository.findByScoreBalanceLessThanAndHouseIdAndSectionId(0d,houseId, sectionId));
    }

    public List<FlatForMessageDTO> findFlatDTOsByHouseAndSectionAndFloor(Long houseId, Long sectionId, long floorId) {
        log.info("Finding flats by house: {}, section: {}, and floor: {}", houseId, sectionId, floorId);
        log.info(flatRepository.findByHouseIdAndSectionIdAndFloorId(houseId, sectionId, floorId).toString());
        List<FlatForMessageDTO> list = flatMapper.toDtoForMsgList(flatRepository.findByHouseIdAndSectionIdAndFloorId(houseId, sectionId, floorId));
        log.info(list.toString());
        House house = houseRepository.findById(1L).orElseThrow();
        if (houseId.equals(house.getId())){
            log.info("Names are equal");
        }else {
            log.info("names arent equal: house name indb: "+ house.getName()+"house name passing by method: "+houseId);
        }
        if (sectionId==1L){
            log.info("section are equal");
        }
        if (floorId==1L){
            log.info("floors are equal");
        }
        return list;
    }

    public List<FlatForMessageDTO> findFlatDTOsByHouse(Long id) {
        log.info("Finding flats by house: {}", id);
        return flatMapper.toDtoForMsgList(flatRepository.findByHouseId(id));
    }

    public List<FlatForMessageDTO> findFlatDTOseByHouseAndSection(Long houseId, Long sectionId) {
        log.info("Finding flats by house: {} and section: {}", houseId, sectionId);
        return flatMapper.toDtoForMsgList(flatRepository.findByHouseIdAndSectionId(houseId, sectionId));
    }

    public List<FlatForMessageDTO> findFlatDTOsWithNegativeBalanceByHouseAndSectionAndFloor(Long houseId, Long sectionId, Long floorId) {
        log.info("Finding flats with negative balance by house: {}, section: {}, and floor: {}", houseId, sectionId, floorId);
        return flatMapper.toDtoForMsgList(flatRepository.findByScoreBalanceLessThanAndHouseIdAndSectionIdAndFloorId(0d,houseId, sectionId, floorId));
    }
}
