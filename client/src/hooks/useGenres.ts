import { useQuery } from "@tanstack/react-query";

import { axiosApp } from "../api/axiosApp";
import type { Genre } from "../api/types";

export const getGenres = async (): Promise<Genre[]> => {
  const response = await axiosApp.get("/genres/all");
  return response.data;
};

export const useGenres = () => {
  return useQuery({
    queryKey: ["genres"],
    queryFn: getGenres,
  });
};
