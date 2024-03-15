package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.mapper.HouseMapper;
import com.avada.myHouse24.mapper.MessageMapper;
import com.avada.myHouse24.model.FlatForMessageDTO;
import com.avada.myHouse24.model.MessageDTO;
import com.avada.myHouse24.repo.AdminRepository;
import com.avada.myHouse24.repo.FlatRepository;
import com.avada.myHouse24.services.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/admin/messages/")
public class MessageController {
    private final AdminRepository adminRepository;
    private final HouseMapper houseMapper;
    private final MessagesServiceImpl messageService;
    private final UserServiceImpl userService;
    private final HouseServiceImpl houseService;
    private final FlatServiceImpl flatService;
    private final FloorServiceImpl floorService;
    private final SectionServiceImpl sectionService;
    private final FlatRepository flatRepository;
    private final FlatMapper flatMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;

    public MessageController(AdminRepository adminRepository, MessageMapper messageMapper, HouseMapper houseMapper, MessagesServiceImpl messageService, UserServiceImpl userService, HouseServiceImpl houseService, FlatServiceImpl flatService, FloorServiceImpl floorService, SectionServiceImpl sectionService, FlatRepository flatRepository, FlatMapper flatMapper, SimpMessagingTemplate messagingTemplate) {
        this.adminRepository = adminRepository;
        this.houseMapper = houseMapper;
        this.messageService = messageService;
        this.userService = userService;
        this.houseService = houseService;
        this.flatService = flatService;
        this.floorService = floorService;
        this.sectionService = sectionService;
        this.flatRepository = flatRepository;
        this.flatMapper = flatMapper;
        this.messagingTemplate = messagingTemplate;
        this.messageMapper = messageMapper;
    }

