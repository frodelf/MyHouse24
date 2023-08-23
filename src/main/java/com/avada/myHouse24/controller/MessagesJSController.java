package com.avada.myHouse24.controller;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Section;
import com.avada.myHouse24.repo.HouseRepository;
import com.avada.myHouse24.repo.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/admin")
public class MessagesJSController {
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @GetMapping("/getSections")
    public List<Section> getSections(@RequestParam Long houseId) {
        House house = houseRepository.findById(houseId).orElse(null);
        if (house != null) {
            return house.getSections();
        }
        return Collections.emptyList();
    }

}
