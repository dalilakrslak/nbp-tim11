import { Drawer } from "antd";
import { useState, useEffect } from "react";

import { SuccessScreen } from "../SuccessScreen";
import { SignInForm } from "../SignInForm";
import { SignUpForm } from "../SignUpForm";

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
}: AuthenticationProps) => {
  const [isSignUp, setIsSignUp] = useState(false);
  const [isSuccessScreenVisible, setIsSuccessScreenVisible] = useState(false);

  useEffect(() => {
    if (!isOpen) return;

    setIsSignUp(false);
    setIsSuccessScreenVisible(false);
  }, [isOpen]);

  const resetForm = () => {
    setIsSuccessScreenVisible(false);
    setIsSignUp(false);
  };

  const handleAuthSuccess = () => {
    setIsSuccessScreenVisible(true);
  };

  const handleSuccessClose = () => {
    onClose();
    setIsSuccessScreenVisible(false);
  };

  const successScreenType = isSignUp ? "signUp" : "signIn";

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
            <h5 className="auth-title">{isSignUp ? "Hello" : "Welcome Back"}</h5>

            {isSignUp ? (
              <SignUpForm
                onSuccess={handleAuthSuccess}
                onToggleAuthType={() => setIsSignUp(false)}
              />
            ) : (
              <SignInForm
                onSuccess={handleAuthSuccess}
                onToggleAuthType={() => setIsSignUp(true)}
              />
            )}
          </div>
        </div>
      )}
    </Drawer>
  );
};
