import { Drawer } from "antd";
import { useState, useEffect } from "react";

import { SuccessScreen } from "../SuccessScreen";
import { SignInForm } from "../SignInForm";
import { SignUpForm } from "../SignUpForm";
import { ForgotPasswordForm } from "../ForgotPasswordForm";
import { ResetPasswordForm } from "../ResetPasswordForm";

import logo from "../../assets/img/Logo.png";

import "./authentication.scss";

export type AuthenticationProps = {
  isOpen: boolean;
  onClose: () => void;
  resetToken?: string;
};

export const Authentication = ({
  isOpen,
  onClose,
  resetToken,
  children,
}: AuthenticationProps & { children?: React.ReactNode }) => {
  const [isSignUp, setIsSignUp] = useState(false);
  const [isForgotPassword, setIsForgotPassword] = useState(false);
  const [isSuccessScreenVisible, setIsSuccessScreenVisible] = useState(false);

  useEffect(() => {
    if (!isOpen) return;

    if (resetToken) {
      setIsSignUp(false);
      setIsForgotPassword(false);
      setIsSuccessScreenVisible(false);
    } else {
      setIsSignUp(false);
      setIsForgotPassword(false);
      setIsSuccessScreenVisible(false);
    }
  }, [isOpen, resetToken]);

  const resetForm = () => {
    setIsSuccessScreenVisible(false);
    setIsSignUp(false);
    setIsForgotPassword(false);
  };

  const handleAuthSuccess = () => {
    setIsSuccessScreenVisible(true);
  };

  const handleSuccessClose = () => {
    onClose();
    setIsSuccessScreenVisible(false);
  };

  const successScreenType = isSignUp
    ? "signUp"
    : resetToken
    ? "resetPassword"
    : "signIn";

  return (
    <Drawer
      placement="right"
      onClose={() => {
        resetForm();
        onClose();
      }}
      open={isOpen}
      width={528}
      className="auth-drawer"
      title={null}
      closable={false}
      destroyOnClose
    >
      {isSuccessScreenVisible ? (
        <SuccessScreen type={successScreenType} onClose={handleSuccessClose} />
      ) : (
        <div className="auth-container">
          <div className="auth-content">
            <img src={logo} alt="Logo" className="auth-logo-icon" />
            <h5 className="auth-title">
              {resetToken
                ? "Reset Password"
                : isSignUp
                ? "Hello"
                : isForgotPassword
                ? "Forgot Password"
                : "Welcome Back"}
            </h5>

            {resetToken ? (
              <ResetPasswordForm
                token={resetToken}
                onSuccess={handleAuthSuccess}
              />
            ) : isSignUp ? (
              <SignUpForm
                onSuccess={handleAuthSuccess}
                onToggleAuthType={() => setIsSignUp(false)}
              />
            ) : isForgotPassword ? (
              <ForgotPasswordForm />
            ) : (
              <SignInForm
                onSuccess={handleAuthSuccess}
                onToggleAuthType={() => setIsSignUp(true)}
                onForgotPassword={() => setIsForgotPassword(true)}
              />
            )}
          </div>
        </div>
      )}
    </Drawer>
  );
};