    @GetMapping("index/{page}")
    public ModelAndView getMessages(
            @PathVariable("page") int page,
            @RequestParam(name = "search", required = false) String searchQuery,
            @RequestParam(name = "sortField", defaultValue = "recipientsName") String sortField,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {

        int pageSize = 10;
        Page<MessageDTO> messagesList = messageService.getPage(page - 1, pageSize, searchQuery, sortField, sortOrder);

        ModelAndView modelAndView = new ModelAndView("admin/message/index");
        modelAndView.addObject("messages", messagesList.getContent());
        modelAndView.addObject("totalPagesCount", messagesList.getTotalPages());
        modelAndView.addObject("searchQuery", searchQuery);
        modelAndView.addObject("messageCount", messagesList.getTotalElements());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("currentSortField", sortField);
        modelAndView.addObject("currentSortOrder", sortOrder);

        return modelAndView;
    }

    @GetMapping("{id}")
    public ModelAndView getMessage(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("admin/message/card");
        modelAndView.addObject("messageId", id);
        return modelAndView;
    }

    @GetMapping("new")
    public ModelAndView createMessagePage() {
        ModelAndView modelAndView = new ModelAndView("admin/message/add");
        Message message = new Message();
        modelAndView.addObject("message", message);
        modelAndView.addObject("houses", houseMapper.toDtoForViewList(houseService.getAll()));
        return modelAndView;
    }

    @GetMapping("api")
    public ResponseEntity<?> getMessagesApi(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "searchQuery", required = false) String searchQuery,
            @RequestParam(name = "sortField", defaultValue = "recipientsName") String sortField,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {
        System.out.println(searchQuery);
        int pageSize = 10;
        Page<MessageDTO> messagesPage = messageService.getPage(page - 1, pageSize, searchQuery, sortField, sortOrder);
        List<MessageDTO> messagesList = messagesPage.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("messages", messagesList);
        response.put("totalPagesCount", messagesPage.getTotalPages());
        response.put("messageCount", messagesPage.getTotalElements());
        return ResponseEntity.ok(response);
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
                for (Flat flat : flatRepository.findByScoreBalanceLessThanAndHouseId(0D, houseId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseService.getById(houseId).getName() + " c задолженостями");
            } else if (floorId == 0L && sectionId > 0L && houseId > 0L && !balance) {
                for (Flat flat : flatRepository.findByHouseIdAndSectionId(houseId, sectionId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseService.getById(houseId).getName() + ", " + sectionService.getById(sectionId).getName());
            } else if (floorId == 0L && sectionId > 0L && houseId > 0L && balance) {
                for (Flat flat : flatRepository.findByScoreBalanceLessThanAndHouseIdAndSectionId(0d, houseId, sectionId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseService.getById(houseId).getName() + ", " + sectionService.getById(sectionId).getName() + " c задолженостями");
            } else if (floorId > 0L && sectionId == 0L && houseId > 0L && !balance) {
                for (Flat flat : flatRepository.findByHouseIdAndFloorId(houseId, floorId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseService.getById(houseId).getName() + ", " + floorService.getById(floorId).getName());
            } else if (floorId > 0L && sectionId == 0L && houseId > 0L && balance) {
                for (Flat flat : flatRepository.findByScoreBalanceLessThanAndHouseIdAndFloorId(0d, houseId, floorId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseService.getById(houseId).getName() + ", " + floorService.getById(floorId).getName() + " c задолженостями");
            } else if (floorId > 0L && sectionId > 0L && houseId > 0L && !balance) {
                for (Flat flat : flatRepository.findByHouseIdAndSectionIdAndFloorId(houseId, sectionId, floorId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseService.getById(houseId).getName() + ", " + sectionService.getById(sectionId).getName() + ", " + floorService.getById(floorId).getName());
            } else if (floorId > 0L && sectionId > 0L && houseId > 0L && balance) {
                for (Flat flat : flatRepository.findByScoreBalanceLessThanAndHouseIdAndSectionIdAndFloorId(0d, houseId, sectionId, floorId)) {
                    recipients.add(flat.getUser());
                }
                message.setRecipientsName(houseService.getById(houseId).getName() + ", " + sectionService.getById(sectionId).getName() + ", " + floorService.getById(floorId).getName() + " c задолженостями");
            }
        } else {
            Flat flat = flatService.getById(flatId);
            recipients.add(flat.getUser());
            message.setRecipientsName(flat.getHouse().getName() + ", " + flat.getSection() + ", " + flat.getFloor() + ", кв." + flat.getNumber());
        }
        message.setRecipients(recipients);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Admin admin = adminRepository.findByEmail(email);
        message.setSender(admin);
        messageService.save(message);
        MessageDTO messageDto = messageMapper.toDtoForView(message);
        messagingTemplate.convertAndSend("/topic/new-message", messageDto);
        return "redirect:/admin/messages/index/1";
    }

    @GetMapping("getHouseInfo")
    @ResponseBody
    public HouseInfoResponse getHouseInfo(
            @RequestParam Long houseId,
            @RequestParam(required = false) Boolean hasDebt) {
        log.info("executing getHouseInfo with houseID: " + houseId);

        House house = houseService.getById(houseId);
        if (house != null) {
            List<Section> sections = new ArrayList<>();
            for (Section houseSection : house.getSections()) {
                sections.add(new Section(houseSection.getId(), houseSection.getName()));
            }
            log.info("House sections: " + sections);

            List<Floor> floors = new ArrayList<>();
            for (Floor houseFloor : house.getFloors()) {
                floors.add(new Floor(houseFloor.getId(), houseFloor.getName()));
            }
            log.info("House floors: " + floors);

            List<Flat> flats;
            if (hasDebt != null && hasDebt) {
                flats = flatRepository.findByScoreBalanceLessThanAndHouseId(0.0, houseId);
            } else {
                flats = flatRepository.findFlatsByHouseId(houseId);
            }

            return new HouseInfoResponse(sections, floors, flats);
        }
        return new HouseInfoResponse(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    @GetMapping("getFilteredFlats")
    @ResponseBody
    public List<FlatForMessageDTO> getFilteredFlats(
            @RequestParam(required = false) Boolean hasDebt,
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
    @GetMapping("{id}/getMessage")
    @ResponseBody
    public ResponseEntity<MessageDTO> getMessageJson(@PathVariable("id") Long id) {
        MessageDTO messageDTO = messageMapper.toDtoForView(messageService.findById(id));
        return ResponseEntity.ok(messageDTO);
    }
    @GetMapping("delete/{id}")
    public String deleteMessage(@PathVariable("id") Long id){
        messageService.deleteById(id);
        return "redirect:/admin/messages/index/1";
    }
    public static class HouseInfoResponse {
        private List<Section> sections;
        private List<Floor> floors;
        private List<Flat> flats;

        public HouseInfoResponse(List<Section> sections, List<Floor> floors, List<Flat> flats) {
            this.sections = sections;
            this.floors = floors;
            this.flats = flats;
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

        public List<Flat> getFlats() {
            return flats;
        }

        public void setFlats(List<Flat> flats) {
            this.flats = flats;
        }
    }
}
