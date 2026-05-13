import { useQuery } from "@tanstack/react-query";

import { axiosApp } from "../api/axiosApp";
import type { Venue } from "../api/types";

export const getAllVenues = async (): Promise<Venue[]> => {
  const response = await axiosApp.get("/venues/all");
  return response.data;
};

export const useAllVenues = () => {
  return useQuery({
    queryKey: ["venues"],
    queryFn: getAllVenues,
  });
};
