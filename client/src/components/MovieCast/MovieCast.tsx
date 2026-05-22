import type { Actor } from "../../api";

import "./movieCast.scss";

export type MovieCastProps = {
  actors?: Actor[];
};

export const MovieCast = ({ actors = [] }: MovieCastProps) => {
  const hasActors = actors.length > 0;

  return (
    <div className="movie-cast-container">
      <div className="movie-cast-title">
        <p className="separator">|</p>
        <p>Cast</p>
      </div>
      {hasActors ? (
        <div className="actors-container">
          {actors.map((actor) => (
            <div key={actor.id} className="actor">
              <p className="actor-name">{`${actor.firstName} ${actor.lastName}`}</p>
              <p className="actor-role">{actor.role}</p>
            </div>
          ))}
        </div>
      ) : (
        <div className="no-actors-message">No movie cast available.</div>
      )}
    </div>
  );
};
