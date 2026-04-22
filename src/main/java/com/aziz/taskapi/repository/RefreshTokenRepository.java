package com.aziz.taskapi.repository;

import com.aziz.taskapi.entity.AppUser;
import com.aziz.taskapi.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(AppUser user);

    void deleteByUser(AppUser user);
}