import { useState } from "react";
import { FaEnvelope } from "react-icons/fa6";
import { Button } from "../Button";
import { useForgotPassword } from "../../hooks";
import { AuthFormErrors } from "../../api/types";
import { validateFieldValue } from "../../utils/authValidation";

export const ForgotPasswordForm = () => {
  const [email, setEmail] = useState("");
  const [errors, setErrors] = useState<AuthFormErrors | undefined>(undefined);
  const [hasAttemptedSubmit, setHasAttemptedSubmit] = useState(false);
  const [emailSent, setEmailSent] = useState(false);

  const forgotPasswordMutation = useForgotPassword();

  const validateForm = () => {
    const newErrors: AuthFormErrors = {};
    const emailError = validateFieldValue("email", email);
    if (emailError) newErrors.email = emailError;
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const maskEmail = (email: string) => {
    return email.replace(/(.{1}).+(@.+)/, (_, a, b) => `${a}*****${b}`);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setHasAttemptedSubmit(true);

    if (!validateForm()) return;

    try {
      await forgotPasswordMutation.mutateAsync({ email });
      setEmailSent(true);
    } catch (error: unknown) {
      const fallbackMessage = "Failed to send reset email. Please try again.";
      let errorMessage: string | undefined;

      if (error instanceof Error) errorMessage = error.message;
      else if (typeof error === "string") errorMessage = error;
      if (!errorMessage) errorMessage = fallbackMessage;

      setErrors({ general: errorMessage });
    }
  };

  if (emailSent) {
    return (
      <div className="auth-form">
        <h2 className="auth-title">Check your email</h2>
        <p className="auth-info-text">
          We have sent a password reset link to{" "}
          <strong>{maskEmail(email)}</strong>.
        </p>
        <p className="auth-info-text">
          Please click the link in your email to continue.
        </p>
      </div>
    );
  }

  return (
    <form className="auth-form" onSubmit={handleSubmit} noValidate>
      {errors?.general && <p className="auth-error-text">{errors.general}</p>}

      <label className="auth-label">Email</label>
      <div className="auth-input-wrapper">
        <FaEnvelope className="auth-input-icon" />
        <input
          className={`auth-input ${errors?.email ? "auth-input-error" : ""}`}
          type="email"
          placeholder="Email Address"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      {errors?.email && <p className="auth-error-text">{errors.email}</p>}

      <Button
        variant="primary"
        label="Send Reset Link"
        type="submit"
        loading={forgotPasswordMutation.isPending}
        loadingText="Sending..."
        className="auth-submit-button"
      />
    </form>
  );
};
