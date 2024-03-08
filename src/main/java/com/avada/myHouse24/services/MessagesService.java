package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Message;
import com.avada.myHouse24.model.FlatForMessageDTO;
import com.avada.myHouse24.model.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

public interface MessagesService {
    Message findById(long id);

    List<Message> getAll();

    Page<Message> findAll(Pageable pageable);

    Message save(Message msg);
    void deleteById(long id);

    Page<MessageDTO> getPage(int pageNumber, int pageSize, String searchQuery, String sortField, String sortOrder);

    List<FlatForMessageDTO> findFlatDTOsWithDebt();

    List<FlatForMessageDTO> findFlatDTOsWithNegativeBalanceByHouse(Long id);

    List<FlatForMessageDTO> findFlatDTOsWithNegativeBalanceByHouseAndSection(Long houseId, Long sectionId);

    List<FlatForMessageDTO> findFlatDTOsByHouseAndSectionAndFloor(Long houseId, Long sectionId, long floorId);

    List<FlatForMessageDTO> findFlatDTOsByHouse(Long id);

    List<FlatForMessageDTO> findFlatDTOseByHouseAndSection(Long houseId, Long sectionId);

    List<FlatForMessageDTO> findFlatDTOsWithNegativeBalanceByHouseAndSectionAndFloor(Long houseId, Long sectionId, Long floorId);
}
