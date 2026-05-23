import { useMemo, useState } from "react";
import { Tabs, Table, Input, Progress, Tag, Statistic, Card } from "antd";
import type { ColumnsType } from "antd/es/table";

import {
  useMovieCatalogReport,
  useScreeningAvailabilityReport,
  useTicketSalesReport,
} from "../../../hooks";
import type {
  MovieCatalogReportRow,
  ScreeningAvailabilityReportRow,
  TicketSalesReportRow,
} from "../../../api/reports";

import "./reports.scss";

const formatDate = (value: string | null | undefined) => {
  if (!value) return "—";
  return new Date(value).toLocaleDateString("en-GB");
};

const formatDateTime = (value: string | null | undefined) => {
  if (!value) return "—";
  return new Date(value).toLocaleString("en-GB", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const formatMoney = (value: number | string | null | undefined) => {
  if (value === null || value === undefined) return "—";
  const num = typeof value === "string" ? Number(value) : value;
  if (Number.isNaN(num)) return "—";
  return num.toLocaleString("en-US", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
};

const toNumber = (value: number | string | null | undefined) => {
  if (value === null || value === undefined) return 0;
  const num = typeof value === "string" ? Number(value) : value;
  return Number.isNaN(num) ? 0 : num;
};

const MovieCatalogTab = () => {
  const { data, isLoading, isError } = useMovieCatalogReport();
  const [search, setSearch] = useState("");

  const filtered = useMemo(() => {
    if (!data) return [];
    const q = search.trim().toLowerCase();
    if (!q) return data;
    return data.filter(
      (row) =>
        row.title.toLowerCase().includes(q) ||
        row.director?.toLowerCase().includes(q) ||
        row.genres?.toLowerCase().includes(q)
    );
  }, [data, search]);

  const columns: ColumnsType<MovieCatalogReportRow> = [
    {
      title: "",
      dataIndex: "coverImageUrl",
      key: "cover",
      width: 70,
      render: (url) =>
        url ? (
          <img src={url} alt="" className="report-cover-thumb" />
        ) : (
          <div className="report-cover-thumb empty" />
        ),
    },
    {
      title: "Title",
      dataIndex: "title",
      key: "title",
      sorter: (a, b) => a.title.localeCompare(b.title),
      defaultSortOrder: "ascend",
    },
    {
      title: "Duration",
      dataIndex: "duration",
      key: "duration",
      width: 110,
      render: (v) => (v ? `${v} min` : "—"),
      sorter: (a, b) => (a.duration ?? 0) - (b.duration ?? 0),
    },
    { title: "Language", dataIndex: "language", key: "language", width: 110 },
    {
      title: "PG",
      dataIndex: "pgRating",
      key: "pgRating",
      width: 80,
      render: (v) => v ?? "—",
    },
    { title: "Director", dataIndex: "director", key: "director" },
    {
      title: "Projection",
      key: "projection",
      render: (_, row) => `${formatDate(row.startDate)} – ${formatDate(row.endDate)}`,
    },
    { title: "Genres", dataIndex: "genres", key: "genres" },
    { title: "Cast", dataIndex: "castMembers", key: "castMembers" },
    { title: "Writers", dataIndex: "writers", key: "writers" },
  ];

  return (
    <div className="report-section">
      <div className="report-toolbar">
        <Input.Search
          allowClear
          placeholder="Search title, director, or genre"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          style={{ maxWidth: 360 }}
        />
      </div>
      <Table
        rowKey="movieId"
        columns={columns}
        dataSource={filtered}
        loading={isLoading}
        pagination={{ pageSize: 10, showSizeChanger: true }}
        scroll={{ x: 1200 }}
        locale={{
          emptyText: isError ? "Failed to load movie catalog" : "No movies found",
        }}
      />
    </div>
  );
};

const ScreeningAvailabilityTab = () => {
  const { data, isLoading, isError } = useScreeningAvailabilityReport();

  const columns: ColumnsType<ScreeningAvailabilityReportRow> = [
    {
      title: "Start",
      dataIndex: "startTime",
      key: "startTime",
      width: 170,
      render: formatDateTime,
      sorter: (a, b) =>
        new Date(a.startTime).getTime() - new Date(b.startTime).getTime(),
      defaultSortOrder: "ascend",
    },
    {
      title: "Movie",
      dataIndex: "movieTitle",
      key: "movieTitle",
      sorter: (a, b) => a.movieTitle.localeCompare(b.movieTitle),
    },
    {
      title: "Venue",
      key: "venue",
      render: (_, row) => (
        <div>
          <div>{row.venueName}</div>
          <div className="report-muted">
            {[row.city, row.street].filter(Boolean).join(" · ")}
          </div>
        </div>
      ),
    },
    { title: "Hall", dataIndex: "hallName", key: "hallName", width: 120 },
    {
      title: "Seats",
      key: "seats",
      width: 160,
      render: (_, row) => (
        <span>
          <strong>{row.bookedSeats ?? 0}</strong> / {row.totalSeats ?? 0}{" "}
          <span className="report-muted">
            ({row.availableSeats ?? 0} free)
          </span>
        </span>
      ),
    },
    {
      title: "Occupancy",
      key: "occupancy",
      width: 220,
      render: (_, row) => {
        const pct = Math.round(toNumber(row.occupancyPercentage));
        const status = pct >= 90 ? "exception" : pct >= 60 ? "active" : "normal";
        return <Progress percent={pct} status={status} size="small" />;
      },
      sorter: (a, b) =>
        toNumber(a.occupancyPercentage) - toNumber(b.occupancyPercentage),
    },
  ];

  return (
    <div className="report-section">
      <Table
        rowKey="screeningId"
        columns={columns}
        dataSource={data ?? []}
        loading={isLoading}
        pagination={{ pageSize: 10, showSizeChanger: true }}
        scroll={{ x: 1100 }}
        locale={{
          emptyText: isError
            ? "Failed to load screening availability"
            : "No screenings found",
        }}
      />
    </div>
  );
};

const TicketSalesTab = () => {
  const { data, isLoading, isError } = useTicketSalesReport();

  const totals = useMemo(() => {
    const rows = data ?? [];
    return {
      revenue: rows.reduce((sum, r) => sum + toNumber(r.totalRevenue), 0),
      tickets: rows.reduce((sum, r) => sum + (r.ticketCount ?? 0), 0),
      customers: rows.reduce((sum, r) => sum + (r.uniqueCustomers ?? 0), 0),
    };
  }, [data]);

  const columns: ColumnsType<TicketSalesReportRow> = [
    {
      title: "Date",
      dataIndex: "saleDate",
      key: "saleDate",
      width: 120,
      render: formatDate,
      sorter: (a, b) =>
        new Date(a.saleDate).getTime() - new Date(b.saleDate).getTime(),
      defaultSortOrder: "descend",
    },
    { title: "Venue", dataIndex: "venueName", key: "venueName" },
    { title: "Hall", dataIndex: "hallName", key: "hallName", width: 120 },
    { title: "Movie", dataIndex: "movieTitle", key: "movieTitle" },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      width: 120,
      render: (status: string) => (
        <Tag color={status === "PURCHASED" ? "green" : "gold"}>{status}</Tag>
      ),
      filters: [
        { text: "Purchased", value: "PURCHASED" },
        { text: "Pending", value: "PENDING" },
      ],
      onFilter: (value, row) => row.status === value,
    },
    {
      title: "Tickets",
      dataIndex: "ticketCount",
      key: "ticketCount",
      width: 100,
      align: "right",
      sorter: (a, b) => (a.ticketCount ?? 0) - (b.ticketCount ?? 0),
    },
    {
      title: "Seats Booked",
      dataIndex: "bookedSeatCount",
      key: "bookedSeatCount",
      width: 130,
      align: "right",
    },
    {
      title: "Revenue",
      dataIndex: "totalRevenue",
      key: "totalRevenue",
      width: 140,
      align: "right",
      render: (v) => formatMoney(v),
      sorter: (a, b) => toNumber(a.totalRevenue) - toNumber(b.totalRevenue),
    },
    {
      title: "Avg Price",
      dataIndex: "averageTicketPrice",
      key: "averageTicketPrice",
      width: 120,
      align: "right",
      render: (v) => formatMoney(v),
    },
    {
      title: "Customers",
      dataIndex: "uniqueCustomers",
      key: "uniqueCustomers",
      width: 120,
      align: "right",
    },
  ];

  return (
    <div className="report-section">
      <div className="report-kpi-row">
        <Card>
          <Statistic
            title="Total Revenue"
            value={totals.revenue}
            precision={2}
          />
        </Card>
        <Card>
          <Statistic title="Tickets Sold" value={totals.tickets} />
        </Card>
        <Card>
          <Statistic title="Unique Customers" value={totals.customers} />
        </Card>
      </div>

      <Table
        rowKey={(row, index) =>
          `${row.saleDate}-${row.venueName}-${row.hallName}-${row.movieTitle}-${row.status}-${index}`
        }
        columns={columns}
        dataSource={data ?? []}
        loading={isLoading}
        pagination={{ pageSize: 10, showSizeChanger: true }}
        scroll={{ x: 1300 }}
        locale={{
          emptyText: isError
            ? "Failed to load ticket sales report"
            : "No sales data found",
        }}
      />
    </div>
  );
};

export default function Reports() {
  return (
    <div className="reports-page">
      <div className="reports-header">
        <h1>Reports</h1>
      </div>

      <Tabs
        defaultActiveKey="movies"
        items={[
          {
            key: "movies",
            label: "Movie Catalog",
            children: <MovieCatalogTab />,
          },
          {
            key: "screenings",
            label: "Screening Availability",
            children: <ScreeningAvailabilityTab />,
          },
          {
            key: "sales",
            label: "Ticket Sales",
            children: <TicketSalesTab />,
          },
        ]}
      />
    </div>
  );
}