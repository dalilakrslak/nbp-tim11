import { useQuery } from "@tanstack/react-query";

import { getMovieRatings } from "../api";
import type { MovieRating } from "../api";

export const useMovieRatings = (movieId: string) => {
  return useQuery<MovieRating[], Error>({
    queryKey: ["movieRatings", movieId],
    queryFn: () => getMovieRatings(movieId),
    enabled: !!movieId,
  });
};
