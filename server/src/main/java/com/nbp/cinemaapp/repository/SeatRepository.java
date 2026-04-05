package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findBySeatCodeInAndHallId(Set<String> seatCodes, UUID hallId);

}
