package com.example.lesson59.security;

import com.example.lesson59.entity.RefreshToken;
import com.example.lesson59.entity.User;
import com.example.lesson59.payload.AuthPayload;
import com.example.lesson59.repository.RefreshTokenRepository;
import com.example.lesson59.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshUtils {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${refresh-token.time.validity}")
    long expiredDate;

    public RefreshToken createRefreshToken(User user) {
        try {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setExpiredDate(new Date(new Date().getTime() + expiredDate)); // now
            refreshToken.setRefreshToken(UUID.randomUUID().toString());
            refreshTokenRepository.save(refreshToken);

            return refreshToken;
        }catch (Exception e){
            log.error(e.getMessage());
            try {
                throw new Exception(e.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public boolean validateToken(RefreshToken refreshToken) {
        return refreshToken.getExpiredDate().after(new Date());
    }

    //  30   31
    public AuthPayload refreshToken(AuthPayload authPayload) throws Exception { // AACC  user date
        RefreshToken refreshToken = refreshTokenRepository.findFirstByRefreshTokenOrderByCreatedAtDesc(authPayload.getRefreshToken());

        if (!validateToken(refreshToken))
            throw new Exception("token is expired");

//     return createTokenByUserName(refreshToken); // access create

        return createTokenByUserName(refreshToken.getUser().getUserName());
    }
  //   ACCESS
    public AuthPayload createTokenByUserName(String username) {
        User user = userRepository.findByUserName(username);

        if (user == null)
            throw new RuntimeException("user not found");

//        create
        String token = jwtTokenProvider.createToken(user.getUserName(), user.getRoleList());
        if (token == null)
            throw new RuntimeException("nimadir xato");

        RefreshToken refreshToken=createRefreshToken(user);

        // ACCESS token + Refresh token   30 kun refresh

        AuthPayload authPayload = new AuthPayload();
        authPayload.setStatus(true);
        authPayload.setUserName(user.getUserName());
        authPayload.setAccessToken(token);
        authPayload.setRefreshToken(refreshToken.getRefreshToken());

//        Map<String,Object> result=new HashMap<>();
//        result.put("status",true);
//        result.put("userName",user.getUserName());
//        result.put("token",token);

        return authPayload;
    }

}
