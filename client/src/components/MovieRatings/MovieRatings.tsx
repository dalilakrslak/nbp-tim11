import { FaRegStar } from "react-icons/fa";

import type { MovieRating } from "../../api";

import "./movieRatings.scss";

export type MovieRatingsProps = {
  ratings?: MovieRating[];
};

export const MovieRatings = ({ ratings = [] }: MovieRatingsProps) => {
  const hasRatings = ratings.length > 0;

  return (
    <div className="movie-ratings-container">
      <div className="movie-ratings-title">
        <p className="separator">|</p>
        <p>Ratings</p>
      </div>

      {hasRatings ? (
        <div className="movie-ratings-list">
          {ratings.map((rating) => (
            <div key={rating.id} className="movie-rating-box">
              <FaRegStar className="rating-star" />
              <div className="rating-info">
                <p className="rating-value">{rating.rating}</p>
                <p className="rating-provider">{rating.name}</p>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <p className="no-ratings-text">No ratings available.</p>
      )}
    </div>
  );
};
