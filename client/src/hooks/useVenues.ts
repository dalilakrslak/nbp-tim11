import * as React from "react";

import { getVenues } from "../api";
import type { Venue } from "../api";
import type { Pageable } from "../utils";

type VenuesResponse = {
  content: Venue[];
  totalElements: number;
};

export type UseVenuesProps = Pageable;

export const useVenues = ({ page = 0, size = 4 }: UseVenuesProps = {}) => {
  const [data, setData] = React.useState<VenuesResponse | null>(null);

  React.useEffect(() => {
    function fetchVenues() {
      const pageable: Pageable = { page, size };
      getVenues(pageable)
        .then((response) => {
          const formattedResponse = {
            content: response?.content || [],
            totalElements:
              response?.page?.totalElements ??
              Math.max((response?.content || []).length, (page + 1) * size),
          };
          setData(formattedResponse);
        })
        .catch((error) => {
          console.error("Failed to fetch venues:", error);
        });
    }

    fetchVenues();
  }, [page, size]);

  return { data };
};
