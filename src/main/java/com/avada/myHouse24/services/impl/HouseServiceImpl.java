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
        double size = 1.0;
        int max = (int)Math.ceil(houseRepository.findAll().size()/size-1);
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return houseRepository.findAll(pageRequest);
    }

    @Override
    public Page<HouseForViewDto> getPage(int pageNumber, Model model, List<HouseForViewDto> houseForViewDtos) {
        double size = 1.0;
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
        House house = new House();
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
            if(user!=null)house.getAdmins().add(adminService.getByName(user.substring(0, user.indexOf(","))));
        }
        house.setName(houseDto.getName());
        house.setAddress(houseDto.getAddress());
        house.setImage(ImageUtil.imageForHouseForAdd(houseDto.getImage()));
        house.setImage1(ImageUtil.imageForHouseForAdd(houseDto.getImage1()));
        house.setImage2(ImageUtil.imageForHouseForAdd(houseDto.getImage2()));
        house.setImage3(ImageUtil.imageForHouseForAdd(houseDto.getImage3()));
        house.setImage4(ImageUtil.imageForHouseForAdd(houseDto.getImage4()));

        houseRepository.save(house);
    }
}
