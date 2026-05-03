import { axiosApp } from "./axiosApp";
import axios, { InternalAxiosRequestConfig } from "axios";

interface ExtendedAxiosRequestConfig extends InternalAxiosRequestConfig {
  _retry?: boolean;
}

export function isExtendedAxiosRequestConfig(
  config: any
): config is ExtendedAxiosRequestConfig {
  return config && typeof config === "object";
}

const axiosRefresh = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

axiosApp.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error: unknown) => {
    return Promise.reject(error);
  }
);

axiosApp.interceptors.response.use(
  (response) => response,
  async (error: unknown) => {
    if (axios.isAxiosError(error)) {
      const is401Error = error.response?.status === 401;
      const isNetworkError = !error.response && error.code === "ERR_NETWORK";

      if (is401Error || isNetworkError) {
        if (!isExtendedAxiosRequestConfig(error.config)) {
          return Promise.reject(error);
        }

        const originalRequest = error.config;

        const refreshToken = localStorage.getItem("refreshToken");
        const hasValidTokens =
          localStorage.getItem("accessToken") && refreshToken;

        if (hasValidTokens && !originalRequest._retry) {
          originalRequest._retry = true;

          try {
            const response = await axiosRefresh.post("/auth/refresh-token", {
              refreshToken: refreshToken,
            });

            const { accessToken } = response.data;
            localStorage.setItem("accessToken", accessToken);

            if (originalRequest.headers) {
              originalRequest.headers.Authorization = `Bearer ${accessToken}`;
            }

            return axiosApp(originalRequest);
          } catch (refreshError) {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            window.location.href = "/";
            return Promise.reject(refreshError);
          }
        }
      }
    }

    return Promise.reject(error);
  }
);

export const authApi = {
  login: async (email: string, password: string) => {
    try {
      const response = await axiosApp.post("/auth/login", {
        email,
        password,
      });
      return response.data;
    } catch (error: unknown) {
      if (axios.isAxiosError(error)) {
        if (error.response) {
          throw {
            status: error.response.status,
            message: error.response.data?.message || "Login failed",
          };
        } else if (error.request) {
          throw {
            message:
              "Unable to connect to server. Please check your connection or try again later.",
          };
        }
      }

      throw {
        message: "An unexpected error occurred. Please try again.",
      };
    }
  },

  register: async (email: string, password: string) => {
    try {
      const response = await axiosApp.post("/auth/register", {
        email,
        password,
      });
      return response.data;
    } catch (error: unknown) {
      if (axios.isAxiosError(error)) {
        if (error.response) {
          throw {
            status: error.response.status,
            message: error.response.data?.message || "Registration failed",
            ...error.response.data,
          };
        } else if (error.request) {
          throw {
            message:
              "Unable to connect to server. Please check your connection or try again later.",
          };
        }
      }
      throw {
        message: "An unexpected error occurred. Please try again.",
      };
    }
  },

  getCurrentUser: async () => {
    try {
      const response = await axiosApp.get("/user/me");
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        if (error.response) {
          console.error(
            "Error fetching current user:",
            error.response.status,
            error.response.data
          );
        } else if (error.request) {
          console.error("Network error fetching current user:", error.message);
        }
      } else {
        console.error("Unexpected error fetching current user:", error);
      }
      throw error;
    }
  },

  logout: async () => {
    const refreshToken = localStorage.getItem("refreshToken");
    if (refreshToken) {
      try {
        await axiosApp.post("/auth/logout", {
          refreshToken: refreshToken,
        });
      } catch (error: unknown) {
        console.error("Logout error:", error);
      } finally {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
      }
    }
  },
};
