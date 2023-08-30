package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register() {
        User mockUser = new User();
        mockUser.setFirstName("John");
        mockUser.setPassword("password");

        when(passwordEncoder.encode(mockUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        User result = userService.register(mockUser);

        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void getByFirstName() {
        String mockName = "Alice";
        User mockUser = new User();
        mockUser.setFirstName(mockName);

        when(userRepository.findByFirstName(mockName)).thenReturn(Optional.of(mockUser));

        User result = userService.getByFirstName(mockName);

        assertEquals(mockName, result.getFirstName());
    }

    @Test
    void getById() {
        long mockId = 1L;
        User mockUser = new User();
        when(userRepository.findById(mockId)).thenReturn(Optional.of(mockUser));

        User result = userService.getById(mockId);

        assertNotNull(result);
    }

    @Test
    void getAll() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User());
        mockUsers.add(new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void getOnlyName() {
        List<User> mockUsers = new ArrayList<>();
        User user1 = new User();
        user1.setFirstName("John");
        User user2 = new User();
        user2.setFirstName("Jane");
        mockUsers.add(user1);
        mockUsers.add(user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<String> result = userService.getOnlyName();

        assertEquals(2, result.size());
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Jane"));
    }

    @Test
    void getPage() {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            users.add(new User());
        }
        when(userRepository.findAll()).thenReturn(users);
        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, users.size());

        List<User> currentPageItems = users.subList(startIndex, endIndex);

        Page<User> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), users.size());

        Model model = new ExtendedModelMap();
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);
        Page<User> result = userService.getPage(1, model);

        assertEquals(mockPage, result);
        assertEquals(1, model.getAttribute("max"));
        assertEquals(1, model.getAttribute("current"));
        verify(userRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetPage() {
        List<UserForViewDTO> users = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            users.add(new UserForViewDTO());
        }
        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, users.size());

        List<UserForViewDTO> currentPageItems = users.subList(startIndex, endIndex);

        Page<UserForViewDTO> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), users.size());

        Model model = new ExtendedModelMap();
        Page<UserForViewDTO> result = userService.getPage(1, model, users);

        assertEquals(mockPage, result);
        assertEquals(1, model.getAttribute("max"));
        assertEquals(1, model.getAttribute("current"));
    }

    @Test
    void isDebt() {
        User user = new User();
        Flat flat = new Flat();
        Score score = new Score();
        score.setBalance(-100.0);
        flat.setScore(score);
        List<Flat> flats = new ArrayList<>();
        flats.add(flat);
        user.setFlats(flats);

        boolean result = userService.isDebt(user);
        assertTrue(result);

        user.getFlats().get(0).getScore().setBalance(100);
        boolean result1 = userService.isDebt(user);
        assertFalse(result1);

    }

    @Test
    void findUserByEmail() {
        String mockEmail1 = "test@example.com";
        User mockUser = new User();
        when(userRepository.findByEmail(mockEmail1)).thenReturn(Optional.of(mockUser));

        User result1 = userService.findUserByEmail(mockEmail1);

        assertNotNull(result1);

        String mockEmail2 = "nonexistent@example.com";
        when(userRepository.findByEmail(mockEmail2)).thenReturn(Optional.empty());

        User result2 = userService.findUserByEmail(mockEmail2);

        assertNull(result2);
    }

    @Test
    void verifyPassword() {
        String mockPassword = "testPassword";
        String mockEncodedPassword = "$2a$10$abcdefghijklmnopqrstuvwxyz1234567890"; // Приклад хешованого пароля
        User mockUser = new User();
        mockUser.setPassword(mockEncodedPassword);

        when(passwordEncoder.matches(mockPassword, mockEncodedPassword)).thenReturn(true);

        boolean result = userService.verifyPassword(mockUser, mockPassword);

        assertTrue(result);
    }

    @Test
    void save() {
        User userToSave = new User();
        userToSave.setFirstName("John");
        userToSave.setPassword("password");

        userService.save(userToSave);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void existsById() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.existsById(1L);

        assertTrue(result);

        when(userRepository.existsById(1L)).thenReturn(false);

        boolean result1 = userService.existsById(1L);

        assertFalse(result1);
    }

    @Test
    void getMaxId() {
        when(userRepository.findMaxId()).thenReturn(100L);
        long result = userService.getMaxId();
        assertEquals(101L, result);

        when(userRepository.findMaxId()).thenThrow(new RuntimeException());
        long resultWithError = userService.getMaxId();
        assertEquals(1, resultWithError);
    }

    @Test
    void deleteById() {
        userService.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void forSelect() {
        Page<User> mockPage = new PageImpl<>(new ArrayList<>());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        List<User> result = userService.forSelect(1, 10, null);
        assertEquals(result, new ArrayList<>());

        when(userRepository.findByFirstNameContainingIgnoreCase(anyString(), any())).thenReturn(mockPage);
        List<User> result1 = userService.forSelect(1, 10, "qwerty");
        assertEquals(result1, new ArrayList<>());
    }

    @Test
    void filter() {
        UserForViewDTO userForViewDTO = new UserForViewDTO();
        userForViewDTO.setId("1");
        userForViewDTO.setFullName("1");
        userForViewDTO.setPhone("1");
        userForViewDTO.setEmail("1");
        userForViewDTO.setDate(new Date(1,1,1));
        userForViewDTO.setStatus("1");
        userForViewDTO.setIsDebt(true);

        List<UserForViewDTO> userForViewDTOList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserForViewDTO user = new UserForViewDTO();
            user.setId(String.valueOf(i));
            user.setFullName(String.valueOf(i));
            user.setPhone("1");
            user.setEmail("1");
            user.setDate(new Date(1,1,1));
            user.setStatus("1");
            user.setIsDebt(true);
            user.setDate(new Date(1,1,1));
            user.setHouses(Arrays.asList("house"));
            user.setFlats(Arrays.asList("1"));
            userForViewDTOList.add(user);
        }

        List<UserForViewDTO> result = userService.filter(userForViewDTO, userForViewDTOList, new Date(1,1,1), "1", "house");

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }
}