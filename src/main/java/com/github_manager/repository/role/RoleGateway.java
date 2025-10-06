package com.github_manager.repository.role;

import com.github_manager.model.RoleModel;
import com.github_manager.service.role.RoleAdapter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@AllArgsConstructor
public class RoleGateway implements RoleAdapter {
    private RoleRepository repository;
    private ModelMapper modelMapper;

    @Override
    public RoleModel save(RoleModel roleModel) {
        var savedEntity = repository.save(toEntity(roleModel));
        return toModel(savedEntity);
    }

    @Override
    public Optional<RoleModel> findById(Long roleId) {
        return repository.findById(roleId).map(this::toModel);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    private RoleModel toModel(RoleEntity entity) {
        return modelMapper.map(entity, RoleModel.class);
    }

    private RoleEntity toEntity(RoleModel model) {
        return modelMapper.map(model, RoleEntity.class);
    }

}