import React from "react";
import "./button.scss";

export type ButtonProps = {
  variant?: "primary" | "secondary" | "tertiary" | "navbar";
  label?: React.ReactNode;
  icon?: React.ReactNode;
  disabled?: boolean;
  loading?: boolean;
  loadingText?: string;
  children?: React.ReactNode;
} & React.ButtonHTMLAttributes<HTMLButtonElement>;

export const Button = ({
  variant = "primary",
  label,
  className = "",
  icon,
  disabled = false,
  loading = false,
  loadingText = "Processing...",
  children,
  ...restProps
}: ButtonProps) => {
  const getContent = () => {
    if (loading) {
      return loadingText;
    }

    return (
      <>
        {label}
        {icon}
      </>
    );
  };

  return (
    <button
      className={`button button-${variant} ${
        disabled || loading ? "disabled" : ""
      } ${loading ? "loading" : ""} ${className}`}
      disabled={disabled || loading}
      {...restProps}
    >
      {getContent()}
    </button>
  );
};
