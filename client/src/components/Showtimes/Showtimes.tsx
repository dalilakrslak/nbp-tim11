import "./showtimes.scss";

import { Movie } from "../../api/types";

const formatTime = (date: string | Date): string => {
  const time = new Date(date);
  return time.toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  });
};

export type ShowtimesProps = {
  showtimes: Movie["screenings"];
};

export const Showtimes = ({ showtimes }: ShowtimesProps) => {
  return (
    <div className="showtimes">
      <h6 className="showtimes-title">Showtimes</h6>
      <div className="showtimes-container">
        {showtimes.length > 0 ? (
          showtimes.map((showtime, index) => (
            <div key={index} className="showtime">
              <h6>{formatTime(showtime.startTime)}</h6>
            </div>
          ))
        ) : (
          <div className="no-showtimes">
            No showtimes available for this date
          </div>
        )}
      </div>
    </div>
  );
};
