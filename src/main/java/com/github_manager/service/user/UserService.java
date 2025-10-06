package com.github_manager.service.user;

import com.github_manager.model.UserModel;
import com.github_manager.repository.role.RoleGateway;
import com.github_manager.repository.user.UserGateway;
import com.github_manager.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserGateway userGateway;
    private final RoleGateway roleGateway;
    private final RestClient restClient;


    public UserService(UserGateway userGateway, RoleGateway roleGateway, RestClient githubRestClient) {
        this.userGateway = userGateway;
        this.roleGateway = roleGateway;
        this.restClient = githubRestClient;
    }


    public void syncGitHubUsers() {
        List<Map<String, Object>> users = restClient.get()
                .uri("/users")
                .retrieve()
                .body(List.class);

        if (users == null) return;

        users.stream()
                .limit(30)
                .forEach(u -> {
                    String login = (String) u.get("login");
                    String url = (String) u.get("url");

                    if (!userGateway.existsByLogin(login)) {
                        var userModel = new UserModel();
                        userModel.setLogin(login);
                        userModel.setUrl(url);
                        userGateway.save(userModel);
                    }
                });
    }

    public List<UserModel> findAll() {
        return userGateway.findAll();
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        var user = userGateway.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        var role = roleGateway.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        user.getRoles().add(role);
        userGateway.save(user);
    }
}
