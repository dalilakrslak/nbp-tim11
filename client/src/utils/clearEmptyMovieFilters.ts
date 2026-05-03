import type { Pageable } from "./pageable";
import type { MovieFilters, MovieFiltersWithPageable } from "../api/types";

export const clearEmptyMovieFilters = (
  pageable: Pageable,
  filters: MovieFilters
): MovieFiltersWithPageable => {
  const validFilters: MovieFiltersWithPageable = { ...pageable };

  if (filters.title) validFilters.title = filters.title;
  if (filters.genres && filters.genres.length > 0)
    validFilters.genres = filters.genres;
  if (filters.city) validFilters.city = filters.city;
  if (filters.cinema) validFilters.cinema = filters.cinema;
  if (filters.projectionTime)
    validFilters.projectionTime = filters.projectionTime;
  if (filters.date) validFilters.date = filters.date;

  return validFilters;
};
