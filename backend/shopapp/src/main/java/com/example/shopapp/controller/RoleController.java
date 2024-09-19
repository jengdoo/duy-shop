package com.example.shopapp.controller;

import com.example.shopapp.Model.Role;
import com.example.shopapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRole(){
        List<Role> roleList = roleService.getAllRoles();
        return ResponseEntity.ok(roleList);
    }
}
