import type { Movie } from "../../api/types";

import "./heroItem.scss";

export type HeroItemProps = {
  movie: Movie;
};

export const HeroItem = ({ movie }: HeroItemProps) => {
  const coverImage =
    movie.photos.find((img) => img.isCoverImage)?.url ||
    movie.photos[0]?.url ||
    "";
  const genre = movie.movieGenres[0]?.genre.name || "Unknown Genre";
  const shortSynopsis = movie.synopsis;

  return (
    <div key={movie.id} className="carousel-item">
      <img src={coverImage} alt={movie.title} className="movie-image" />
      <div className="overlay">
        <div className="genre">
          <p className="genre-text">{genre}</p>
        </div>
        <h2 className="title">{movie.title}</h2>
        <h6 className="synopsis">{shortSynopsis}</h6>
      </div>
    </div>
  );
};
