import { FaFilm } from "react-icons/fa6";
import { Link } from "react-router-dom";

import "./moviePagesEmpty.scss";

type MoviesNotFoundProps = {
  type: "currently-showing" | "upcoming";
  message?: string;
};

export const MoviePagesEmpty = ({ type }: MoviesNotFoundProps) => {
  return (
    <div className="empty-page-container">
      <div className="empty-page-content">
        <FaFilm className="empty-page-icon" />
        <p className="empty-page-title">No movies available</p>
        <p className="empty-page-description">
          We are working on updating our schedule for upcoming movies. Stay
          tuned for amazing movie experience or explore our other exciting
          cinema features in the meantime!
        </p>
        {type === "currently-showing" && (
          <Link to="/upcoming" className="empty-page-link">
            Explore Upcoming Movies
          </Link>
        )}
      </div>
    </div>
  );
};
