import { useParams } from "react-router-dom";

import {
  Error,
  Loading,
  MovieMedia,
  MovieInfo,
  MovieCast,
  TicketSection,
  SeeAlsoList,
  MovieRatings,
} from "../../components";
import { useMovieById, useMovieRatings } from "../../hooks";

import "./movieDetails.scss";

const getErrorType = (error: any) => {
  if (!error) return "generic";

  const status = error?.response?.status || error?.status;
  switch (status) {
    case 404:
      return "notFound";
    case 401:
    case 403:
      return "unauthorized";
    case 500:
    case 502:
    case 503:
      return "server";
    default:
      return error?.message?.includes("fetch") ||
        error?.message?.includes("network")
        ? "connection"
        : "generic";
  }
};

const shouldShowRetry = (error: any) => {
  const status = error?.response?.status || error?.status;
  return status !== 404 && status !== 401;
};

export const MovieDetails = () => {
  const { movieId } = useParams<{ movieId: string }>();

  const {
    data: movie,
    isLoading: isMovieLoading,
    isError: isMovieError,
    error: movieError,
    refetch: refetchMovie,
  } = useMovieById(movieId!);

  const {
    data: ratings,
    isLoading: areRatingsLoading,
    isError: areRatingsError,
    error: ratingsError,
    refetch: refetchRatings,
  } = useMovieRatings(movieId!);

  if (isMovieLoading) return <Loading />;

  if (isMovieError || !movie) {
    return (
      <Error
        type={getErrorType(movieError)}
        message={movieError?.message}
        onRetry={shouldShowRetry(movieError) ? refetchMovie : undefined}
      />
    );
  }

  return (
    <div className="movie-details-container">
      <div className="movie-details-content">
        <div className="movie-details-title">Movie Details</div>
        <MovieMedia trailerUrl={movie?.trailerUrl} photos={movie?.photos} />
        <div className="movie-details-info-ticket-container">
          <div className="movie-details-info">
            <MovieInfo movie={movie} />
            <MovieCast
              actors={movie?.roles?.map((role) => ({
                id: role.actor.id,
                firstName: role.actor.firstName,
                lastName: role.actor.lastName,
                role: role.name,
              }))}
            />
            {!areRatingsLoading && !areRatingsError && (
              <MovieRatings ratings={ratings} />
            )}
            {areRatingsError && (
              <div className="ratings-error">
                <Error
                  type={getErrorType(ratingsError)}
                  message="Failed to load movie ratings"
                  onRetry={
                    shouldShowRetry(ratingsError) ? refetchRatings : undefined
                  }
                  showRetry={true}
                />
              </div>
            )}
          </div>
          <TicketSection movie={movie} />
        </div>
      </div>
      <div className="see-also-container">
        <SeeAlsoList movieId={movie?.id} />
      </div>
    </div>
  );
};
