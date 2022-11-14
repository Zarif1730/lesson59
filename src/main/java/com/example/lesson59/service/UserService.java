package com.example.lesson59.service;


import com.example.lesson59.entity.User;
import com.example.lesson59.payload.UserPayload;

public interface UserService {
    User saveUser(UserPayload userPayload);
}
