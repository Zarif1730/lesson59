package com.example.lesson59.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthPayload {
    String accessToken;
    String refreshToken;
    boolean status;
    String userName;

}
