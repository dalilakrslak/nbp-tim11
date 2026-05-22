import { useEffect, useState } from "react";
import {
  CardNumberElement,
  CardExpiryElement,
  CardCvcElement,
  useStripe,
  useElements,
} from "@stripe/react-stripe-js";
import { useNavigate } from "react-router-dom";

import { authApi, User } from "../../api";
import type { Movie, Screening } from "../../api";
import { Button } from "../Button";
import { useStripeCharge } from "../../hooks";

import "./stripeForm.scss";

export type ChargeRequest = {
  token: string;
  amount: number;
  userId: string;
  bookingDetails?: BookingDetails;
};

export type PaymentResponse = {
  success: boolean;
  transactionId?: string;
  message?: string;
  ticketId?: string;
};

export type SelectedSeat = {
  seatCode: string;
  seatType: string;
  price: number;
};

export type BookingData = {
  selectedSeats: SelectedSeat[];
  movie: Movie;
  screening: Screening;
  totalPrice: number;
  screeningId: string;
};

export type BookingDetails = {
  screeningId: string;
  selectedSeatCodes: string[];
  movieTitle: string;
  screeningTime: Date;
  venue: string;
  hall: string;
};

export type StripeFormProps = {
  bookingData: BookingData;
  totalAmount: number;
};

export const StripeForm = ({ bookingData, totalAmount }: StripeFormProps) => {
  const stripe = useStripe();
  const elements = useElements();
  const navigate = useNavigate();

  const [user, setUser] = useState<User | null>(null);
  const [paymentError, setPaymentError] = useState<string>("");
  const [paymentSuccess, setPaymentSuccess] = useState<PaymentResponse | null>(
    null
  );

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const currentUser = await authApi.getCurrentUser();
        setUser(currentUser);
      } catch (error) {
        console.error("Failed to load user:", error);
        setPaymentError("Failed to load user information");
      }
    };

    fetchUser();
  }, []);

  const handleBackToHome = () => {
    navigate("/");
  };

  const mutation = useStripeCharge(
    (data: PaymentResponse) => {
      setPaymentSuccess(data);
      setPaymentError("");
    },
    (error: any) => {
      console.error("Payment error:", error);
      setPaymentError(
        error?.response?.data?.message || "Payment failed. Please try again."
      );
      setPaymentSuccess(null);
    }
  );

  const prepareBookingDetails = (): BookingDetails => {
    return {
      screeningId: bookingData.screeningId,
      selectedSeatCodes: bookingData.selectedSeats.map((seat) => seat.seatCode),
      movieTitle: bookingData.movie.title,
      screeningTime: bookingData.screening.startTime,
      venue: bookingData.screening.hall.venue.name,
      hall: bookingData.screening.hall.name,
    };
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setPaymentError("");

    if (!stripe || !elements || !user) {
      setPaymentError("Payment system not ready. Please try again.");
      return;
    }

    const cardNumberElement = elements.getElement(CardNumberElement);
    if (!cardNumberElement) {
      setPaymentError("Card information is required.");
      return;
    }

    const { token, error } = await stripe.createToken(cardNumberElement);

    if (error) {
      console.error("Stripe token error:", error);
      setPaymentError(error.message || "Failed to process card information.");
    } else if (token) {
      const bookingDetails = prepareBookingDetails();

      mutation.mutate({
        token: token.id,
        amount: totalAmount,
        userId: user.id,
        bookingDetails,
      });
    }
  };

  const elementStyle = {
    style: {
      base: {
        fontSize: "16px",
        color: "#424770",
        "::placeholder": {
          color: "#667085",
        },
      },
      invalid: {
        color: "#9e2146",
      },
    },
  };

  return (
    <div className="stripe-form">
      {paymentSuccess && (
        <div className="payment-success-overlay">
          <div className="payment-success-modal">
            <h2>Payment Successful!</h2>
            <p>Your booking has been confirmed.</p>
            <p className="receipt-message"></p>

            <div className="success-actions">
              <Button
                variant="primary"
                label="Back to Home"
                onClick={handleBackToHome}
                className="success-button"
              />
            </div>
          </div>
        </div>
      )}

      <div className="payment-header-container">
        <h5 className="payment-header">Payment Details</h5>
      </div>

      <form onSubmit={handleSubmit} className="payment-form">
        <div className="split-card-form">
          <div className="input-field full-width">
            <label className="field-label">Card Number</label>
            <div className="input-wrapper">
              <CardNumberElement options={elementStyle} />
            </div>
          </div>

          <div className="row">
            <div className="input-field half-width">
              <label className="field-label">Expiry Date</label>
              <div className="input-wrapper">
                <CardExpiryElement options={elementStyle} />
              </div>
            </div>
            <div className="input-field half-width">
              <label className="field-label">CVV</label>
              <div className="input-wrapper">
                <CardCvcElement options={elementStyle} />
              </div>
            </div>
          </div>
        </div>

        {paymentError && (
          <div className="payment-error" role="alert">
            {paymentError}
          </div>
        )}

        <Button
          type="submit"
          variant="primary"
          className="payment-submit-button"
          label={
            mutation.isPending
              ? "Processing Payment..."
              : `Pay ${totalAmount} BAM`
          }
          disabled={!stripe || mutation.isPending || !user}
        ></Button>
      </form>
    </div>
  );
};
