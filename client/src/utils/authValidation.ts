export type ValidationField = "email" | "password" | "confirmPassword";

export const validateFieldValue = (
  field: ValidationField,
  value: string,
  additionalData?: { password?: string }
): string | null => {
  switch (field) {
    case "email":
      if (!value) return "Email is required";
      if (!/\S+@\S+\.\S+/.test(value))
        return "Please enter a valid email address";
      return null;

    case "password":
      if (!value) return "Password is required";
      if (value.length < 6) return "Password must be at least 6 characters";
      return null;

    case "confirmPassword":
      if (!value) return "Please confirm your password";
      if (additionalData?.password && value !== additionalData.password)
        return "Passwords do not match";
      return null;

    default:
      return null;
  }
};
