package com.example.lesson59.service;

import com.example.lesson59.entity.Role;

import java.util.List;

public interface RoleService {
//    Role findById(Long id);
    List<Role> getRoles(List<Long> rolesId);
}
