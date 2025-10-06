package com.github_manager.service.user;

import com.github_manager.model.RoleModel;
import com.github_manager.model.UserModel;
import com.github_manager.repository.role.RoleGateway;
import com.github_manager.repository.user.UserGateway;
import com.github_manager.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private RoleGateway roleGateway;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<UserModel> userModelCaptor;

    private UserModel userModel;
    private RoleModel roleModel;

    @BeforeEach
    void setUp() {
        userModel = new UserModel();
        userModel.setId(1L);
        userModel.setLogin("testuser");
        userModel.setUrl("https://api.github.com/users/testuser");
        userModel.setRoles(new HashSet<>());

        roleModel = new RoleModel();
        roleModel.setId(1L);
        roleModel.setName("ADMIN");
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        List<UserModel> expectedUsers = Collections.singletonList(userModel);
        when(userGateway.findAll()).thenReturn(expectedUsers);

        List<UserModel> actualUsers = userService.findAll();

        assertEquals(expectedUsers, actualUsers);
        verify(userGateway).findAll();
    }

    @Test
    void assignRoleToUser_shouldAssignRoleSuccessfully() {
        when(userGateway.findById(1L)).thenReturn(Optional.of(userModel));
        when(roleGateway.findById(1L)).thenReturn(Optional.of(roleModel));

        userService.assignRoleToUser(1L, 1L);

        verify(userGateway).findById(1L);
        verify(roleGateway).findById(1L);
        verify(userGateway).save(userModelCaptor.capture());

        UserModel savedUser = userModelCaptor.getValue();
        assertTrue(savedUser.getRoles().contains(roleModel));
    }

    @Test
    void assignRoleToUser_shouldThrowExceptionWhenUserNotFound() {
        when(userGateway.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.assignRoleToUser(999L, 1L));

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userGateway).findById(999L);
        verify(roleGateway, never()).findById(any());
        verify(userGateway, never()).save(any());
    }

    @Test
    void assignRoleToUser_shouldThrowExceptionWhenRoleNotFound() {
        when(userGateway.findById(1L)).thenReturn(Optional.of(userModel));
        when(roleGateway.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.assignRoleToUser(1L, 999L));

        assertEquals("Perfil não encontrado", exception.getMessage());
        verify(userGateway).findById(1L);
        verify(roleGateway).findById(999L);
        verify(userGateway, never()).save(any());
    }

    @Test
    void syncGitHubUsers_shouldSyncUsersFromGitHub() {
        List<Map<String, Object>> githubUsers = new ArrayList<>();
        Map<String, Object> githubUser = new HashMap<>();
        githubUser.put("login", "newuser");
        githubUser.put("url", "https://api.github.com/users/newuser");
        githubUsers.add(githubUser);

        when(restClient.get()
                .uri("/users")
                .retrieve()
                .body(List.class))
                .thenReturn(githubUsers);

        when(userGateway.existsByLogin("newuser")).thenReturn(false);

        userService.syncGitHubUsers();

        verify(userGateway).existsByLogin("newuser");
        verify(userGateway).save(userModelCaptor.capture());

        UserModel savedUser = userModelCaptor.getValue();
        assertEquals("newuser", savedUser.getLogin());
        assertEquals("https://api.github.com/users/newuser", savedUser.getUrl());
    }

    @Test
    void syncGitHubUsers_shouldNotSaveExistingUsers() {
        List<Map<String, Object>> githubUsers = new ArrayList<>();
        Map<String, Object> githubUser = new HashMap<>();
        githubUser.put("login", "existinguser");
        githubUser.put("url", "https://api.github.com/users/existinguser");
        githubUsers.add(githubUser);

        when(restClient.get()
                .uri("/users")
                .retrieve()
                .body(List.class))
                .thenReturn(githubUsers);

        when(userGateway.existsByLogin("existinguser")).thenReturn(true);

        userService.syncGitHubUsers();

        verify(userGateway).existsByLogin("existinguser");
        verify(userGateway, never()).save(any());
    }

    @Test
    void syncGitHubUsers_shouldHandleNullResponse() {
        when(restClient.get()
                .uri("/users")
                .retrieve()
                .body(List.class))
                .thenReturn(null);

        userService.syncGitHubUsers();

        verify(userGateway, never()).existsByLogin(anyString());
        verify(userGateway, never()).save(any());
    }
}
