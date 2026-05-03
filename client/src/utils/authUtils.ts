import { useState } from "react";

export type PasswordVisibilityState = { [key: string]: boolean };

export const usePasswordVisibility = () => {
  const [visibleFields, setVisibleFields] = useState<PasswordVisibilityState>({
    password: false,
    repeatPassword: false,
  });

  const toggleVisibility = (field: string) => {
    setVisibleFields((prev: PasswordVisibilityState) => ({
      ...prev,
      [field]: !prev[field],
    }));
  };

  return { visibleFields, toggleVisibility };
};
