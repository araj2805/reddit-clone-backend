package org.taker.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.taker.reddit.model.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRespository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}
