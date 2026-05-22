import { useMutation } from "@tanstack/react-query";
import { axiosApp } from "../api/axiosApp";

export type ChangePasswordRequest = {
  oldPassword: string;
  newPassword: string;
};

export const useChangePassword = (
  onSuccess?: () => void,
  onError?: (error: any) => void
) => {
  const changePasswordFn = async ({
    oldPassword,
    newPassword,
  }: ChangePasswordRequest) => {
    await axiosApp.post("/user/change-password", {
      oldPassword,
      newPassword,
    });
  };

  return useMutation<void, any, ChangePasswordRequest>({
    mutationFn: changePasswordFn,
    onSuccess,
    onError,
  });
};
