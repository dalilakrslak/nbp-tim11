import * as React from "react";

import { getCurrentlyShowingMovies } from "../api/movies";
import type { Movie, MovieFiltersWithPageable } from "../api/types";
import { clearEmptyMovieFilters } from "../utils";

export type CurrentlyShowingResponse = {
  content: Movie[];
  totalElements: number;
};

export const useCurrentlyShowingMovies = ({
  page = 0,
  size = 4,
  title,
  genres,
  city,
  cinema,
  projectionTime,
  date,
}: MovieFiltersWithPageable = {}) => {
  const [data, setData] = React.useState<CurrentlyShowingResponse | null>(null);
  const [isLoading, setIsLoading] = React.useState<boolean>(true);

  React.useEffect(() => {
    function fetchCurrentlyShowingMovies() {
      setIsLoading(true);

      const pageable = clearEmptyMovieFilters(
        { page, size },
        { title, genres, city, cinema, projectionTime, date }
      );

      getCurrentlyShowingMovies(pageable)
        .then((response) => {
          const formattedResponse = {
            content: response?.content || [],
            totalElements:
              response?.page?.totalElements ??
              Math.max((response?.content || []).length, (page + 1) * size),
          };
          setData(formattedResponse);
        })
        .catch((error) => {
          console.error("Failed to fetch currently showing movies:", error);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }

    fetchCurrentlyShowingMovies();
  }, [page, size, title, genres, city, cinema, projectionTime, date]);

  return { data, isLoading };
};
