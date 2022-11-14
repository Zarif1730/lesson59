package com.example.lesson59.repository;

import com.example.lesson59.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findFirstByRefreshTokenOrderByCreatedAtDesc(String refreshToken);

}
