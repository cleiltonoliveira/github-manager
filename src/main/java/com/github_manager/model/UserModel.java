package com.github_manager.model;

import lombok.Data;

import java.util.Set;

@Data
public class UserModel {
    private Long id;
    private String login;
    private String url;
    private Set<RoleModel> roles;
}

