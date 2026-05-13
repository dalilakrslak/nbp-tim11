import "./dateBox.scss";

export type DateBoxProps = {
  date: string;
  isSelected: boolean;
  onSelect: () => void;
  className?: string;
};

export const DateBox = ({
  date,
  isSelected,
  onSelect,
  className,
}: DateBoxProps) => {
  const dateObj = new Date(date);
  const formattedDate = dateObj.toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
  });

  const today = new Date();
  const isToday = dateObj.toDateString() === today.toDateString();

  const day = isToday
    ? "Today"
    : dateObj.toLocaleDateString("en-US", { weekday: "short" });

  return (
    <div className={`datebox-container ${className}`}>
      <div
        className={`date-block ${isSelected ? "selected" : ""}`}
        onClick={onSelect}
      >
        <p className="date">{formattedDate}</p>
        <p className="day">{day}</p>
      </div>
    </div>
  );
};
