package com.github_manager.service.role;


import com.github_manager.model.RoleModel;

import java.util.Optional;

public interface RoleAdapter {

    RoleModel save(RoleModel role);

    Optional<RoleModel> findById(Long roleId);

    boolean existsByName(String name);
}
