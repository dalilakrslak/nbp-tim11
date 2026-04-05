package com.nbp.cinemaapp.config;

import com.nbp.cinemaapp.repository.ResetTokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class ResetTokenCleanup {

    private final ResetTokenRepository resetTokenRepository;

    public ResetTokenCleanup(final ResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void clearExpiredTokens() {
        resetTokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }
}
