package com.github_manager.service.role;

import com.github_manager.model.RoleModel;
import com.github_manager.repository.role.RoleGateway;
import com.github_manager.service.exception.ResourceConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleGateway roleGateway;

    @InjectMocks
    private RoleService roleService;

    private RoleModel roleModel;

    @BeforeEach
    void setUp() {
        roleModel = new RoleModel();
        roleModel.setId(1L);
        roleModel.setName("ADMIN");
    }

    @Test
    void saveRole_shouldSaveRoleWhenItDoesNotExist() {
        when(roleGateway.existsByName("ADMIN")).thenReturn(false);
        when(roleGateway.save(roleModel)).thenReturn(roleModel);

        RoleModel savedRole = roleService.saveRole(roleModel);

        assertNotNull(savedRole);
        assertEquals("ADMIN", savedRole.getName());
        verify(roleGateway).existsByName("ADMIN");
        verify(roleGateway).save(roleModel);
    }

    @Test
    void saveRole_shouldThrowExceptionWhenRoleAlreadyExists() {
        when(roleGateway.existsByName("ADMIN")).thenReturn(true);

        ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> {
            roleService.saveRole(roleModel);
        });

        assertEquals("Role with name ADMIN already exists.", exception.getMessage());
        verify(roleGateway).existsByName("ADMIN");
        verify(roleGateway, never()).save(any());
    }
}
