import { DateBox } from "../DateBox";

import "./dateList.scss";

export type DateListProps = {
  onDateSelect: (selectedDate: string) => void;
  selectedDate: string;
  dateBoxClassName?: string;
};

const generateDateArray = () => {
  const today = new Date();
  return Array.from({ length: 10 }, (_, i) => {
    const date = new Date(today);
    date.setDate(today.getDate() + i);
    return date.toISOString().split("T")[0];
  });
};

export const DateList = ({
  selectedDate,
  onDateSelect,
  dateBoxClassName,
}: DateListProps) => {
  const dates = generateDateArray();

  return (
    <div className="datelist-container">
      {dates.map((date) => (
        <DateBox
          key={date}
          date={date}
          isSelected={selectedDate === date}
          onSelect={() => onDateSelect(date)}
          className={dateBoxClassName}
        />
      ))}
    </div>
  );
};
