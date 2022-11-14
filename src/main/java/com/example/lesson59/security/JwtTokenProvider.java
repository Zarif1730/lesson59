package com.example.lesson59.security;

import com.example.lesson59.entity.Role;
import com.example.lesson59.entity.User;
import com.example.lesson59.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final CustomUserDetails customUserDetails;

    @Value("${spring.secret.key}")
    private String secret;

    @Value("${spring.time.validity}")
    private Long validity;

    @PostConstruct //
    void init(){
        secret= Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String username, List<Role> roleList){
        Claims claims= Jwts.claims().setSubject(username);
        claims.put("roles",roleList);

        return Jwts
                .builder()
                .addClaims(claims)
                .setIssuedAt(new Date()) // 11 noyabr
                .setExpiration(new Date(new Date().getTime()+validity)) // 12 noyabr
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String resolveToken(HttpServletRequest req){
        String token=req.getHeader("Authorization");
        if (token!=null&&token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;

    }

    public User getUser(String token){
        return userRepository.findByUserName(Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody().getSubject());
    }

//    public Authentication getAuthentication(String token){
//        return new UsernamePasswordAuthenticationToken(userDetailsService,"",getUser(token).getRoleList());
//    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails=customUserDetails.loadUserByUsername(getUser(token).getUserName());
        return new UsernamePasswordAuthenticationToken(userDetails, "", getUser(token).getRoleList());
    }

}
