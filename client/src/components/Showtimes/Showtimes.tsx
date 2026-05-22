import type { Screening } from "../../api/types";

import "./showtimes.scss";

const formatTime = (date: string | Date): string => {
  const time = new Date(date);
  return time.toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  });
};

export type ShowtimesProps = {
  showtimes: Screening[];
  selectedScreening: Screening | null;
  onScreeningSelect: (screening: Screening) => void;
  isLoading?: boolean;
};

export const Showtimes = ({
  showtimes,
  selectedScreening,
  onScreeningSelect,
  isLoading,
}: ShowtimesProps) => {
  if (isLoading) {
    return (
      <div className="showtimes">
        <h6 className="showtimes-title">Showtimes</h6>
        <div className="showtimes-container">Loading...</div>
      </div>
    );
  }

  return (
    <div className="showtimes">
      <h6 className="showtimes-title">Showtimes</h6>
      <div className="showtimes-container">
        {showtimes.length > 0 ? (
          showtimes.map((showtime) => (
            <div
              key={showtime.id}
              className={`showtime ${
                selectedScreening?.id === showtime.id ? "selected" : ""
              }`}
              onClick={() => onScreeningSelect(showtime)}
            >
              <h6 className="start-time">{formatTime(showtime.startTime)}</h6>
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
