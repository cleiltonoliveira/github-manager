package com.github_manager.controller.user;

import com.github_manager.model.RoleModel;
import com.github_manager.model.UserModel;
import com.github_manager.service.exception.ResourceNotFoundException;
import com.github_manager.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ModelMapper modelMapper;

    private List<UserModel> users;

    @BeforeEach
    void setUp() {
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setLogin("testuser");
        userModel.setUrl("https://api.github.com/users/testuser");
        userModel.setRoles(new HashSet<>());

        RoleModel roleModel = new RoleModel();
        roleModel.setId(1L);
        roleModel.setName("ADMIN");

        users = Collections.singletonList(userModel);
    }

    @Test
    void sync_shouldReturnOkStatus() throws Exception {
        doNothing().when(userService).syncGitHubUsers();

        mockMvc.perform(post("/api/v1/users/sync")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuários sincronizados com sucesso!"));

        verify(userService, times(1)).syncGitHubUsers();
    }

    @Test
    void getAllUsers_shouldReturnOkStatusAndUserList() throws Exception {
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].login").value("testuser"))
                .andExpect(jsonPath("$[0].url").value("https://api.github.com/users/testuser"));

        verify(userService, times(1)).findAll();
    }

    @Test
    void assignRoleToUser_shouldReturnOkStatus() throws Exception {
        doNothing().when(userService).assignRoleToUser(anyLong(), anyLong());

        mockMvc.perform(post("/api/v1/users/1/roles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Perfil atribuído com sucesso"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.roleId").value(1));

        verify(userService, times(1)).assignRoleToUser(1L, 1L);
    }

    @Test
    void assignRoleToUser_shouldReturnNotFoundWhenUserNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Usuário não encontrado"))
                .when(userService).assignRoleToUser(999L, 1L);

        mockMvc.perform(post("/api/v1/users/999/roles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));

        verify(userService, times(1)).assignRoleToUser(999L, 1L);
    }

    @Test
    void assignRoleToUser_shouldReturnNotFoundWhenRoleNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Perfil não encontrado"))
                .when(userService).assignRoleToUser(1L, 999L);

        mockMvc.perform(post("/api/v1/users/1/roles/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Perfil não encontrado"));

        verify(userService, times(1)).assignRoleToUser(1L, 999L);
    }
}
