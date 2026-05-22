import { useQuery } from "@tanstack/react-query";

import { Movie } from "../api";
import { getMovieById } from "../api/movies";

export const useMovieById = (movieId: string) => {
  return useQuery<Movie>({
    queryKey: ["movie", movieId],
    queryFn: () => getMovieById(movieId),
    enabled: !!movieId,
  });
};
