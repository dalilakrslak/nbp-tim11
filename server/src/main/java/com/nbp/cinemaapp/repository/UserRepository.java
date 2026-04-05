package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.dto.projection.UserProjection;
import com.nbp.cinemaapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(final String email);

    Boolean existsByEmail(final String email);

    Optional<UserProjection> findProjectedByEmail(final String email);
}
