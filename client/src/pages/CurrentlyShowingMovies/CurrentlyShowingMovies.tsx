import { useMemo, useState } from "react";

import {
  MoviesFilter,
  MoviesList,
  MoviePagesEmpty,
  Loading,
} from "../../components";
import { useCurrentlyShowingMovies } from "../../hooks";
import type { UpcomingMoviesFilters } from "../UpcomingMovies";

import "./currentlyShowingMovies.scss";

const PAGE_SIZE = 4;

export type CurrentlyShowingMoviesFilters = UpcomingMoviesFilters & {
  projectionTime: string;
  date: string;
};

export const CurrentlyShowingMovies = () => {
  const [filters, setFilters] = useState<CurrentlyShowingMoviesFilters>({
    title: "",
    genres: [],
    city: "",
    cinema: "",
    projectionTime: "",
    date: new Date().toISOString().split("T")[0],
  });

  const [pageSize, setPageSize] = useState<number>(PAGE_SIZE);

  const { data, isLoading } = useCurrentlyShowingMovies({
    page: 0,
    size: pageSize,
    ...filters,
  });

  const movies = data?.content || [];
  const totalMovies = data?.totalElements || 0;

  const handleLoadMore = () => {
    setPageSize((prevSize) => prevSize + PAGE_SIZE);
  };

  const uniqueProjections = useMemo(() => {
    const projectionsMap = new Map();

    movies.forEach((movie) => {
      movie.screenings.forEach((screening) => {
        const originalTime = screening.startTime;
        const date = new Date(originalTime);
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        const displayTime = `${hours}:${minutes}`;

        projectionsMap.set(originalTime, {
          value: originalTime,
          displayValue: displayTime,
        });
      });
    });

    return Array.from(projectionsMap.values());
  }, [movies]);

  const handleFilterChange = (filterType: string, value: string | string[]) => {
    setFilters((prevFilters) => ({
      ...prevFilters,
      [filterType]: value,
    }));
    setPageSize(PAGE_SIZE);
  };

  const showLoadMore = movies.length > 0 && movies.length < totalMovies;
  const showEmptyState = !isLoading && movies.length === 0;

  return (
    <div className="currently-showing-movies">
      <h4 className="currently-showing-movies-title">
        Currently Showing {totalMovies > 0 ? `(${totalMovies})` : ""}
      </h4>

      <MoviesFilter
        filters={filters}
        onFilterChange={handleFilterChange}
        projectionTimes={uniqueProjections}
        isCurrentlyShowing
      />

      <p className="reminder">
        Quick reminder that our cinema schedule is on a ten-day update cycle.
      </p>

      <div className="currently-showing-movies-list-container">
        {isLoading ? (
          <Loading />
        ) : showEmptyState ? (
          <MoviePagesEmpty type="currently-showing" />
        ) : (
          <>
            <MoviesList
              movies={movies}
              selectedDate={filters.date}
              cardLayout="horizontal"
            />

            {showLoadMore && (
              <div className="load-more-container">
                <p className="load-more" onClick={handleLoadMore}>
                  Load More
                </p>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};
