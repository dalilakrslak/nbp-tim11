import { useQuery } from "@tanstack/react-query";

import {
  getMovieCatalogReport,
  getScreeningAvailabilityReport,
  getTicketSalesReport,
  type MovieCatalogReportRow,
  type ScreeningAvailabilityReportRow,
  type TicketSalesReportRow,
} from "../api/reports";

export const useMovieCatalogReport = () =>
  useQuery<MovieCatalogReportRow[]>({
    queryKey: ["reports", "movie-catalog"],
    queryFn: getMovieCatalogReport,
  });

export const useScreeningAvailabilityReport = () =>
  useQuery<ScreeningAvailabilityReportRow[]>({
    queryKey: ["reports", "screening-availability"],
    queryFn: getScreeningAvailabilityReport,
  });

export const useTicketSalesReport = () =>
  useQuery<TicketSalesReportRow[]>({
    queryKey: ["reports", "ticket-sales"],
    queryFn: getTicketSalesReport,
  });
