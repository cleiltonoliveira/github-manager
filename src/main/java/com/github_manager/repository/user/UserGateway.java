package com.github_manager.repository.user;

import com.github_manager.model.UserModel;
import com.github_manager.service.user.UserAdapter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@AllArgsConstructor
public class UserGateway implements UserAdapter {
    private UserRepository repository;

    private ModelMapper modelMapper;

    @Override
    public UserModel save(UserModel userModel) {
        var savedEntity = repository.save(toEntity(userModel));
        return toModel(savedEntity);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

    @Override
    public List<UserModel> findAll() {
        return repository.findAll().stream().map(this::toModel).toList();
    }

    @Override
    public Optional<UserModel> findById(Long userId) {
        return repository.findById(userId).map(this::toModel);
    }

    private UserModel toModel(UserEntity entity) {
        return modelMapper.map(entity, UserModel.class);
    }

    private UserEntity toEntity(UserModel domain) {
        return modelMapper.map(domain, UserEntity.class);
    }
}