import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { FaRegStar } from "react-icons/fa";

import {
  getSeatsByScreeningId,
  getMovieById,
  getScreeningById,
} from "../../api";
import type { SeatAvailability, SeatType, Screening, Movie } from "../../api";
import { MovieRatingLabels } from "../../utils";
import { Button } from "../Button";
import { Loading } from "../Loading";

import "./seatOptions.scss";

export const SeatOptions = () => {
  const { screeningId } = useParams<{ screeningId: string }>();
  const navigate = useNavigate();

  const { data: screening } = useQuery<Screening>({
    queryKey: ["screening", screeningId],
    queryFn: () => getScreeningById(screeningId!),
    enabled: !!screeningId,
  });

  const { data: movie, isLoading: isMovieLoading } = useQuery<Movie>({
    queryKey: ["movie", screening?.movie?.id],
    queryFn: () => getMovieById(screening?.movie?.id!),
    enabled: !!screening?.movie?.id,
  });

  const { data: seats = [] } = useQuery<SeatAvailability[]>({
    queryKey: ["seats", screeningId],
    queryFn: () => getSeatsByScreeningId(screeningId!),
    enabled: !!screeningId,
  });

  const [selectedSeats, setSelectedSeats] = useState<string[]>([]);

  const seatPrices: Record<SeatType, number> = {
    REGULAR: 7,
    VIP: 10,
    LOVE: 24,
  };

  const toggleSeat = (seatCode: string) => {
    setSelectedSeats((prev) =>
      prev.includes(seatCode)
        ? prev.filter((code) => code !== seatCode)
        : [...prev, seatCode]
    );
  };

  const seatClass = (seat: SeatAvailability) => {
    const base = "seat";
    const type = seat.seatType.toLowerCase();
    const isSelected = selectedSeats.includes(seat.seatCode);
    const isReserved = seat.taken;
    return `${base} ${type}${isReserved ? " reserved" : ""}${
      isSelected ? " selected" : ""
    }`;
  };

  const calculateTotalPrice = (): number => {
    return selectedSeats.reduce((total, seatCode) => {
      const seat = seats.find((s) => s.seatCode === seatCode);
      const type = seat?.seatType || "REGULAR";
      return total + (seatPrices[type] ?? 0);
    }, 0);
  };

  const totalPrice = calculateTotalPrice();

  const getSelectedSeatsDetails = () => {
    return selectedSeats.map((seatCode) => {
      const seat = seats.find((s) => s.seatCode === seatCode);
      return {
        seatCode,
        seatType: seat?.seatType || "REGULAR",
        price: seatPrices[seat?.seatType || "REGULAR"],
      };
    });
  };

  const handleContinueToPayment = () => {
    const bookingData = {
      selectedSeats: getSelectedSeatsDetails(),
      movie,
      screening,
      totalPrice,
      screeningId,
    };

    navigate(`/payment/${screeningId}`, {
      state: bookingData,
    });
  };

  const formattedDate = screening?.startTime
    ? new Date(screening.startTime)
        .toLocaleString("en-US", {
          weekday: "long",
          month: "short",
          day: "numeric",
          hour: "2-digit",
          minute: "2-digit",
          hour12: false,
        })
        .replace(",", "")
    : "";

  const rows = ["A", "B", "C", "D", "E", "F", "G", "H", "I"];

  return (
    <div className="seat-options">
      <div className="booking-header">
        {isMovieLoading ? (
          <Loading />
        ) : (
          <>
            <img
              src={
                movie?.photos.find((p) => p.isCoverImage)?.url ||
                movie?.photos[0]?.url
              }
              alt={movie?.title}
              className="movie-photo"
            />
            <div className="movie-info">
              <div className="movie-title">{movie?.title}</div>
              <div className="base-info">
                <p className="pg-rating">
                  {MovieRatingLabels[movie?.pgRating ?? " "]}
                </p>
                <p className="separator">|</p>
                <p className="language">{movie?.language}</p>
                <p className="separator">|</p>
                <p className="duration">{movie?.duration} Min</p>
              </div>
            </div>
          </>
        )}

        <div className="booking-details">
          <div className="booking-details-title">Booking Details</div>
          <p className="booking-details-info">{formattedDate}</p>
          <p className="booking-details-info">
            {screening?.hall.venue.name}, {screening?.hall.venue.street},{" "}
            {screening?.hall.venue.location.city}
          </p>
          <p className="booking-details-info">{screening?.hall.name}</p>
        </div>
      </div>

      <hr className="seat-divider" />

      <div className="layout">
        <div className="seating-layout">
          <div className="seating-layout-title">Cinema Screen</div>
          <div className="seating-layout-screen">
            <svg
              className="screen-svg"
              viewBox="0 0 300 50"
              preserveAspectRatio="none"
            >
              <path className="screen-path" d="M 10 40 Q 150 25 290 40" />
            </svg>
          </div>

          <div className="seats-container">
            {rows.map((row) => (
              <div key={row} className="seat-row">
                <div className="row-seats">
                  {[...Array(8)].map((_, i) => {
                    const id = `${row}${i + 1}`;
                    const seat = seats.find((s) => s.seatCode === id);
                    if (!seat) return null;

                    return (
                      <button
                        key={id}
                        className={seatClass(seat)}
                        title={`${id} - ${seat.seatType}`}
                        onClick={() => !seat.taken && toggleSeat(id)}
                        disabled={seat.taken}
                      >
                        {seat.seatType === "VIP" ? (
                          <>
                            <FaRegStar className="vip-star" />
                            {id}
                          </>
                        ) : (
                          id
                        )}
                      </button>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="seat-guide">
          <div className="seat-guide-title">Seat Guide</div>
          <div className="seat-guide-legend">
            <div className="legend-column">
              <div className="legend-item">
                <div className="legend-seat available">XY</div>
                <div className="legend-text">Available</div>
              </div>
              <div className="legend-item">
                <div className="legend-seat reserved">XY</div>
                <div className="legend-text">Reserved</div>
              </div>
              <div className="legend-item">
                <div className="legend-seat selected">XY</div>
                <div className="legend-text">Selected</div>
              </div>
            </div>

            <div className="legend-column">
              <div className="legend-item">
                <div className="legend-seat regular">XY</div>
                <div className="legend-text">Regular Seats (7 BAM)</div>
              </div>
              <div className="legend-item">
                <div className="legend-seat vip">
                  <FaRegStar className="vip-star" />
                  XY
                </div>
                <div className="legend-text">VIP Seats (10 BAM)</div>
              </div>
              <div className="legend-item">
                <div className="legend-seat love">XY</div>
                <div className="legend-text">Love Seats (24 BAM)</div>
              </div>
            </div>
          </div>
          <hr className="seat-guide-divider" />
          <div className="chosen-seats">
            <div className="chosen-seats-title">Chosen Seats</div>
            <div className="seat-list">
              <div>Seat(s)</div>
              <div>Total price</div>
            </div>
            <hr className="seat-guide-divider" />
            <div className="seat-summary">
              <div>
                {selectedSeats.length > 0 ? selectedSeats.join(", ") : ""}
              </div>
              {totalPrice > 0 && <div>{totalPrice} BAM</div>}
            </div>
          </div>

          <Button
            variant="primary"
            className="continue-button"
            label="Continue to Payment"
            disabled={selectedSeats.length === 0}
            onClick={handleContinueToPayment}
          />
        </div>
      </div>
    </div>
  );
};
