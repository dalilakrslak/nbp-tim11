import { useLocation } from "react-router-dom";
import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";

import { StripeForm } from "../StripeForm";
import type { Movie, Screening } from "../../api";
import { MovieRatingLabels } from "../../utils";

import "./payment.scss";

const stripePromise = loadStripe(
  "pk_test_51RdRoPFTGQmTbzgKC4fAO0djKQaTzWLKnp2Ydc9IFpILMjHaSjCZOgtJxgnoKQlPlIfQGDhr23lHN2384fXzS7f900aKmjdGzu"
);

type SelectedSeat = {
  seatCode: string;
  seatType: string;
  price: number;
};

type BookingData = {
  selectedSeats: SelectedSeat[];
  movie: Movie;
  screening: Screening;
  totalPrice: number;
  screeningId: string;
};

export const Payment = () => {
  const location = useLocation();

  const bookingData: BookingData = location.state as BookingData;

  if (!bookingData) {
    return (
      <div className="payment-container">
        <div className="payment-error">
          <h2>No booking data found</h2>
          <p>Please select your seats first.</p>
        </div>
      </div>
    );
  }

  const { selectedSeats, movie, screening, totalPrice } = bookingData;

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

  return (
    <div className="payment-container">
      <Elements stripe={stripePromise}>
        <StripeForm bookingData={bookingData} totalAmount={totalPrice} />
      </Elements>

      <div className="booking-summary">
        <h6 className="booking-summary-header">Booking Summary</h6>
        <div className="booking-summary-details">
          <div className="movie-details">
            <div className="basic-info-container">
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
            </div>

            <hr className="seat-divider" />
            <div className="date-and-time">
              <p className="date-and-time-gray">Date and Time</p>
              <p className="date-and-time-white">{formattedDate}</p>
            </div>

            <div className="cinema-details">
              <p className="cinema-details-gray">Cinema Details</p>
              <p className="cinema-details-white">
                {screening.hall.venue.name}: {screening.hall.venue.street}
              </p>
              <p className="cinema-details-white">{screening.hall.name}</p>
            </div>
            <div className="seat-details">
              <p className="seat-details-gray">Seat(s) Details</p>
              <p className="seat-details-white">
                Seat(s): {selectedSeats.map((seat) => seat.seatCode).join(", ")}
              </p>
              <div className="price-details">
                <p className="price-details-gray">Price Details</p>
                <strong className="price-details-white">
                  Total Price: {totalPrice} BAM
                </strong>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
