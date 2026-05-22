import { useEffect, useState } from "react";
import { Modal, Table, Form, Input, Select } from "antd";
import { Button } from "../../components/Button";
import { CompactPagination } from "../../components/CompactPagination";
import { ConfirmModal } from "../../components/ConfirmModal";
import { Pageable } from "../../utils";
import { getAdminMovies, createMovie, updateMovie, deleteMovie } from "../../api/movies";
import { PGRating, type MovieListItem } from "../../api/types";

const PAGE_SIZE = 5;

export default function MoviesAdmin() {
  const [movies, setMovies] = useState<MovieListItem[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const [openAdd, setOpenAdd] = useState(false);
  const [openEdit, setOpenEdit] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);

  const [movieToEdit, setMovieToEdit] = useState<MovieListItem | null>(null);
  const [movieToDelete, setMovieToDelete] = useState<MovieListItem | null>(null);

  const [form] = Form.useForm();
  const [editForm] = Form.useForm();

  const fetchMovies = async (page = currentPage) => {
    const pageable: Pageable = { page, size: PAGE_SIZE };
    const data = await getAdminMovies(pageable);
    setMovies(data.content);
    setTotalElements(data.page.totalElements);
  };

  useEffect(() => {
    fetchMovies();
  }, [currentPage]);

  const handleCreateMovie = async () => {
    try {
      const values = await form.validateFields();

      await createMovie({
        title: values.title,
        duration: values.duration,
        pgRating: values.pgRating,
        language: values.language,
        director: values.director,
        trailerUrl: values.trailerUrl,
      });

      setOpenAdd(false);
      form.resetFields();

      const lastPage = Math.ceil((totalElements + 1) / PAGE_SIZE) - 1;
      setCurrentPage(lastPage);
    } catch {}
  };

  const handleEdit = (movie: MovieListItem) => {
    setMovieToEdit(movie);
    editForm.setFieldsValue(movie);
    setOpenEdit(true);
  };

  const handleSaveEdit = async () => {
    try {
      const values = await editForm.validateFields();

      await updateMovie(movieToEdit!.id, values);

      setOpenEdit(false);
      setMovieToEdit(null);

      fetchMovies();
    } catch {}
  };

  const handleDeleteOpen = (movie: MovieListItem) => {
    setMovieToDelete(movie);
    setDeleteOpen(true);
  };

  const confirmDelete = async () => {
    if (!movieToDelete) return;

    await deleteMovie(movieToDelete.id);

    const isLastItemOnPage = movies.length === 1 && currentPage > 0;
    setDeleteOpen(false);
    setMovieToDelete(null);

    if (isLastItemOnPage) {
      setCurrentPage((p) => p - 1);
    } else {
      fetchMovies();
    }
  };

  const columns = [
    { title: "Title", dataIndex: "title" },
    { title: "Duration (min)", dataIndex: "duration" },
    {
      title: "PG Rating",
      dataIndex: "pgRating",
      render: (value: PGRating) => value,
    },
    { title: "Language", dataIndex: "language" },
    { title: "Director", dataIndex: "director" },
    {
      title: "Trailer",
      dataIndex: "trailerUrl",
      render: (url: string) => (
        <a href={url} target="_blank" rel="noopener noreferrer">
          Open
        </a>
      ),
    },
    {
      title: "Actions",
      render: (_: any, record: MovieListItem) => (
        <div style={{ display: "flex", gap: 10 }}>
          <Button variant="primary" label="Edit" onClick={() => handleEdit(record)} />
          <Button
            variant="secondary"
            label="Delete"
            onClick={() => handleDeleteOpen(record)}
          />
        </div>
      ),
    },
  ];

  const pgOptions = Object.values(PGRating).map((v) => ({
    label: v,
    value: v,
  }));

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
        <h1 style={{ margin: 0, fontSize: 30 }}>Movies</h1>
        <Button
          variant="primary"
          label="+ Add Movie"
          onClick={() => {
            form.resetFields();
            setOpenAdd(true);
          }}
        />
      </div>

      <Table columns={columns} dataSource={movies} rowKey="id" pagination={false} />

      <div style={{ marginTop: 20 }}>
        <CompactPagination
          currentPage={currentPage}
          totalElements={totalElements}
          pageSize={PAGE_SIZE}
          onPageChange={setCurrentPage}
        />
      </div>

      <Modal title="Add Movie" open={openAdd} footer={null} onCancel={() => setOpenAdd(false)}>
        <Form layout="vertical" form={form}>
          <Form.Item label="Title" name="title" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item label="Duration (min)" name="duration" rules={[{ required: true }]}><Input type="number" /></Form.Item>
          <Form.Item label="PG Rating" name="pgRating" rules={[{ required: true }]}><Select options={pgOptions} /></Form.Item>
          <Form.Item label="Language" name="language" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item label="Director" name="director" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item label="Trailer URL" name="trailerUrl" rules={[{ required: true }, { type: "url" }]}><Input /></Form.Item>

          <div style={{ display: "flex", justifyContent: "flex-end", gap: 12 }}>
            <Button variant="secondary" label="Cancel" onClick={() => setOpenAdd(false)} />
            <Button variant="primary" label="Create" onClick={handleCreateMovie} />
          </div>
        </Form>
      </Modal>

      <Modal title="Edit Movie" open={openEdit} footer={null} onCancel={() => setOpenEdit(false)}>
        <Form layout="vertical" form={editForm}>
          <Form.Item label="Title" name="title" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item label="Duration (min)" name="duration" rules={[{ required: true }]}><Input type="number" /></Form.Item>
          <Form.Item label="PG Rating" name="pgRating" rules={[{ required: true }]}><Select options={pgOptions} /></Form.Item>
          <Form.Item label="Language" name="language" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item label="Director" name="director" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item label="Trailer URL" name="trailerUrl" rules={[{ required: true }, { type: "url" }]}><Input /></Form.Item>

          <div style={{ display: "flex", justifyContent: "flex-end", gap: 12 }}>
            <Button variant="secondary" label="Cancel" onClick={() => setOpenEdit(false)} />
            <Button variant="primary" label="Save Changes" onClick={handleSaveEdit} />
          </div>
        </Form>
      </Modal>

      <ConfirmModal
        open={deleteOpen}
        title="Delete Movie"
        description={`Are you sure you want to delete the movie "${movieToDelete?.title}"?`}
        confirmText="Delete"
        cancelText="Cancel"
        onConfirm={confirmDelete}
        onCancel={() => setDeleteOpen(false)}
      />
    </div>
  );
}