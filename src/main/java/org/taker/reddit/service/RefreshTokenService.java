package org.taker.reddit.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taker.reddit.exception.SubredditNotFoundException;
import org.taker.reddit.model.RefreshToken;
import org.taker.reddit.repository.RefreshTokenRespository;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private RefreshTokenRespository refreshTokenRespository;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRespository.save(refreshToken);
    }

    public void validateRefreshToken(String token) {
        refreshTokenRespository.findByToken(token).orElseThrow(() -> new SubredditNotFoundException("Invalid refresh token"));

    }

    public void deleteRefreshToken(String token) {
        refreshTokenRespository.deleteByToken(token);
    }
}
