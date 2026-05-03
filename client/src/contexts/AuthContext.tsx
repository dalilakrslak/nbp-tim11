import React, { createContext, useContext } from "react";

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export type AuthProviderProps = {
  children: React.ReactNode;
};

export type AuthContextType = {
  login: (accessToken: string, refreshToken: string) => void;
  logout: () => void;
  getToken: () => string | null;
};

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const login = (accessToken: string, refreshToken: string) => {
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  };

  const getToken = () => {
    return localStorage.getItem("accessToken");
  };

  return (
    <AuthContext.Provider value={{ login, logout, getToken }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);

  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return context;
};
