package com.github_manager.service.user;

import com.github_manager.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserAdapter {

    UserModel save(UserModel userAccount);

    boolean existsByLogin(String login);

    List<UserModel> findAll();

    Optional<UserModel> findById(Long userId);
}
