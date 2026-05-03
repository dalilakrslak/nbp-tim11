import { format, isAfter, isBefore, addDays } from "date-fns";

export const getReleaseDateText = (releaseDate: string | Date): string => {
  const releaseDateObj =
    typeof releaseDate === "string" ? new Date(releaseDate) : releaseDate;
  const today = new Date();
  const oneWeekFromNow = addDays(today, 7);

  if (
    isAfter(releaseDateObj, today) &&
    isBefore(releaseDateObj, oneWeekFromNow)
  ) {
    const dayName = format(releaseDateObj, "EEEE");
    return `Opens ${dayName}`;
  }

  return format(releaseDateObj, "EEE, MMM d, yyyy");
};
