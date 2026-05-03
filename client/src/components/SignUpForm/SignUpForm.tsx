import { FaEnvelope, FaLock } from "react-icons/fa6";
import { EyeOutlined, EyeInvisibleOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";

import { useRegister } from "../../hooks";
import { Button } from "../Button";
import { usePasswordVisibility } from "../../utils";
import { AuthFormErrors } from "../../api/types";
import {
  validateFieldValue,
  ValidationField,
} from "../../utils/authValidation";

type SignUpFormProps = {
  onSuccess: () => void;
  onToggleAuthType: () => void;
};

export const SignUpForm = ({
  onSuccess,
  onToggleAuthType,
}: SignUpFormProps) => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [confirmPassword, setConfirmPassword] = useState<string>("");
  const [errors, setErrors] = useState<AuthFormErrors | undefined>(undefined);
  const [hasAttemptedSubmit, setHasAttemptedSubmit] = useState<boolean>(false);

  const { visibleFields, toggleVisibility } = usePasswordVisibility();
  const registerMutation = useRegister();

  useEffect(() => {
    setEmail("");
    setPassword("");
    setConfirmPassword("");
    setErrors(undefined);
    setHasAttemptedSubmit(false);
    registerMutation.reset();
  }, []);

  const validateForm = () => {
    const newErrors: AuthFormErrors = {};

    const emailError = validateFieldValue("email", email);
    const passwordError = validateFieldValue("password", password);
    const confirmPasswordError = validateFieldValue(
      "confirmPassword",
      confirmPassword,
      { password }
    );

    if (emailError) newErrors.email = emailError;
    if (passwordError) newErrors.password = passwordError;
    if (confirmPasswordError) newErrors.confirmPassword = confirmPasswordError;

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const validateField = (field: ValidationField, value: string) => {
    if (!hasAttemptedSubmit) return;

    const newErrors: AuthFormErrors = { ...errors };
    const error = validateFieldValue(field, value, { password });

    if (error) {
      newErrors[field] = error;
    } else {
      delete newErrors[field];
    }

    if (field === "password" && confirmPassword) {
      const confirmError = validateFieldValue(
        "confirmPassword",
        confirmPassword,
        { password: value }
      );
      if (confirmError) {
        newErrors.confirmPassword = confirmError;
      } else {
        delete newErrors.confirmPassword;
      }
    }

    setErrors(newErrors);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setHasAttemptedSubmit(true);

    if (!validateForm()) return;

    try {
      await registerMutation.mutateAsync({
        email,
        password,
      });
      setErrors(undefined);
      onSuccess();
    } catch (error: unknown) {
      const fallbackMessage = "Registration failed. Please try again.";
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

        <label className="auth-label">Confirm Password</label>
        <div className="auth-input-wrapper">
          <FaLock className="auth-input-icon" />
          <input
            className={`auth-input ${
              errors?.confirmPassword ? "auth-input-error" : ""
            }`}
            type={visibleFields.repeatPassword ? "text" : "password"}
            placeholder="Confirm Password"
            value={confirmPassword}
            onChange={(e) => {
              setConfirmPassword(e.target.value);
              validateField("confirmPassword", e.target.value);
            }}
          />
          <button
            type="button"
            className="auth-toggle-password"
            onClick={() => toggleVisibility("repeatPassword")}
          >
            {visibleFields.repeatPassword ? (
              <EyeInvisibleOutlined />
            ) : (
              <EyeOutlined />
            )}
          </button>
        </div>
        {errors?.confirmPassword && (
          <p className="auth-error-text">{errors.confirmPassword}</p>
        )}

        <Button
          variant="primary"
          label="Sign Up"
          className="auth-submit-button"
          loading={registerMutation.isPending}
          loadingText="Processing..."
          type="submit"
        />

        <div className="auth-change-type">
          <p className="auth-change-type-text">Already have an account? </p>
          <a
            href="#"
            className="auth-change-type-link"
            onClick={handleToggleAuthType}
          >
            Sign In
          </a>
        </div>
      </form>
    </>
  );
};
