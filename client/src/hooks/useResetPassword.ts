import { useMutation } from "@tanstack/react-query";
import { axiosApp } from "../api/axiosApp";

export type ResetPasswordRequest = {
  token: string;
  newPassword: string;
};

export const useResetPassword = (
  onSuccess?: () => void,
  onError?: (error: any) => void
) => {
  const resetPasswordFn = async ({
    token,
    newPassword,
  }: ResetPasswordRequest) => {
    await axiosApp.post("/auth/reset-password/confirm", {
      token,
      newPassword,
    });
  };

  return useMutation<void, any, ResetPasswordRequest>({
    mutationFn: resetPasswordFn,
    onSuccess,
    onError,
  });
};
