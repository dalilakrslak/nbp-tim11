package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID>, JpaSpecificationExecutor<Movie> {
    @Query("SELECT m FROM Movie m " +
            "JOIN m.movieGenres mg " +
            "JOIN mg.genre g " +
            "WHERE g.id IN :genreIds " +
            "AND m.id <> :movieId " +
            "GROUP BY m.id " +
            "ORDER BY COUNT(m.id) DESC")
    Page<Movie> findSimilarMovies(@Param("genreIds") final List<UUID> genreIds, @Param("movieId") final UUID movieId, final Pageable pageable);
}
