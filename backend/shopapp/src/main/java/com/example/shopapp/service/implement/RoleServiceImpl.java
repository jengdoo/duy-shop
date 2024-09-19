package com.example.shopapp.service.implement;

import com.example.shopapp.Model.Role;
import com.example.shopapp.repositories.RoleRepo;
import com.example.shopapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
}
