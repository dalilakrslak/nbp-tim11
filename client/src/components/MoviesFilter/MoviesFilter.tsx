import { useCallback, useMemo } from "react";
import { Input, Select } from "antd";
import {
  ClockCircleOutlined,
  EnvironmentOutlined,
  HomeOutlined,
  SearchOutlined,
  VideoCameraOutlined,
} from "@ant-design/icons";

import { MovieFilters } from "../../api/types";
import { DateList } from "../DateList";
import { useGenres, useAllVenues, useAllLocations } from "../../hooks";

import "./moviesFilter.scss";

export type FilterProps = {
  filters?: MovieFilters;
  onFilterChange: (filterType: string, value: string | string[]) => void;
  projectionTimes?: {
    displayValue: string;
    value: string;
  }[];
  isCurrentlyShowing: boolean;
};

export const MoviesFilter = ({
  filters,
  onFilterChange,
  projectionTimes,
  isCurrentlyShowing,
}: FilterProps) => {
  const { data: genresData } = useGenres();
  const { data: venuesData } = useAllVenues();
  const { data: locationsData } = useAllLocations();

  const genreNames = useMemo(() => {
    return genresData?.map((genre) => genre.name) || [];
  }, [genresData]);

  const venueNames = useMemo(() => {
    return venuesData?.map((venue) => venue.name) || [];
  }, [venuesData]);

  const cityNames = useMemo(() => {
    return locationsData?.map((location) => location.city) || [];
  }, [locationsData]);

  const handleFilterChange = useCallback(
    (filterType: string, value: string | string[]) => {
      onFilterChange(filterType, value);
    },
    [onFilterChange]
  );

  const sortedProjectionTimes = useMemo(
    () =>
      projectionTimes?.slice().sort((a, b) => {
        const dateA = new Date(a.value);
        const dateB = new Date(b.value);
        return dateA.getTime() - dateB.getTime();
      }) || [],
    [projectionTimes]
  );

  const cityOptions = useMemo(
    () =>
      cityNames?.map((city) => ({
        value: city,
        label: city,
      })),
    [cityNames]
  );

  const cinemaOptions = useMemo(
    () =>
      venueNames?.map((cinema) => ({
        value: cinema,
        label: cinema,
      })),
    [venueNames]
  );

  const genreOptions = useMemo(
    () =>
      genreNames?.map((genre) => ({
        value: genre,
        label: genre,
      })),
    [genreNames]
  );

  const projectionTimeOptions = useMemo(
    () =>
      sortedProjectionTimes.map((projection) => ({
        value: projection.value,
        label: projection.displayValue,
      })),
    [sortedProjectionTimes]
  );

  return (
    <div className="filter-container">
      <div className="search-input">
        <Input
          size="large"
          prefix={<SearchOutlined className="search-icon" />}
          placeholder="Search Movies"
          value={filters?.title}
          onChange={(e) => handleFilterChange("title", e.target.value)}
          className="title-input"
        />
      </div>
      <div className="filters">
        <Select
          size="large"
          prefix={<EnvironmentOutlined className="filter-icon" />}
          placeholder="All Cities"
          value={filters?.city || undefined}
          onChange={(value) => handleFilterChange("city", value)}
          options={cityOptions}
          className="filters-select-dropdown"
          allowClear
        />
        <Select
          size="large"
          prefix={<HomeOutlined className="filter-icon" />}
          placeholder="All Cinemas"
          value={filters?.cinema || undefined}
          onChange={(value) => handleFilterChange("cinema", value)}
          options={cinemaOptions}
          className="filters-select-dropdown"
          allowClear
        />
        <Select
          size="large"
          prefix={<VideoCameraOutlined className="filter-icon" />}
          placeholder="All Genres"
          value={filters?.genres}
          mode="multiple"
          onChange={(value) => handleFilterChange("genres", value)}
          options={genreOptions}
          className="filters-select-dropdown"
        />
        {isCurrentlyShowing && (
          <Select
            size="large"
            prefix={<ClockCircleOutlined className="filter-icon" />}
            placeholder="All Projection Times"
            value={filters?.projectionTime || undefined}
            allowClear
            onChange={(value) => handleFilterChange("projectionTime", value)}
            options={projectionTimeOptions}
            className="filters-select-dropdown"
          />
        )}
      </div>
      {isCurrentlyShowing && (
        <DateList
          onDateSelect={(value) => handleFilterChange("date", value)}
          selectedDate={filters?.date || ""}
        />
      )}
    </div>
  );
};
