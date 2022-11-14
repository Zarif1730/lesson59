package com.example.lesson59.controller.user;

import com.example.lesson59.entity.User;
import com.example.lesson59.payload.UserPayload;
import com.example.lesson59.repository.UserRepository;
import com.example.lesson59.security.SecurityUtils;
import com.example.lesson59.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody UserPayload userPayload){
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.saveUser(userPayload));
    }

    @GetMapping("/update")
    public ResponseEntity<?> userUpdate(@RequestBody UserPayload userPayload){
        System.out.println(userPayload);
        User user=userRepository.findByUserName(SecurityUtils.getCurrentUsername());
        System.out.println(user);
        user.setFullName(userPayload.getFullName());
//        user.setPassword(passwordEncoder.encode(userPayload.getPassword()));
//        user.setUserName(userPayload.getUserName());

        userRepository.save(user);
        return ResponseEntity.ok(userRepository.findByUserName(SecurityUtils.getCurrentUsername()));
    }

}
