import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import { authApi } from "../api";
import { useAuth } from "../contexts/";

function isAuthError(obj: unknown): obj is AuthError {
  return (
    typeof obj === "object" &&
    obj !== null &&
    typeof (obj as AuthError)?.message === "string"
  );
}

function isAxiosError(
  error: unknown
): error is { response?: { status: number } } {
  return (
    typeof error === "object" &&
    error !== null &&
    "response" in error &&
    typeof (error as any).response === "object" &&
    (typeof (error as any).response.status === "number" ||
      typeof (error as any).response.status === "undefined")
  );
}

export type AuthError = {
  status?: number;
  message: string;
  email?: string;
};

export const useLogin = () => {
  const { login } = useAuth();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({
      email,
      password,
    }: {
      email: string;
      password: string;
    }) => {
      try {
        const data = await authApi.login(email, password);
        return data;
      } catch (error: unknown) {
        if (isAuthError(error)) {
          if (error.status === 401) {
            throw new Error("Invalid email or password");
          } else if (error.status === 404) {
            throw new Error("Account not found. Please sign up first");
          } else {
            throw new Error(error.message || "Login failed. Please try again");
          }
        }
      }
    },
    onSuccess: (data) => {
      login(data.accessToken, data.refreshToken);
      queryClient.invalidateQueries({ queryKey: ["currentUser"] });
    },
  });
};

export const useRegister = () => {
  const { login } = useAuth();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({
      email,
      password,
    }: {
      email: string;
      password: string;
    }) => {
      try {
        const data = await authApi.register(email, password);
        return data;
      } catch (error: unknown) {
        if (isAuthError(error)) {
          if (error.email) {
            throw new Error("Email is already in use.");
          } else {
            throw new Error(
              error.message || "Registration failed. Please try again"
            );
          }
        }
      }
    },
    onSuccess: (data) => {
      login(data.accessToken, data.refreshToken);
      queryClient.invalidateQueries({ queryKey: ["currentUser"] });
    },
  });
};

export const useLogout = () => {
  const { logout } = useAuth();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => authApi.logout(),
    onSuccess: () => {
      logout();
      queryClient.setQueryData(["currentUser"], null);
    },
    onError: () => {
      logout();
      queryClient.setQueryData(["currentUser"], null);
    },
  });
};

export const useCurrentUser = () => {
  const { logout, getToken } = useAuth();

  return useQuery({
    queryKey: ["currentUser"],
    queryFn: async () => {
      try {
        return await authApi.getCurrentUser();
      } catch (error: unknown) {
        if (isAxiosError(error) && error.response?.status === 401) {
          logout();
        }
        throw error;
      }
    },
    enabled: !!getToken(),
    retry: false,
    staleTime: 5 * 60 * 1000,
  });
};
