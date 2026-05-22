import { Card } from "../Card";
import type { Movie } from "../../api/types";
import { Showtimes } from "../Showtimes";
import { Badge } from "antd";
import { MovieRatingLabels, getReleaseDateText } from "../../utils";
import { Link } from "react-router-dom";
import { Loading } from "../Loading";
import "./moviesList.scss";

export type MoviesListProps = {
  movies?: Movie[];
  selectedDate?: string;
  listLayout?: "currentlyShowing" | "upcoming";
  cardLayout?: "horizontal" | "vertical";
  isLoading?: boolean;
};

export const MoviesList = ({
  movies,
  selectedDate,
  listLayout = "currentlyShowing",
  cardLayout = "vertical",
  isLoading = false,
}: MoviesListProps) => {
  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className={`movies-list-container ${listLayout}`}>
      {movies?.map((movie) => {
        const coverPhoto =
          movie.photos.find((photo) => photo.isCoverImage) || movie.photos[0];
        let descriptionContent;
        let extraContent;
        if (listLayout === "currentlyShowing") {
          descriptionContent = (
            <div className="card-horizontal-description">
              <div className="card-rating-language-duration">
                <div className="card-pg-rating">
                  {MovieRatingLabels[movie.pgRating] ?? movie.pgRating}
                </div>
                <span className="separator">|</span>
                <div className="card-language">{movie.language}</div>
                <span className="separator">|</span>
                <div className="card-duration">{movie.duration} Min</div>
              </div>
              <div className="card-genres">
                {movie.movieGenres.map((genre) => (
                  <div key={genre.id} className="card-genre">
                    {genre.genre.name}
                  </div>
                ))}
              </div>
            </div>
          );
          const filteredScreenings = selectedDate
            ? movie.screenings.filter((screening) => {
                const screeningDate = new Date(screening.startTime);
                const dateString = screeningDate.toISOString().split("T")[0];
                return dateString === selectedDate;
              })
            : movie.screenings;
          extraContent = (
            <div className="card-showtimes">
              <Showtimes
                showtimes={filteredScreenings}
                selectedScreening={null}
                onScreeningSelect={() => {}}
              />
            </div>
          );
        } else {
          descriptionContent = (
            <div className="card-duration-genre">
              <p className="card-duration">{movie.duration} MIN</p>
              <p className="separator">|</p>
              <p className="card-genre">{movie.movieGenres[0].genre.name}</p>
            </div>
          );
        }
        const card = (
          <Link
            to={`/movies/${movie.id}`}
            className="card-link-wrapper"
            key={movie.id}
            onClick={() => {
              window.scrollTo(0, 0);
            }}
          >
            <Card
              photoUrl={coverPhoto.url}
              title={movie.title}
              description={descriptionContent}
              extra={extraContent}
              layout={cardLayout}
            />
          </Link>
        );

        if (listLayout === "upcoming") {
          const releaseDateText = getReleaseDateText(movie.startDate);
          return (
            <Badge.Ribbon
              text={releaseDateText}
              color="#B22222"
              className="movie-ribbon-badge"
              key={`badge-${movie.id}`}
            >
              {card}
            </Badge.Ribbon>
          );
        }
        return card;
      })}
    </div>
  );
};
