import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import { Button } from "../Button";
import logo from "../../assets/img/Logo.png";
import signInImg from "../../assets/img/sign-in-success.png";
import signUpImg from "../../assets/img/sign-up-success.png";

import "../Authentication/authentication.scss";

export type SuccessScreenType = "signIn" | "signUp" | "resetPassword";

export type SuccessScreenProps = {
  type: SuccessScreenType;
  onClose: () => void;
};

export const SuccessScreen = ({ type, onClose }: SuccessScreenProps) => {
  const navigate = useNavigate();
  const isSignIn = type === "signIn";
  const isSignUp = type === "signUp";
  const isResetPassword = type === "resetPassword";

  // Redirect destination based on type
  const redirectPath = isResetPassword ? "/?openLogin=true" : "/";

  // auto-close + redirect
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
      navigate(redirectPath);
    }, 2500);

    return () => clearTimeout(timer);
  }, [navigate, onClose, redirectPath]);

  const handleButtonClick = () => {
    onClose();
    navigate(redirectPath);
  };

  const { title, subtitle, buttonText, image } = (() => {
    if (isResetPassword) {
      return {
        title: "Password Reset Successful!",
        subtitle: "You can now sign in with your new password.",
        buttonText: "Go to Sign In",
        image: signInImg,
      };
    }

    if (isSignUp) {
      return {
        title: "You're all set!",
        subtitle: "Start exploring latest movies, venues, and ticket options!",
        buttonText: "See Movies",
        image: signUpImg,
      };
    }

    return {
      title: "Sign In Successful!",
      subtitle: "You will be redirected to the homepage.",
      buttonText: "Go to Home",
      image: signInImg,
    };
  })();

  return (
    <div className="auth-container">
      <div className="auth-content">
        <img src={logo} alt="Logo" className="auth-logo-icon" />

        <h5 className="auth-title">{title}</h5>
        <p className="auth-subtitle">{subtitle}</p>

        <div className="auth-success-image">
          <img src={image} alt="Success" />
        </div>

        <Button
          variant="primary"
          label={buttonText}
          className="auth-submit-button"
          onClick={handleButtonClick}
        />
      </div>
    </div>
  );
};
