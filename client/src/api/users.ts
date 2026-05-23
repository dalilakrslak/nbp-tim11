import { axiosApp } from "./axiosApp";
import type { Pageable } from "../utils";
import type { PageResponse } from "./types";

export type User = {
  id: string;
  email: string;
  createdAt: string;
};

export const getUsers = async (
  pageable: Pageable
): Promise<PageResponse<User>> => {
  const response = await axiosApp.get("/users", {
    params: { ...pageable },
  });
  return response.data;
};

export const createUser = async (payload: {
  email: string;
  password: string;
}): Promise<User> => {
  const response = await axiosApp.post("/users", payload);
  return response.data;
};

export const deleteUser = async (id: string): Promise<void> => {
  await axiosApp.delete(`/users/${id}`);
};

export const uploadProfilePicture = async (file: File): Promise<void> => {
  const formData = new FormData();
  formData.append("file", file);

  await axiosApp.post("/user/me/profile-picture", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

export const getProfilePictureBlob = async (): Promise<Blob | null> => {
  try {
    const response = await axiosApp.get("/user/me/profile-picture", {
      responseType: "blob",
    });
    return response.data;
  } catch (error: any) {
    if (error?.response?.status === 404) return null;
    throw error;
  }
};

export const deleteProfilePicture = async (): Promise<void> => {
  await axiosApp.delete("/user/me/profile-picture");
};