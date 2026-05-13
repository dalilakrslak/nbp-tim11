import { useQuery } from "@tanstack/react-query";

import { axiosApp } from "../api/axiosApp";
import type { Location } from "../api/types";

export const getAllLocations = async (): Promise<Location[]> => {
  const response = await axiosApp.get("/locations/all");
  return response.data;
};

export const useAllLocations = () => {
  return useQuery({
    queryKey: ["locations"],
    queryFn: getAllLocations,
  });
};
