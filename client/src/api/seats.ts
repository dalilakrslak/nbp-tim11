import { axiosApp } from "./axiosApp";

import type { SeatAvailability } from "./types";

export const getSeatsByScreeningId = async (
  screeningId: string
): Promise<SeatAvailability[]> => {
  const response = await axiosApp.get(`/screenings/${screeningId}/seats`);
  return response.data;
};
