import { useMutation } from "@tanstack/react-query";
import { axiosApp } from "../api/axiosApp";

export type ForgotPasswordRequest = {
  email: string;
};

export const useForgotPassword = (
  onSuccess?: () => void,
  onError?: (error: any) => void
) => {
  const sendForgotPasswordEmail = async (request: ForgotPasswordRequest) => {
    const response = await axiosApp.post("/auth/reset-password", request);
    return response.data;
  };

  return useMutation<void, any, ForgotPasswordRequest>({
    mutationFn: sendForgotPasswordEmail,
    onSuccess,
    onError,
  });
};
