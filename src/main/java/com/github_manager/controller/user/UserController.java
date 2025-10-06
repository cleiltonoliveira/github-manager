package com.github_manager.controller.user;

import com.github_manager.service.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private ModelMapper modelMapper;

    @PostMapping("sync")
    public ResponseEntity<?> sync() {
        userService.syncGitHubUsers();
        return ResponseEntity.ok(Map.of("message", "Usuários sincronizados com sucesso!"));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        var users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<?> assignRoleToUser(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(Map.of(
                "message", "Perfil atribuído com sucesso",
                "userId", userId,
                "roleId", roleId
        ));
    }


}
