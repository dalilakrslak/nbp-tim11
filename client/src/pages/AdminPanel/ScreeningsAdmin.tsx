import { useEffect, useState } from "react";
import { Modal, Table, Form, Select, DatePicker } from "antd";
import dayjs from "dayjs";
import { Button } from "../../components/Button";
import { CompactPagination } from "../../components/CompactPagination";
import { Pageable } from "../../utils";
import { axiosApp } from "../../api/axiosApp";

const PAGE_SIZE = 5;

type PageResponse<T> = {
  content: T[];
  page: {
    totalElements: number;
  };
};

type MovieListItem = {
  id: string;
  title: string;
};

type VenueListItem = {
  id: string;
  name: string;
};

type HallListItem = {
  id: string;
  name: string;
};

type ScreeningListItem = {
  id: string;
  movie: {
    id: string;
    title: string;
  };
  hall: {
    id: string;
    name: string;
    venue?: {
      id: string;
      name: string;
      location?: {
        city?: string;
        country?: string;
      };
    };
  };
  startTime: string;
};

type CreateScreeningPayload = {
  movieId: string;
  hallId: string;
  startTime: string;
};

export default function ScreeningsAdmin() {
  const [screenings, setScreenings] = useState<ScreeningListItem[]>([]);
  const [movies, setMovies] = useState<MovieListItem[]>([]);
  const [venues, setVenues] = useState<VenueListItem[]>([]);
  const [halls, setHalls] = useState<HallListItem[]>([]);

  const [totalElements, setTotalElements] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const [openAdd, setOpenAdd] = useState(false);
  const [selectedVenueId, setSelectedVenueId] = useState<string | null>(null);

  const [form] = Form.useForm();

  const getAdminScreenings = async (
    pageable: Pageable
  ): Promise<PageResponse<ScreeningListItem>> => {
    const response = await axiosApp.get("/screenings", {
      params: { ...pageable },
    });

    return response.data;
  };

  const getAdminMovies = async (): Promise<PageResponse<MovieListItem>> => {
    const response = await axiosApp.get("/movies", {
      params: {
        page: 0,
        size: 100,
      },
    });

    return response.data;
  };

  const getVenuesAll = async (): Promise<VenueListItem[]> => {
    const response = await axiosApp.get("/venues/all");
    return response.data;
  };

  const getHallsByVenueId = async (
    venueId: string
  ): Promise<HallListItem[]> => {
    const response = await axiosApp.get(`/venues/${venueId}/halls`);
    return response.data;
  };

  const createScreening = async (
    payload: CreateScreeningPayload
  ): Promise<ScreeningListItem> => {
    const response = await axiosApp.post("/screenings", payload);
    return response.data;
  };

  const fetchScreenings = async (page = currentPage) => {
    const pageable: Pageable = { page, size: PAGE_SIZE };
    const data = await getAdminScreenings(pageable);

    setScreenings(data.content);
    setTotalElements(data.page.totalElements);
  };

  const fetchDropdownData = async () => {
    const moviesData = await getAdminMovies();
    const venuesData = await getVenuesAll();

    setMovies(moviesData.content);
    setVenues(venuesData);
  };

  useEffect(() => {
    fetchScreenings();
  }, [currentPage]);

  useEffect(() => {
    fetchDropdownData();
  }, []);

  const handleVenueChange = async (venueId: string) => {
    setSelectedVenueId(venueId);
    form.setFieldValue("hallId", undefined);

    const hallsData = await getHallsByVenueId(venueId);
    setHalls(hallsData);
  };

  const handleCreateScreening = async () => {
    try {
      const values = await form.validateFields();

      await createScreening({
        movieId: values.movieId,
        hallId: values.hallId,
        startTime: values.startTime.format("YYYY-MM-DDTHH:mm:ss"),
      });

      setOpenAdd(false);
      form.resetFields();
      setSelectedVenueId(null);
      setHalls([]);

      const lastPage = Math.ceil((totalElements + 1) / PAGE_SIZE) - 1;
      setCurrentPage(lastPage);
    } catch {}
  };

  const columns = [
    {
      title: "Movie",
      render: (_: any, record: ScreeningListItem) => record.movie?.title || "-",
    },
    {
      title: "Venue",
      render: (_: any, record: ScreeningListItem) =>
        record.hall?.venue?.name || "-",
    },
    {
      title: "Hall",
      render: (_: any, record: ScreeningListItem) => record.hall?.name || "-",
    },
    {
      title: "City",
      render: (_: any, record: ScreeningListItem) =>
        record.hall?.venue?.location?.city || "-",
    },
    {
      title: "Start Time",
      dataIndex: "startTime",
      render: (value: string) =>
        value ? dayjs(value).format("DD.MM.YYYY HH:mm") : "-",
    },
  ];

  return (
    <div
      style={{
        background: "white",
        borderRadius: 14,
        padding: 32,
        boxShadow: "0 8px 25px rgba(0,0,0,0.06)",
      }}
    >
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: 24,
        }}
      >
        <h1 style={{ margin: 0, fontSize: 30 }}>Screenings</h1>

        <Button
          variant="primary"
          label="+ Add Screening"
          onClick={() => {
            form.resetFields();
            setSelectedVenueId(null);
            setHalls([]);
            setOpenAdd(true);
          }}
        />
      </div>

      <Table
        columns={columns}
        dataSource={screenings}
        rowKey="id"
        pagination={false}
      />

      <div style={{ marginTop: 20 }}>
        <CompactPagination
          currentPage={currentPage}
          totalElements={totalElements}
          pageSize={PAGE_SIZE}
          onPageChange={setCurrentPage}
        />
      </div>

      <Modal
        title="Add Screening"
        open={openAdd}
        footer={null}
        onCancel={() => {
          setOpenAdd(false);
          form.resetFields();
          setSelectedVenueId(null);
          setHalls([]);
        }}
      >
        <Form layout="vertical" form={form}>
          <Form.Item
            label="Movie"
            name="movieId"
            rules={[{ required: true, message: "Please select movie" }]}
          >
            <Select
              showSearch
              placeholder="Select movie"
              optionFilterProp="label"
              options={movies.map((movie) => ({
                value: movie.id,
                label: movie.title,
              }))}
            />
          </Form.Item>

          <Form.Item
            label="Venue"
            name="venueId"
            rules={[{ required: true, message: "Please select venue" }]}
          >
            <Select
              showSearch
              placeholder="Select venue"
              optionFilterProp="label"
              onChange={handleVenueChange}
              options={venues.map((venue) => ({
                value: venue.id,
                label: venue.name,
              }))}
            />
          </Form.Item>

          <Form.Item
            label="Hall"
            name="hallId"
            rules={[{ required: true, message: "Please select hall" }]}
          >
            <Select
              placeholder={
                selectedVenueId ? "Select hall" : "Select venue first"
              }
              disabled={!selectedVenueId}
              options={halls.map((hall) => ({
                value: hall.id,
                label: hall.name,
              }))}
            />
          </Form.Item>

          <Form.Item
            label="Start Time"
            name="startTime"
            rules={[{ required: true, message: "Please select start time" }]}
          >
            <DatePicker
              showTime
              format="DD.MM.YYYY HH:mm"
              style={{ width: "100%" }}
            />
          </Form.Item>

          <div style={{ display: "flex", justifyContent: "flex-end", gap: 12 }}>
            <Button
              variant="secondary"
              label="Cancel"
              onClick={() => {
                setOpenAdd(false);
                form.resetFields();
                setSelectedVenueId(null);
                setHalls([]);
              }}
            />
            <Button
              variant="primary"
              label="Create"
              onClick={handleCreateScreening}
            />
          </div>
        </Form>
      </Modal>
    </div>
  );
}