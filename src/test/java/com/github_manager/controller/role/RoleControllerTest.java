package com.github_manager.controller.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github_manager.controller.role.dto.RoleCreationRequest;
import com.github_manager.model.RoleModel;
import com.github_manager.service.exception.ResourceConflictException;
import com.github_manager.service.role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @MockitoBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private RoleModel roleModel;
    private RoleCreationRequest roleCreationRequest;

    @BeforeEach
    void setUp() {
        roleCreationRequest = new RoleCreationRequest();
        roleCreationRequest.setName("ADMIN");

        roleModel = new RoleModel();
        roleModel.setId(1L);
        roleModel.setName("ADMIN");

        when(modelMapper.map(any(RoleCreationRequest.class), any())).thenReturn(roleModel);
    }

    @Test
    void save_shouldReturnCreatedStatusAndSavedRole() throws Exception {
        when(roleService.saveRole(any(RoleModel.class))).thenReturn(roleModel);

        mockMvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    void save_shouldReturnConflictWhenRoleAlreadyExists() throws Exception {
        when(roleService.saveRole(any(RoleModel.class)))
                .thenThrow(new ResourceConflictException("Role with name ADMIN already exists."));

        mockMvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Role with name ADMIN already exists."));
    }

    @Test
    void save_shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        roleCreationRequest.setName("");

        mockMvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isBadRequest());
    }
}
