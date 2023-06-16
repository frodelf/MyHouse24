package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.model.UserForAddDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final UserServiceImpl userService;
    public UserForViewDTO toDtoForView(User user){
        UserForViewDTO userForViewDTO = new UserForViewDTO();
        if(user.getId() != null)userForViewDTO.setId(IdUtil.fromIdToString(user.getId()));
        userForViewDTO.setFullName(user.getLastName() == null? "":user.getLastName() +" "+user.getFirstName()==null?"":user.getFirstName()+" "+user.getFathersName()==null?"":user.getFathersName());
        userForViewDTO.setPhone(user.getPhone());
        userForViewDTO.setEmail(user.getEmail());
        if(user.getFlats() != null)
            userForViewDTO.setHouses(getHouseNamesFromFlat(user.getFlats()));
        if(user.getFlats() != null)
            userForViewDTO.setFlats(getHouseNamesFromFlat(user.getFlats()));
        userForViewDTO.setDate(user.getFromDate());
        userForViewDTO.setStatus(user.getStatus()==null?"":String.valueOf(user.getStatus()));
        userForViewDTO.setIsDebt(userService.isDebt(user));
        return userForViewDTO;
    }

    public User toEntityForAdd(UserForAddDTO userForAddDTO){
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setId(IdUtil.fromStringToId(userForAddDTO.getId()));
        user.setFirstName(userForAddDTO.getFirstName());
        user.setLastName(userForAddDTO.getLastName());
        user.setFathersName(userForAddDTO.getFathersName());
        user.setBirthday(Date.valueOf(userForAddDTO.getBirthday()));
        user.setPhone(userForAddDTO.getPhone());
        user.setViber(userForAddDTO.getViber());
        user.setTelegram(userForAddDTO.getTelegram());
        user.setEmail(userForAddDTO.getEmail());
        user.setPassword(encoder.encode(userForAddDTO.getPassword()));
        user.setStatus(UserStatus.valueOf(userForAddDTO.getStatus()));
        user.setDescription(userForAddDTO.getDescription());
        user.setImage(userForAddDTO.getImageName());
        return user;
    }
    public UserForAddDTO toDtoForAdd(User user){
        UserForAddDTO userForAddDTO = new UserForAddDTO();
        userForAddDTO.setId(IdUtil.fromIdToString(user.getId()));
        userForAddDTO.setFirstName(user.getFirstName());
        userForAddDTO.setLastName(user.getLastName());
        userForAddDTO.setFathersName(user.getFathersName());
        if(user.getBirthday() != null)userForAddDTO.setBirthday(user.getBirthday().toLocalDate());
        userForAddDTO.setPhone(user.getPhone());
        userForAddDTO.setViber(user.getViber());
        userForAddDTO.setTelegram(user.getTelegram());
        userForAddDTO.setEmail(user.getEmail());
        userForAddDTO.setStatus(user.getStatus().toString());
        userForAddDTO.setDescription(user.getDescription());
        userForAddDTO.setImageName(user.getImage());

        return userForAddDTO;
    }

    public List<UserForViewDTO> toDtoListForView(List<User> users){
        List<UserForViewDTO> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(toDtoForView(user));
        }
        return usersDto;
    }

    private List<String> getHouseNamesFromFlat(List<Flat> flats){
        ArrayList<String> housesName = new ArrayList<>();
        for (Flat flat : flats) {
            housesName.add(flat.getHouse().getName());
        }
        return housesName;
    }
    private List<String> getFlatNames(List<Flat> flats){
        ArrayList<String> flatNames = new ArrayList<>();
        for (Flat flat : flats) {
            flatNames.add(flat.getNumber()+", "+flat.getHouse().getName());
        }
        return flatNames;
    }
}
