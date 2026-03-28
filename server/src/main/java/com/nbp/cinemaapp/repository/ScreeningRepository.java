package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, UUID>, JpaSpecificationExecutor<Screening> {

    List<Screening> findByMovieId(final UUID movieId);
}
