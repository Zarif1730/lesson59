package com.example.lesson59.service.impl;

import com.example.lesson59.entity.User;
import com.example.lesson59.payload.UserPayload;
import com.example.lesson59.repository.UserRepository;
import com.example.lesson59.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(UserPayload userPayload) {
        try {
            User user=new User();
            user.setUserName(userPayload.getUserName());
            user.setPassword(passwordEncoder.encode(userPayload.getPassword()));
            user.setFullName(userPayload.getFullName());
            user.setRoleList(roleService.getRoles(userPayload.getRolesId()));
            user.setPhoneNumber(userPayload.getPhoneNumber());

            userRepository.save(user);
            return user;
        }catch (Exception e){
            log.error(e.getMessage());
            return new User();
        }
    }
}
