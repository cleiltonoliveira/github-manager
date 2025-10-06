package com.github_manager.controller.role;

import com.github_manager.controller.role.dto.RoleCreationRequest;
import com.github_manager.model.RoleModel;
import com.github_manager.service.role.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;
    private ModelMapper modelMapper;

    @PostMapping("roles")
    public ResponseEntity<?> save(@RequestBody @Valid RoleCreationRequest roleCreationRequest) {
        var model= toModel(roleCreationRequest);
        var savedRole= roleService.saveRole(model);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    private RoleModel toModel(RoleCreationRequest request) {
        return modelMapper.map(request, RoleModel.class);
    }
}
