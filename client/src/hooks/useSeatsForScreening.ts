import { useQuery } from "@tanstack/react-query";

import { getSeatsForScreening } from "../api";
import type { SeatAvailability } from "../api";

export const useSeatsForScreening = (screeningId: string) => {
  return useQuery<SeatAvailability[]>({
    queryKey: ["seats", screeningId],
    queryFn: () => getSeatsForScreening(screeningId),
    enabled: !!screeningId,
  });
};
