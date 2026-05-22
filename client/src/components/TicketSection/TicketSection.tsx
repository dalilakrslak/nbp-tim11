import { useMemo, useState, useCallback, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Select } from "antd";
import { EnvironmentOutlined, HomeOutlined } from "@ant-design/icons";
import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import ArrowForwardRoundedIcon from "@mui/icons-material/ArrowForwardRounded";

import {
  useAllVenues,
  useAllLocations,
  useCurrentUser,
  useScreenings,
} from "../../hooks";
import { DateList } from "../DateList";
import { Showtimes } from "../Showtimes";
import { Button } from "../Button";
import { Movie, Screening } from "../../api";
import NotifyBell from "../../assets/img/NotifyBell.png";

import "./ticketSection.scss";

export type TicketFilters = {
  city: string;
  cinema: string;
  date: string;
};

export type TicketSectionProps = {
  movie?: Movie;
};

export const TicketSection = ({ movie }: TicketSectionProps) => {
  const navigate = useNavigate();
  const { data: currentUser } = useCurrentUser();
  const { data: venuesData } = useAllVenues();
  const { data: locationsData } = useAllLocations();
  const dateListRef = useRef<HTMLDivElement | null>(null);
  const [isLeftDisabled, setIsLeftDisabled] = useState<boolean>(true);
  const [isRightDisabled, setIsRightDisabled] = useState<boolean>(false);
  const [selectedScreening, setSelectedScreening] = useState<Screening | null>(
    null
  );

  const [filters, setFilters] = useState<TicketFilters>({
    city: "",
    cinema: "",
    date: new Date().toISOString().split("T")[0],
  });

  const { data: screenings, isLoading: areScreeningsLoading } = useScreenings({
    movieId: movie?.id,
    city: filters.city || undefined,
    cinema: filters.cinema || undefined,
    date: filters.date,
  });

  const isUpcoming = useMemo(() => {
    if (!movie?.startDate) return false;

    const today = new Date();
    const startDate = new Date(movie.startDate);
    today.setHours(0, 0, 0, 0);
    startDate.setHours(0, 0, 0, 0);

    return startDate > today;
  }, [movie?.startDate]);

  const startMonth = useMemo(() => {
    if (!movie?.startDate) return "";

    const startDate = new Date(movie.startDate);

    return startDate.toLocaleString("default", { month: "long" });
  }, [movie?.startDate]);

  const handleFilterChange = useCallback(
    (filterType: keyof TicketFilters, value: string) => {
      setFilters((prev) => ({ ...prev, [filterType]: value }));
      setSelectedScreening(null);
    },
    []
  );

  const cityOptions = useMemo(
    () =>
      locationsData?.map((location) => ({
        value: location.city,
        label: location.city,
      })) || [],
    [locationsData]
  );

  const cinemaOptions = useMemo(() => {
    if (!venuesData) return [];

    const filteredVenues = filters.city
      ? venuesData.filter((venue) => venue.location.city === filters.city)
      : venuesData;

    return filteredVenues.map((venue) => ({
      value: venue.name,
      label: venue.name,
    }));
  }, [venuesData, filters.city]);

  const filteredShowtimes = screenings?.content || [];

  const handleScreeningSelect = useCallback((screening: Screening) => {
    setSelectedScreening(screening);
  }, []);

  const handleBuyTicket = useCallback(() => {
    if (!movie || !selectedScreening || !currentUser) return;

    navigate(`/screenings/${selectedScreening.id}/seats`);
  }, [movie, selectedScreening, currentUser, navigate]);

  const handleScroll = (direction: "left" | "right") => {
    if (!dateListRef.current) return;

    const scrollAmount = 200;

    if (direction === "left") {
      dateListRef.current.scrollBy({ left: -scrollAmount, behavior: "smooth" });
    } else {
      dateListRef.current.scrollBy({ left: scrollAmount, behavior: "smooth" });
    }

    checkScrollPosition();
  };

  const checkScrollPosition = useCallback(() => {
    if (!dateListRef.current) return;

    const { scrollLeft, scrollWidth, clientWidth } = dateListRef.current;
    const tolerance = 5;

    setIsLeftDisabled(scrollLeft <= 0);
    setIsRightDisabled(scrollLeft + clientWidth >= scrollWidth - tolerance);
  }, []);

  useEffect(() => {
    checkScrollPosition();
    window.addEventListener("resize", checkScrollPosition);
    return () => window.removeEventListener("resize", checkScrollPosition);
  }, [checkScrollPosition, venuesData]);

  // Reset selected screening when filtered showtimes change
  useEffect(() => {
    if (
      selectedScreening &&
      !filteredShowtimes.find((s) => s.id === selectedScreening.id)
    ) {
      setSelectedScreening(null);
    }
  }, [filteredShowtimes, selectedScreening]);

  if (isUpcoming) {
    return (
      <div className="ticket-section-container">
        <div className="ticket-section-content">
          <div className="upcoming-movie-info">
            <h5 className="upcoming-movie-title">
              {movie?.title} is coming in {startMonth}!
            </h5>
            <div className="upcoming-movie-bell">
              <img
                src={NotifyBell}
                alt="Coming Soon Bell"
                className="bell-icon"
              />
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="ticket-section-container">
      <div className="ticket-section-content">
        <div className="city-cinema-filters">
          <Select
            size="large"
            prefix={<EnvironmentOutlined className="filter-icon" />}
            placeholder="All Cities"
            value={filters.city || undefined}
            onChange={(value) => handleFilterChange("city", value || "")}
            options={cityOptions}
            className="filters-select-dropdown"
            allowClear
            loading={areScreeningsLoading}
          />
          <Select
            size="large"
            prefix={<HomeOutlined className="filter-icon" />}
            placeholder="All Cinemas"
            value={filters.cinema || undefined}
            onChange={(value) => handleFilterChange("cinema", value || "")}
            options={cinemaOptions}
            className="filters-select-dropdown"
            allowClear
            loading={areScreeningsLoading}
          />
        </div>

        <div className="date-navigation">
          <div
            className="date-list-container"
            ref={dateListRef}
            onScroll={checkScrollPosition}
          >
            <DateList
              selectedDate={filters.date}
              onDateSelect={(value) => handleFilterChange("date", value)}
              dateBoxClassName="small"
            />
          </div>
          <div className="date-arrows">
            <Button
              variant="tertiary"
              onClick={() => handleScroll("left")}
              disabled={isLeftDisabled}
              aria-label="Previous dates"
              icon={<ArrowBackRoundedIcon />}
            />
            <Button
              variant="tertiary"
              onClick={() => handleScroll("right")}
              disabled={isRightDisabled}
              aria-label="Next dates"
              icon={<ArrowForwardRoundedIcon />}
            />
          </div>
        </div>

        <Showtimes
          showtimes={filteredShowtimes}
          selectedScreening={selectedScreening}
          onScreeningSelect={handleScreeningSelect}
          isLoading={areScreeningsLoading}
        />
      </div>

      <div className="ticket-actions">
        <Button
          variant="secondary"
          className="reserve-button"
          label="Reserve Ticket"
          disabled={!currentUser}
        />
        <Button
          variant="primary"
          className="buy-button"
          label="Buy Ticket"
          disabled={!currentUser || !selectedScreening || areScreeningsLoading}
          onClick={handleBuyTicket}
        />
      </div>
    </div>
  );
};
