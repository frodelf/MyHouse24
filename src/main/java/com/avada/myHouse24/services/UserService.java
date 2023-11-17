package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.model.UserForViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.sql.Date;
import java.util.List;

public interface UserService {

    User register(User user);
    User getByFirstName(String name);
    User getById(long id);
    List<User> getAll();
    List<String> getOnlyName();
    boolean isDebt(User user);
    User findUserByEmail(String email);
    boolean verifyPassword(User user, String password);
    void save(User user);
    boolean existsById(long id);
    long getMaxId();
    void deleteById(long id);
    Page<User> getPage(int pageNumber, Model model);
    Page<UserForViewDTO> getPage(int pageNumber, Model model, List<UserForViewDTO> userList);
    List<User> forSelect(int page, int pageSize, String search);
    List<UserForViewDTO> filter(UserForViewDTO userForViewDTO, List<UserForViewDTO> users, Date date, String flat, String house);
}
