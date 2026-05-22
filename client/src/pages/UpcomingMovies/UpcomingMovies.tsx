import { useState } from "react";

import { MoviesList } from "../../components";
import { useUpcomingMovies } from "../../hooks";
import { MoviesFilter, MoviePagesEmpty, Loading } from "../../components";

const PAGE_SIZE = 4;

export type UpcomingMoviesFilters = {
  title?: string;
  genres?: string[];
  city?: string;
  cinema?: string;
};

export const UpcomingMovies = () => {
  const [filters, setFilters] = useState<UpcomingMoviesFilters>({
    title: "",
    genres: [],
    city: "",
    cinema: "",
  });

  const [pageSize, setPageSize] = useState<number>(PAGE_SIZE);

  const { data, isLoading } = useUpcomingMovies({
    page: 0,
    size: pageSize,
    title: filters?.title,
    genres: filters?.genres,
    city: filters?.city,
    cinema: filters?.cinema,
  });

  const movies = data?.content || [];
  const totalMovies = data?.totalElements || 0;

  const handleLoadMore = () => {
    setPageSize((prevSize) => prevSize + PAGE_SIZE);
  };

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
        Upcoming Movies {totalMovies > 0 ? `(${totalMovies})` : ""}
      </h4>

      <MoviesFilter
        filters={filters}
        onFilterChange={handleFilterChange}
        isCurrentlyShowing={false}
      />

      <div className="currently-showing-movies-list-container">
        {isLoading ? (
          <Loading />
        ) : showEmptyState ? (
          <MoviePagesEmpty type="upcoming" />
        ) : (
          <>
            <MoviesList
              movies={movies}
              listLayout="upcoming"
              cardLayout="vertical"
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
