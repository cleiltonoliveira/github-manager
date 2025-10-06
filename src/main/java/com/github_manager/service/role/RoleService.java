package com.github_manager.service.role;

import com.github_manager.model.RoleModel;
import com.github_manager.repository.role.RoleGateway;
import com.github_manager.service.exception.ResourceConflictException;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleGateway roleGateway;

    public RoleService(RoleGateway roleGateway) {
        this.roleGateway = roleGateway;
    }

    public RoleModel saveRole(RoleModel roleModel) {
        var exists = roleGateway.existsByName(roleModel.getName());

        if (exists) {
            throw new ResourceConflictException("Role with name " + roleModel.getName() + " already exists.");
        }
        return roleGateway.save(roleModel);
    }
}
