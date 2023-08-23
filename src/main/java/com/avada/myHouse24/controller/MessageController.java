package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.mapper.HouseMapper;
import com.avada.myHouse24.model.FlatForMessageDTO;
import com.avada.myHouse24.repo.FlatRepository;
import com.avada.myHouse24.services.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/messages/")
public class MessageController {
    private final HouseMapper houseMapper;
    private final MessagesServiceImpl messageService;
    private final UserServiceImpl userService;
    private final HouseServiceImpl houseService;
    private final FlatServiceImpl flatService;
    private final FloorServiceImpl floorService;
    private final SectionServiceImpl sectionService;
    private final FlatRepository flatRepository;
    private final FlatMapper flatMapper;

    @GetMapping
    public String getMessages(Model model) {
        model.addAttribute("messages", messageService.findAll());
        return "/message/msgList";
    }

    @GetMapping("{id}")
    public String getMessage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("message", messageService.findById(id));
        return "/message/msg";
    }

    @GetMapping("new")
    public String createMessagePage(Model model) {
        Message message = new Message();
        model.addAttribute("message", message);
        model.addAttribute("houses", houseMapper.toDtoForViewList(houseService.getAll()));

        return "/admin/message/msgNew";
    }

    @PostMapping("create")
    public String createNewMessage(@ModelAttribute("message") Message message,
                                @RequestParam(name = "balance", defaultValue = "false") Boolean balance,
                                @RequestParam(name = "house", defaultValue = "0") Long houseId,
                                @RequestParam(name = "section", defaultValue = "0") Long sectionId,
                                @RequestParam(name = "floor", defaultValue = "0") Long floorId,
                                @RequestParam(name = "flatNumber", defaultValue = "0") Long flatId) throws IOException {
        List<User> recipients = new ArrayList<>();
        if (flatId == 0L) {
            if (floorId == 0L && sectionId == 0L && houseId == 0L && !balance) {
                for (Flat flat : flatService.getAll()) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName("Всем");
            } else if (floorId == 0L && sectionId == 0L && houseId == 0L && balance) {
                for (Flat flat : flatRepository.findByScoreBalanceLessThan(0D)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName("Всем с задоженностями");
            } else if (floorId == 0L && sectionId == 0L && houseId > 0L && !balance) {
                for (Flat flat : flatRepository.findFlatsByHouseId(houseId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseService.getById(houseId).getName());
            } else if (floorId == 0L && sectionId == 0L && houseId > 0L && balance) {
                for (Flat flat : flatRepository.findByScoreBalanceLessThanAndHouseId(0D,houseId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseId + " c задолженостями");
            } else if (floorId == 0L && sectionId > 0L && houseId > 0L && !balance) {
                for (Flat flat : flatRepository.findByHouseIdAndSectionId(houseId, sectionId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName((houseId) + ", " + sectionId);
            } else if (floorId == 0L && sectionId > 0L && houseId > 0L && balance) {
                for (Flat flat : flatRepository.findByScoreBalanceLessThanAndHouseIdAndSectionId(0d,houseId, sectionId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseId + ", " + sectionId + " c задолженостями");
            } else if (floorId > 0L && sectionId == 0L && houseId > 0L && !balance) {
                for (Flat flat : flatRepository.findByHouseIdAndFloorId(houseId, floorId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseId + ", " + floorId);
            } else if (floorId > 0L && sectionId == 0L && houseId > 0L && balance) {
                for (Flat flat : flatRepository.findByScoreBalanceLessThanAndHouseIdAndFloorId(0d,houseId, floorId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseId + ", " + floorId + " c задолженостями");
            } else if (floorId > 0L && sectionId > 0L && houseId > 0L && !balance) {
                for (Flat flat : flatRepository.findByHouseIdAndSectionIdAndFloorId(houseId, sectionId, floorId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseId + ", " + sectionId + ", " + floorId);
            } else if (floorId > 0L && sectionId > 0L && houseId > 0L && balance) {
                for (Flat flat : flatRepository.findByScoreBalanceLessThanAndHouseIdAndSectionIdAndFloorId(0d,houseId, sectionId, floorId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseId + ", " + sectionId + ", " + floorId + " c задолженостями");
            }
        } else {
            Flat flat = flatService.getById(flatId);
            recipients.add(flat.getUser());
            message.setRecipientsName(flat.getHouse().getName() + ", " + flat.getSection() + ", " + flat.getFloor() + ", кв." + flat.getNumber());
        }
        message.setRecipients(recipients);
        messageService.save(message);
        return "redirect:/admin/messages/";
    }

    @GetMapping("/getHouseInfo")
    @ResponseBody
    public HouseInfoResponse getHouseInfo(@RequestParam Long houseId) {
        log.info("executing getHouseInfo with houseID: " + houseId);

        House house = houseService.getById(houseId);
        if (house != null) {
            List<Section> sections = new ArrayList<>();
            for (Section houseSection : house.getSections()) {
                sections.add(new Section(houseSection.getId(), houseSection.getName()));
            }
            log.info("House sections: " + sections);
            List<Floor> floors = new ArrayList<>();
            for(Floor houseFloor: house.getFloors()){
                floors.add(new Floor(houseFloor.getId(), houseFloor.getName()));
            }
            log.info("House floors: " + floors);
            return new HouseInfoResponse(sections, floors);
        }
        return new HouseInfoResponse(Collections.emptyList(), Collections.emptyList());
    }
    public static class HouseInfoResponse {
        private List<Section> sections;
        private List<Floor> floors;
        public HouseInfoResponse(List<Section> sections, List<Floor> floors) {
            this.sections = sections;
            this.floors = floors;
        }

        public List<Section> getSections() {
            return sections;
        }

        public void setSections(List<Section> sections) {
            this.sections = sections;
        }


        public List<Floor> getFloors() {
            return floors;
        }

        public void setFloors(List<Floor> floors) {
            this.floors = floors;
        }
    }
    @GetMapping("/getFilteredFlats")
    @ResponseBody
    public List<FlatForMessageDTO> getFilteredFlats(
            @RequestParam(required = false) boolean hasDebt,
            @RequestParam(required = false) Long houseId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Long floorId) {

        List<FlatForMessageDTO> filteredFlats;

        if (hasDebt) {
            if (houseId != null) {
                if (sectionId != null) {
                    if (floorId != null) {
                        filteredFlats = messageService.findFlatDTOsWithNegativeBalanceByHouseAndSectionAndFloor(houseId, sectionId, floorId);
                    } else {
                        filteredFlats = messageService.findFlatDTOsWithNegativeBalanceByHouseAndSection(houseId, sectionId);
                    }
                } else {
                    filteredFlats = messageService.findFlatDTOsWithNegativeBalanceByHouse(houseId);
                }
            } else {
                filteredFlats = messageService.findFlatDTOsWithDebt();
            }
        } else {
            if (houseId != null) {
                if (sectionId != null) {
                    if (floorId != null) {
                        filteredFlats = messageService.findFlatDTOsByHouseAndSectionAndFloor(houseId, sectionId, floorId);
                    } else {
                        filteredFlats = messageService.findFlatDTOseByHouseAndSection(houseId, sectionId);
                    }
                } else {
                    filteredFlats = messageService.findFlatDTOsByHouse(houseId);
                }
            } else {
                filteredFlats = flatMapper.toDtoForMsgList(flatRepository.findAll());
            }
        }

        return filteredFlats;
    }
}
