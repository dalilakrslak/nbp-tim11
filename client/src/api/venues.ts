import { axiosApp } from "./axiosApp";
import type { Venue, VenueRequest, PageResponse } from "./types";
import type { Pageable } from "../utils";

export const getVenues = async (
  pageable: Pageable
): Promise<PageResponse<Venue>> => {
  const response = await axiosApp.get("/venues", {
    params: { ...pageable },
  });

  return response.data;
};

export const createVenue = async (
  payload: VenueRequest
): Promise<Venue> => {
  const response = await axiosApp.post("/venues", payload);
  return response.data;
};

export const updateVenue = async (
  venueId: string,
  payload: VenueRequest
): Promise<Venue> => {
  const response = await axiosApp.put(`/venues/${venueId}`, payload);
  return response.data;
};

export const deleteVenue = async (venueId: string): Promise<void> => {
  await axiosApp.delete(`/venues/${venueId}`);
};
