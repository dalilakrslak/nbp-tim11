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
