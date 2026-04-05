package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, UUID> {
    Optional<ResetToken> findByToken(final String token);

    void deleteAllByExpiresAtBefore(final LocalDateTime time);
}
