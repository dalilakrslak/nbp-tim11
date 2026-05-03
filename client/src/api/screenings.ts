import { axiosApp } from "./axiosApp";
import type { Screening, ScreeningFiltersWithPageable } from "./types";
import type { ResponseType } from "../utils";

export const getScreenings = async (
  filters: ScreeningFiltersWithPageable
): Promise<ResponseType<Screening>> => {
  const response = await axiosApp.get("/screenings", {
    params: { ...filters },
  });

  return response.data;
};

export const getScreeningById = async (
  screeningId: string
): Promise<Screening> => {
  const response = await axiosApp.get(`/screenings/${screeningId}`);
  return response.data;
};

export const getScreeningsByMovieId = async (
  movieId: string
): Promise<Screening> => {
  const response = await axiosApp.get(`/movies/${movieId}/screenings/`);
  return response.data;
};
