package com.example.lesson59.controller;

import com.example.lesson59.entity.RefreshToken;
import com.example.lesson59.entity.User;
import com.example.lesson59.model.Result;
import com.example.lesson59.payload.AuthPayload;
import com.example.lesson59.payload.LoginPayload;
import com.example.lesson59.repository.UserRepository;
import com.example.lesson59.security.JwtTokenProvider;
import com.example.lesson59.security.RefreshUtils;
import com.example.lesson59.service.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleServiceImpl roleService;
    private final AuthenticationManager authenticationManager;
    private final RefreshUtils refreshUtils;

    @PostMapping("/login")
    public ResponseEntity<?> getLogin(@RequestBody LoginPayload loginPayload) {

        User user = userRepository.findByUserName(loginPayload.getUserName());

        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginPayload.getUserName(), loginPayload.getPassword()));

//        create
        String token = jwtTokenProvider.createToken(user.getUserName(), user.getRoleList());
        if (token == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nimadir xato");

        RefreshToken refreshToken=refreshUtils.createRefreshToken(user);

        AuthPayload authPayload = new AuthPayload();
        authPayload.setStatus(true);
        authPayload.setUserName(user.getUserName());
        authPayload.setAccessToken(token);
        authPayload.setRefreshToken(refreshToken.getRefreshToken());

//        Map<String,Object> result=new HashMap<>();
//        result.put("status",true);
//        result.put("userName",user.getUserName());
//        result.put("token",token);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authPayload);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody AuthPayload authPayload) {  // refresh-token   AACC
        try {
            return ResponseEntity.ok(refreshUtils.refreshToken(authPayload));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getTest(@PathVariable Long id) {
        Result result = roleService.getRole(id);
        return ResponseEntity.status(result.isStatus() ? 200 : 404).body(result);
    }

}
