import { axiosApp } from "./axiosApp";

export type MovieCatalogReportRow = {
  movieId: string;
  title: string;
  synopsis: string | null;
  duration: number | null;
  language: string | null;
  pgRating: string | null;
  director: string | null;
  trailerUrl: string | null;
  startDate: string | null;
  endDate: string | null;
  coverImageUrl: string | null;
  genres: string | null;
  castMembers: string | null;
  writers: string | null;
};

export type ScreeningAvailabilityReportRow = {
  screeningId: string;
  movieTitle: string;
  duration: number | null;
  pgRating: string | null;
  startTime: string;
  estimatedEndTime: string | null;
  hallName: string;
  venueName: string;
  street: string | null;
  city: string | null;
  country: string | null;
  totalSeats: number | null;
  bookedSeats: number | null;
  availableSeats: number | null;
  occupancyPercentage: number | string | null;
};

export type TicketSalesReportRow = {
  saleDate: string;
  venueName: string;
  hallName: string;
  movieTitle: string;
  status: string;
  ticketCount: number | null;
  bookedSeatCount: number | null;
  totalRevenue: number | string | null;
  averageTicketPrice: number | string | null;
  uniqueCustomers: number | null;
};

export const getMovieCatalogReport = async (): Promise<
  MovieCatalogReportRow[]
> => {
  const response = await axiosApp.get("/reports/movie-catalog");
  return response.data;
};

export const getScreeningAvailabilityReport = async (): Promise<
  ScreeningAvailabilityReportRow[]
> => {
  const response = await axiosApp.get("/reports/screening-availability");
  return response.data;
};

export const getTicketSalesReport = async (): Promise<TicketSalesReportRow[]> => {
  const response = await axiosApp.get("/reports/ticket-sales");
  return response.data;
};
