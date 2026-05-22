import { useQuery } from "@tanstack/react-query";

import { getSimilarMovies, Movie } from "../api";
import type { Pageable, ResponseType } from "../utils";

export const useSimilarMovies = (movieId: string, pageable: Pageable) => {
  return useQuery<ResponseType<Movie>>({
    queryKey: ["similar", movieId, pageable],
    queryFn: () => getSimilarMovies(movieId, pageable),
    enabled: !!movieId,
  });
};
