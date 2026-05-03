import * as React from "react";

import { getUpcomingMovies } from "../api";
import type { Movie } from "../api";
import type { Pageable } from "../utils";
import { clearEmptyMovieFilters } from "../utils";

export type UpcomingResponse = {
  content: Movie[];
  totalElements: number;
};

export type UseUpcomingProps = Pageable & {
  title?: string;
  genres?: string[];
  city?: string;
  cinema?: string;
};

export const useUpcomingMovies = ({
  page = 0,
  size = 4,
  title,
  genres,
  city,
  cinema,
}: UseUpcomingProps = {}) => {
  const [data, setData] = React.useState<UpcomingResponse | null>(null);
  const [isLoading, setIsLoading] = React.useState<boolean>(true);

  React.useEffect(() => {
    function fetchUpcomingMovies() {
      setIsLoading(true);

      const pageable = clearEmptyMovieFilters(
        { page, size },
        { title, genres, city, cinema }
      );

      getUpcomingMovies(pageable)
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
          console.error("Failed to fetch upcoming movies:", error);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }

    fetchUpcomingMovies();
  }, [page, size, title, genres, city, cinema]);

  return { data, isLoading };
};
