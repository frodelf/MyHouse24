package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.MasterRequest;
import com.avada.myHouse24.enums.MasterRequestStatus;
import com.avada.myHouse24.enums.RolesForMasterRequest;
import com.avada.myHouse24.model.MasterRequestDTO;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MasterRequestMapper {
    public MasterRequestDTO toDto(MasterRequest masterRequest){
        MasterRequestDTO masterRequestDTO = new MasterRequestDTO();
        masterRequestDTO.setId(masterRequest.getId());
        if(masterRequest.getRole()!=null)masterRequestDTO.setRole(masterRequest.getRole());
        else masterRequestDTO.setRole(RolesForMasterRequest.AnySpecialist.getName());
        masterRequestDTO.setFlat(masterRequest.getFlat());
        masterRequestDTO.setDate(masterRequest.getDate());
        masterRequestDTO.setTime(masterRequest.getDate());
        masterRequestDTO.setDescription(masterRequest.getDescription());
        masterRequestDTO.setStatus(masterRequest.getStatus());
        return masterRequestDTO;
    }
    public MasterRequest toEntity(MasterRequestDTO masterRequestDTO){
        MasterRequest masterRequest = new MasterRequest();
        masterRequest.setId(masterRequestDTO.getId());
        if(RolesForMasterRequest.getRoleByName(masterRequestDTO.getRole()) != null)masterRequest.setRole(masterRequestDTO.getRole());
        else masterRequest.setRole(null);
        masterRequest.setFlat(masterRequestDTO.getFlat());
        masterRequest.setDate(DateTimeUtil.combineDateTime(masterRequestDTO.getDate(), masterRequestDTO.getTime()));
        masterRequest.setDescription(masterRequestDTO.getDescription());
        masterRequest.setUser(masterRequestDTO.getFlat().getUser());
        masterRequest.setStatus(MasterRequestStatus.NEW.name());
        return masterRequest;
    }
    public List<MasterRequestDTO> toDtoList(List<MasterRequest> masterRequestList){
        List<MasterRequestDTO> masterRequestDTOList = new ArrayList<>();
        for (MasterRequest masterRequest : masterRequestList) {
            masterRequestDTOList.add(toDto(masterRequest));
        }
        return masterRequestDTOList;
    }
}
