import { FaEnvelope, FaLock } from "react-icons/fa6";
import { EyeOutlined, EyeInvisibleOutlined } from "@ant-design/icons";
import { useState } from "react";

import { useLogin } from "../../hooks";
import { Button } from "../Button";
import { usePasswordVisibility } from "../../utils";
import { AuthFormErrors } from "../../api/types";
import {
  validateFieldValue,
  ValidationField,
} from "../../utils/authValidation";

type SignInFormProps = {
  onSuccess: () => void;
  onToggleAuthType: () => void;
  onForgotPassword?: () => void;
};

export const SignInForm = ({
  onSuccess,
  onToggleAuthType,
  onForgotPassword,
}: SignInFormProps) => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [errors, setErrors] = useState<AuthFormErrors | undefined>();
  const [hasAttemptedSubmit, setHasAttemptedSubmit] = useState<boolean>(false);

  const { visibleFields, toggleVisibility } = usePasswordVisibility();
  const loginMutation = useLogin();

  const validateForm = () => {
    const newErrors: AuthFormErrors = {};

    const emailError = validateFieldValue("email", email);
    const passwordError = validateFieldValue("password", password);

    if (emailError) newErrors.email = emailError;
    if (passwordError && passwordError != "Password must be at least 6 characters") newErrors.password = passwordError;

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const validateField = (field: string, value: string) => {
    if (!hasAttemptedSubmit) return;

    const newErrors: AuthFormErrors = { ...errors };
    const error = validateFieldValue(field as ValidationField, value);

    if (error && error != "Password must be at least 6 characters") {
      newErrors[field as keyof AuthFormErrors] = error;
    } else {
      delete newErrors[field as keyof AuthFormErrors];
    }

    setErrors(newErrors);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setHasAttemptedSubmit(true);

    if (!validateForm()) return;

    try {
      await loginMutation.mutateAsync({
        email,
        password,
      });
      setErrors(undefined);
      onSuccess();
    } catch (error: unknown) {
      const fallbackMessage = "Authentication failed. Please try again.";
      let errorMessage: string | undefined;

      if (error instanceof Error) {
        errorMessage = error.message;
      } else if (typeof error === "string") {
        errorMessage = error;
      }

      if (!errorMessage) {
        setErrors((prev) => ({ ...prev, general: fallbackMessage }));
      } else if (errorMessage.toLowerCase().includes("email")) {
        setErrors((prev) => ({ ...prev, email: errorMessage }));
      } else if (errorMessage.toLowerCase().includes("password")) {
        setErrors((prev) => ({ ...prev, password: errorMessage }));
      } else {
        setErrors((prev) => ({ ...prev, general: errorMessage }));
      }
    }
  };

  const handleToggleAuthType = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    onToggleAuthType();
  };

  return (
    <>
      {errors?.general && (
        <div className="auth-general-error">
          <p className="auth-error-text">{errors.general}</p>
        </div>
      )}

      <form className="auth-form" onSubmit={handleSubmit} noValidate>
        <label className="auth-label">Email</label>
        <div className="auth-input-wrapper">
          <FaEnvelope className="auth-input-icon" />
          <input
            className={`auth-input ${errors?.email ? "auth-input-error" : ""}`}
            type="email"
            placeholder="Email Address"
            value={email}
            onChange={(e) => {
              setEmail(e.target.value);
              validateField("email", e.target.value);
            }}
          />
        </div>
        {errors?.email && <p className="auth-error-text">{errors.email}</p>}

        <label className="auth-label">Password</label>
        <div className="auth-input-wrapper">
          <FaLock className="auth-input-icon" />
          <input
            className={`auth-input ${
              errors?.password ? "auth-input-error" : ""
            }`}
            type={visibleFields.password ? "text" : "password"}
            placeholder="Password"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
              validateField("password", e.target.value);
            }}
          />
          <button
            type="button"
            className="auth-toggle-password"
            onClick={() => toggleVisibility("password")}
          >
            {visibleFields.password ? (
              <EyeInvisibleOutlined />
            ) : (
              <EyeOutlined />
            )}
          </button>
        </div>
        {errors?.password && (
          <p className="auth-error-text">{errors.password}</p>
        )}

        {onForgotPassword && (
          <a
            href="#"
            className="auth-change-type-link auth-forgot-password"
            onClick={(e) => {
              e.preventDefault();
              onForgotPassword();
            }}
          >
            Forgot password?
          </a>
        )}

        <Button
          variant="primary"
          label="Sign In"
          className="auth-submit-button"
          loading={loginMutation.isPending}
          loadingText="Processing..."
          type="submit"
        />

        <div className="auth-change-type">
          <p className="auth-change-type-text">Don't have an account yet? </p>
          <a
            href="#"
            className="auth-change-type-link"
            onClick={handleToggleAuthType}
          >
            Sign Up
          </a>
        </div>
      </form>
    </>
  );
};
