package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.mapper.UserMapper;
import com.avada.myHouse24.model.UserForAddDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.RoleServiceImpl;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.services.registration.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private RoleServiceImpl roleService;

    @MockBean
    private HouseServiceImpl houseService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private EmailService emailService;

    @MockBean
    private FlatMapper flatMapper;

    @Test
    void getAll() throws Exception {
        UserForViewDTO user = new UserForViewDTO();
        user.setStatus("");
        when(userService.getPage(anyInt(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(userMapper.toDtoForView(any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/index/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/get-all"))
                .andExpect(model().attributeExists("filter", "users", "userCount", "houses", "allStatus"));

        verify(userService, times(1)).getPage(eq(1), any());
    }

    @Test
    void add() throws Exception {
        when(userService.getMaxId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/add"))
                .andExpect(model().attributeExists("maxId", "status"));
    }

    @Test
    void testAdd() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());

        UserForAddDTO user = new UserForAddDTO();
        user.setPassword("1234");
        user.setPasswordAgain("1111");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/user/add")
                        .file(image)
                        .with(csrf())
                        .flashAttr("userDTO", user))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/add"))
                .andExpect(model().attributeExists("status", "passwordAgainError"));

        user.setId("1");
        user.setFirstName("first name");
        user.setLastName("last name");
        user.setFathersName("fathers name");
        user.setBirthday(LocalDate.now());
        user.setPhone("098765432");
        user.setViber("viber");
        user.setTelegram("telegram");
        user.setEmail("email@gmail.com");
        user.setStatus("status");
        user.setDescription("description");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/user/add")
                        .file(image)
                        .with(csrf())
                        .flashAttr("userDTO", user))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/add"))
                .andExpect(model().attributeExists("status", "passwordAgainError"));

        user.setPasswordAgain("1234");
        when(userMapper.toEntityForAdd(any())).thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/user/add")
                        .file(image)
                        .with(csrf())
                        .flashAttr("userDTO", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/index/1"));
    }

    @Test
    void edit() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userService.getById(anyLong())).thenReturn(user);
        UserForAddDTO userForAddDTO = new UserForAddDTO();
        userForAddDTO.setId("1");
        when(userMapper.toDtoForAdd(any())).thenReturn(userForAddDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/edit"))
                .andExpect(model().attributeExists("user", "status"));

        verify(userService, times(1)).getById(anyLong());
    }

    @Test
    void testEdit() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());

        User user = new User();
        user.setId(1L);
        when(userService.getById(anyLong())).thenReturn(user);
        UserForAddDTO userForAddDTO = new UserForAddDTO();
        userForAddDTO.setPassword("1234");
        userForAddDTO.setPasswordAgain("1111");
        userForAddDTO.setId("1");
        when(userMapper.toDtoForAdd(any())).thenReturn(userForAddDTO);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/user/edit/1")
                        .file(image)
                        .with(csrf())
                        .flashAttr("userDTO", userForAddDTO))
                .andExpect(status().isOk());

        userForAddDTO.setId("1");
        userForAddDTO.setFirstName("first name");
        userForAddDTO.setLastName("last name");
        userForAddDTO.setFathersName("fathers name");
        userForAddDTO.setBirthday(LocalDate.now());
        userForAddDTO.setPhone("098765432");
        userForAddDTO.setViber("viber");
        userForAddDTO.setTelegram("telegram");
        userForAddDTO.setEmail("email@gmail.com");
        userForAddDTO.setStatus("status");
        userForAddDTO.setDescription("description");

        User user1 = new User();
        user1.setPassword("1234");
        userForAddDTO.setPassword("1234");

        when(userMapper.toEntityForAdd(any())).thenReturn(user1);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/user/edit/1")
                        .file(image)
                        .with(csrf())
                        .flashAttr("userDTO", userForAddDTO))
                .andExpect(status().isOk());

        user1.setPassword("");
        when(userMapper.toEntityForAdd(any())).thenReturn(user1);
        user1.setImage("image");
        when(userService.getById(anyLong())).thenReturn(user1);
        userForAddDTO.setPassword("");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/user/edit/1")
                        .file(image)
                        .with(csrf())
                        .flashAttr("userDTO", userForAddDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/index/1"));

        userForAddDTO.setPassword("1111");
        image = new MockMultipartFile("image", "", "image/jpeg", "test image".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/user/edit/1")
                        .file(image)
                        .with(csrf())
                        .flashAttr("userDTO", userForAddDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/index/1"));
    }

    @Test
    void index() throws Exception {
        User user = new User();
        when(userService.getById(anyLong())).thenReturn(user);
        UserForAddDTO userForAddDTO = new UserForAddDTO();
        userForAddDTO.setId("1");
        when(userMapper.toDtoForAdd(any())).thenReturn(userForAddDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/index"))
                .andExpect(model().attributeExists("user", "flats"));
        verify(userService, times(2)).getById(anyLong());
    }

    @Test
    void delete() throws Exception {
        User user = new User();
        when(userService.getById(anyLong())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/index/1"));

        verify(userService, times(1)).getById(anyLong());
    }

    @Test
    void inviteMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/invite"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/invite"));
    }

    @Test
    void testInviteMessage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/invite")
                        .with(csrf())
                        .param("email", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/index/1"));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/invite")
                        .with(csrf())
                        .param("email", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/invite"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void filter() throws Exception {
        UserForViewDTO userForViewDTO = new UserForViewDTO();
        userForViewDTO.setStatus("");
        House house = new House();
        house.setFlats(new ArrayList<>());
        when(houseService.getByName(anyString())).thenReturn(house);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/filter/1")
                        .flashAttr("userForViewDTO", userForViewDTO)
                        .param("house", "HouseName")
                        .flashAttr("userForViewDTO", userForViewDTO)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/get-all"))
                .andExpect(model().attributeExists("filter"));
    }

    @Test
    void getAllUsers() throws Exception {
        int page = 1;
        String search = "searchTerm";
        User user = new User();
        user.setId(1L);
        when(userService.forSelect(anyInt(), anyInt(), anyString())).thenReturn(Arrays.asList(user));
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/get-users")
                        .param("_page", String.valueOf(page))
                        .param("_search", search)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].id", is(1)));

        verify(userService, times(1)).forSelect(eq(page), anyInt(), eq(search));
    }
}