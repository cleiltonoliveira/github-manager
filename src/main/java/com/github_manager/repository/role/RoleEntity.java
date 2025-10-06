package com.github_manager.repository.role;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
