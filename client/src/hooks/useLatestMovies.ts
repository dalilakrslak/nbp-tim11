import * as React from "react";

import { getLatestMovies } from "../api";
import type { Movie } from "../api";

export type LatestResponse = {
  content: Movie[];
};

export const useLatestMovies = () => {
  const [data, setData] = React.useState<LatestResponse | null>(null);

  React.useEffect(() => {
    function fetchLatestMovies() {
      getLatestMovies()
        .then((response) => {
          const formattedResponse = {
            content: response?.content || [],
          };
          setData(formattedResponse);
        })
        .catch((error) => {
          console.error("Failed to fetch latest movies:", error);
        });
    }

    fetchLatestMovies();
  }, []);

  return { data };
};
