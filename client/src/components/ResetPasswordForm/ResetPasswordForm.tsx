import { FaLock } from "react-icons/fa6";
import { EyeOutlined, EyeInvisibleOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";

import { Button } from "../Button";
import { usePasswordVisibility } from "../../utils";
import { AuthFormErrors } from "../../api/types";
import {
  validateFieldValue,
  ValidationField,
} from "../../utils/authValidation";
import { useResetPassword } from "../../hooks";

export type ResetPasswordFormProps = {
  token: string;
  onSuccess: () => void;
};

export const ResetPasswordForm = ({
  token,
  onSuccess,
}: ResetPasswordFormProps) => {
  const [newPassword, setNewPassword] = useState<string>("");
  const [repeatPassword, setRepeatPassword] = useState<string>("");
  const [errors, setErrors] = useState<AuthFormErrors | undefined>(undefined);
  const [hasAttemptedSubmit, setHasAttemptedSubmit] = useState<boolean>(false);

  const { visibleFields, toggleVisibility } = usePasswordVisibility();
  const resetPasswordMutation = useResetPassword();

  useEffect(() => {
    setNewPassword("");
    setRepeatPassword("");
    setErrors(undefined);
    setHasAttemptedSubmit(false);
    resetPasswordMutation.reset();
  }, [token]);

  const validateForm = () => {
    const newErrors: AuthFormErrors = {};

    const passwordError = validateFieldValue("password", newPassword);
    const repeatPasswordError = validateFieldValue(
      "confirmPassword",
      repeatPassword,
      { password: newPassword }
    );

    if (passwordError) newErrors.password = passwordError;
    if (repeatPasswordError) newErrors.confirmPassword = repeatPasswordError;

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const validateField = (field: ValidationField, value: string) => {
    if (!hasAttemptedSubmit) return;

    const newErrors: AuthFormErrors = { ...errors };
    const error = validateFieldValue(field, value, { password: newPassword });

    if (error) {
      newErrors[field] = error;
    } else {
      delete newErrors[field];
    }

    if (field === "password" && repeatPassword) {
      const confirmError = validateFieldValue(
        "confirmPassword",
        repeatPassword,
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
      await resetPasswordMutation.mutateAsync({
        token,
        newPassword,
      });
      setErrors(undefined);
      onSuccess();
    } catch (error: unknown) {
      const fallbackMessage = "Password reset failed. Please try again.";
      let errorMessage: string | undefined;

      if (error instanceof Error) {
        errorMessage = error.message;
      } else if (typeof error === "string") {
        errorMessage = error;
      }

      if (!errorMessage) {
        setErrors((prev) => ({ ...prev, general: fallbackMessage }));
      } else if (errorMessage.toLowerCase().includes("password")) {
        setErrors((prev) => ({ ...prev, password: errorMessage }));
      } else {
        setErrors((prev) => ({ ...prev, general: errorMessage }));
      }
    }
  };

  return (
    <>
      {errors?.general && (
        <div className="auth-general-error">
          <p className="auth-error-text">{errors.general}</p>
        </div>
      )}

      <form className="auth-form" onSubmit={handleSubmit} noValidate>
        <label className="auth-label">New Password</label>
        <div className="auth-input-wrapper">
          <FaLock className="auth-input-icon" />
          <input
            className={`auth-input ${
              errors?.password ? "auth-input-error" : ""
            }`}
            type={visibleFields.password ? "text" : "password"}
            placeholder="New Password"
            value={newPassword}
            onChange={(e) => {
              setNewPassword(e.target.value);
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

        <label className="auth-label">Repeat Password</label>
        <div className="auth-input-wrapper">
          <FaLock className="auth-input-icon" />
          <input
            className={`auth-input ${
              errors?.confirmPassword ? "auth-input-error" : ""
            }`}
            type={visibleFields.repeatPassword ? "text" : "password"}
            placeholder="Repeat Password"
            value={repeatPassword}
            onChange={(e) => {
              setRepeatPassword(e.target.value);
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
          label="Reset Password"
          className="auth-submit-button"
          loading={resetPasswordMutation.isPending}
          loadingText="Processing..."
          type="submit"
        />
      </form>
    </>
  );
};
