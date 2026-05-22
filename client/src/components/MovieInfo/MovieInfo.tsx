import { MovieRatingLabels } from "../../utils";
import type { Movie } from "../../api";

import "./movieInfo.scss";

export type MovieInfoProps = {
  movie: Movie;
};

export const MovieInfo = ({ movie }: MovieInfoProps) => {
  const {
    title,
    pgRating,
    movieGenres,
    startDate,
    endDate,
    language,
    duration,
    synopsis,
    director,
    movieWriters,
  } = movie;

  const formattedStartDate = new Date(startDate).toLocaleDateString("en-GB");
  const formattedEndDate = new Date(endDate).toLocaleDateString("en-GB");

  return (
    <div className="movie-info-container">
      <div className="movie-info-title">{title}</div>
      <div className="base-info">
        <p className="pg-rating">{MovieRatingLabels[pgRating]}</p>
        <p className="separator">|</p>
        <p className="language">{language}</p>
        <p className="separator">|</p>
        <p className="duration">{duration} Min</p>
        <p className="separator">|</p>
        <p className="projection-date">
          Projection date: {formattedStartDate} - {formattedEndDate}
        </p>
      </div>
      <div className="genre-container">
        {movieGenres.map(({ id, genre }) => (
          <div key={id} className="genre">
            <p className="genre-name">{genre.name}</p>
          </div>
        ))}
      </div>
      <div className="movie-info-synopsis">
        <p>{synopsis}</p>
      </div>
      <div className="movie-info-director-container">
        <p className="creator-title">Director:</p>
        <p className="director-name">{director}</p>
      </div>
      <div className="movie-info-writers-container">
        <p className="creator-title">Writers:</p>
        <p className="writer-name">
          {movieWriters
            .map((writerObj) => {
              const writer = writerObj.writer;
              return `${writer.firstName} ${writer.lastName}`;
            })
            .join(", ")}
        </p>
      </div>
    </div>
  );
};
