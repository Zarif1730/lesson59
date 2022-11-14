package com.example.lesson59.service.impl;

import com.example.lesson59.entity.Role;
import com.example.lesson59.exeption.ResourceNotFound;
import com.example.lesson59.model.Result;
import com.example.lesson59.repository.RoleRepository;
import com.example.lesson59.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
//    @Override
//    public Role findById(Long id) {
//        return null;
//    }

    @Override
    public List<Role> getRoles(List<Long> rolesId) {
        return rolesId.stream()
                .map(this::findRoleById)
                .collect(Collectors.toList());
    }

    public Role findRoleById(Long id){
        return roleRepository.findById(id).orElseThrow(()->new ResourceNotFound("role","id",id));
    }

    public Result getRole(Long id){
        try {
            Role role=roleRepository.findById(id).orElseThrow(()->new ResourceNotFound("role","id",id));
            return new Result("success",true,role,null);
        }catch (Exception e){
            log.error(e.getMessage());
            return new Result("fail",false,null,e.getMessage());
        }
    }
}
