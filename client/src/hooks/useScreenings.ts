import { useQuery } from "@tanstack/react-query";

import { getScreenings, getScreeningsByMovieId } from "../api";
import type { Screening, ScreeningFiltersWithPageable } from "../api/types";
import type { ResponseType } from "../utils";

export const useScreenings = (filters: ScreeningFiltersWithPageable) => {
  return useQuery<ResponseType<Screening>>({
    queryKey: ["screenings", filters],
    queryFn: () => getScreenings(filters),
    enabled: !!filters,
  });
};

export const useScreeningsByMovieId = (movieId: string) => {
  return useQuery<Screening>({
    queryKey: ["screenings", movieId],
    queryFn: () => getScreeningsByMovieId(movieId),
    enabled: !!movieId,
  });
};
