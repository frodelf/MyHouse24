package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.model.HouseForAddDto;
import com.avada.myHouse24.model.HouseForViewDto;
import com.avada.myHouse24.repo.HouseRepository;
import com.avada.myHouse24.services.HouseService;
import com.avada.myHouse24.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseRepository houseRepository;
    private final AdminServiceImpl adminService;
    private final AmazonS3Service amazonS3Service;
    @Override
    public House getByName(String name) {
        return houseRepository.findByName(name).get();
    }

    @Override
    public House getById(long id) {
        return houseRepository.findById(id).get();
    }

    @Override
    public List<House> getAll() {
        return houseRepository.findAll();
    }

    @Override
    public List<String> getAllName() {
        return houseRepository.findAll().stream()
                .map(House::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Page<House> getPage(int pageNumber, Model model) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int)Math.ceil(houseRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(houseRepository.findAll().size()/size-1) : 0;
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return houseRepository.findAll(pageRequest);
    }

    @Override
    public Page<HouseForViewDto> getPage(int pageNumber, Model model, List<HouseForViewDto> houseForViewDtos) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(houseForViewDtos.size() / size-1 ) > 0 ? (int) Math.ceil(houseForViewDtos.size() / size-1 ) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, houseForViewDtos.size());
        List<HouseForViewDto> pageList = houseForViewDtos.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<HouseForViewDto> userPage = new PageImpl<>(pageList, pageable, houseForViewDtos.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return userPage;
    }

    @Override
    public void deleteById(long id) {
        houseRepository.deleteById(id);
    }

    @Override
    public void add(HouseForAddDto houseDto) throws IOException {
        House houseExample = houseRepository.findById(houseDto.getId()).get();
        House house = new House();
        if(houseDto.getId() != null){
            house = getById(houseDto.getId());
            house.setSections(new ArrayList<Section>());
            house.setFloors(new ArrayList<Floor>());
            house.setAdmins(new ArrayList<Admin>());
        }
        for (String section : houseDto.getSections()) {
            if(house.getSections() == null)house.setSections(new ArrayList<Section>());
            house.getSections().add(new Section(section));
        }
        for (String floor : houseDto.getFloors()) {
            if(house.getFloors() == null)house.setFloors(new ArrayList<Floor>());
            house.getFloors().add(new Floor(floor));
        }
        for (String user : houseDto.getUsers()) {
            if(house.getAdmins() == null)house.setAdmins(new ArrayList<Admin>());
            if(user!=null)house.getAdmins().add(adminService.getByName(user));
        }
        house.setName(houseDto.getName());
        house.setAddress(houseDto.getAddress());
        if(!houseDto.getImage().getOriginalFilename().isBlank() && houseDto.getImage().getOriginalFilename() != null && !houseDto.getImage().getOriginalFilename().isEmpty()){
            if(houseExample.getImage() != null)amazonS3Service.deleteFile(houseExample.getImage());
            house.setImage(amazonS3Service.uploadFile(houseDto.getImage()));
        }
        if(!houseDto.getImage1().getOriginalFilename().isBlank() && houseDto.getImage1().getOriginalFilename() != null && !houseDto.getImage1().getOriginalFilename().isEmpty()){
            if(houseExample.getImage1() != null)amazonS3Service.deleteFile(houseExample.getImage1());
            house.setImage1(amazonS3Service.uploadFile(houseDto.getImage1()));
        }
        if(!houseDto.getImage2().getOriginalFilename().isBlank() && houseDto.getImage2().getOriginalFilename() != null && !houseDto.getImage2().getOriginalFilename().isEmpty()){
            if(houseExample.getImage2() != null)amazonS3Service.deleteFile(houseExample.getImage2());
            house.setImage2(amazonS3Service.uploadFile(houseDto.getImage2()));
        }
        if(!houseDto.getImage3().getOriginalFilename().isBlank() && houseDto.getImage3().getOriginalFilename() != null && !houseDto.getImage3().getOriginalFilename().isEmpty()){
            if(houseExample.getImage3() != null)amazonS3Service.deleteFile(houseExample.getImage3());
            house.setImage3(amazonS3Service.uploadFile(houseDto.getImage3()));
        }
        if(!houseDto.getImage4().getOriginalFilename().isBlank() && houseDto.getImage4().getOriginalFilename() != null && !houseDto.getImage4().getOriginalFilename().isEmpty()){
            if(houseExample.getImage4() != null)amazonS3Service.deleteFile(houseExample.getImage4());
            house.setImage4(amazonS3Service.uploadFile(houseDto.getImage4()));
        }
        houseRepository.save(house);
    }
    public List<House> forSelect(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<House> housePage;

        if (search != null && !search.isEmpty()) {
            housePage = houseRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            housePage = houseRepository.findAll(pageable);
        }

        return housePage.getContent();
    }
}
