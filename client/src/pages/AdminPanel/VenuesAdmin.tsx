import { useEffect, useState } from "react";
import { Modal, Table, Form, Input, Select } from "antd";
import { Button } from "../../components/Button";
import { CompactPagination } from "../../components/CompactPagination";
import { ConfirmModal } from "../../components/ConfirmModal";
import { Pageable } from "../../utils";

import { getVenues, createVenue, updateVenue, deleteVenue } from "../../api/venues";

import { useAllLocations } from "../../hooks";
import type { Venue, Location } from "../../api/types";

const PAGE_SIZE = 5;

export default function VenuesAdmin() {
  const [venues, setVenues] = useState<Venue[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const [openAdd, setOpenAdd] = useState(false);
  const [openEdit, setOpenEdit] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);

  const [venueToEdit, setVenueToEdit] = useState<Venue | null>(null);
  const [venueToDelete, setVenueToDelete] = useState<Venue | null>(null);

  const [form] = Form.useForm();
  const [editForm] = Form.useForm();

  const { data: locations = [], isLoading: locationsLoading } =
    useAllLocations();

  const fetchVenues = async (page = currentPage) => {
    const pageable: Pageable = { page, size: PAGE_SIZE };
    const data = await getVenues(pageable);

    setVenues(data.content);
    setTotalElements(data.page.totalElements);
  };

  useEffect(() => {
    fetchVenues();
  }, [currentPage]);

  const handleCreateVenue = async () => {
    try {
      const values = await form.validateFields();

      await createVenue({
        name: values.name,
        street: values.street,
        imageUrl: values.imageUrl,
        locationId: values.locationId,
      });

      setOpenAdd(false);
      form.resetFields();

      const lastPage =
        Math.ceil((totalElements + 1) / PAGE_SIZE) - 1;

      setCurrentPage(lastPage);
    } catch {}
  };

  const handleEdit = (venue: Venue) => {
    setVenueToEdit(venue);

    editForm.setFieldsValue({
      name: venue.name,
      street: venue.street,
      imageUrl: venue.imageUrl,
      locationId: venue.location.id,
    });

    setOpenEdit(true);
  };

  const handleSaveEdit = async () => {
    if (!venueToEdit) return;

    try {
      const values = await editForm.validateFields();

      await updateVenue(venueToEdit.id, {
        name: values.name,
        street: values.street,
        imageUrl: values.imageUrl,
        locationId: values.locationId,
      });

      setOpenEdit(false);
      setVenueToEdit(null);

      fetchVenues(); 
    } catch (e) {
      console.error(e);
    }
  };

  const confirmDelete = async () => {
    if (!venueToDelete) return;

    await deleteVenue(venueToDelete.id);

    const isLastItemOnPage = venues.length === 1 && currentPage > 0;

    setDeleteOpen(false);
    setVenueToDelete(null);

    if (isLastItemOnPage) {
      setCurrentPage((p) => p - 1);
    } else {
      fetchVenues();
    }
  };

  const columns = [
    { title: "Name", dataIndex: "name" },
    { title: "Street", dataIndex: "street" },
    {
      title: "Image",
      dataIndex: "imageUrl",
      render: (url: string) => (
        <a href={url} target="_blank" rel="noopener noreferrer">
          Open
        </a>
      ),
    },
    {
      title: "City",
      render: (_: any, record: Venue) =>
        record.location?.city ?? "NO LOCATION",
    },
    {
      title: "Actions",
      render: (_: any, record: Venue) => (
        <div style={{ display: "flex", gap: 10 }}>
          <Button
            variant="primary"
            label="Edit"
            onClick={() => handleEdit(record)}
          />
          <Button
            variant="secondary"
            label="Delete"
            onClick={() => {
              setVenueToDelete(record);
              setDeleteOpen(true);
            }}
          />
        </div>
      ),
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
          marginBottom: 24,
        }}
      >
        <h1 style={{ margin: 0, fontSize: 30 }}>Venues</h1>

        <Button
          variant="primary"
          label="+ Add Venue"
          onClick={() => {
            form.resetFields();
            setOpenAdd(true);
          }}
        />
      </div>

      <Table
        columns={columns}
        dataSource={venues}
        rowKey="id"
        pagination={false}
      />

      <CompactPagination
        currentPage={currentPage}
        totalElements={totalElements}
        pageSize={PAGE_SIZE}
        onPageChange={setCurrentPage}
      />

      <Modal
        title="Add Venue"
        open={openAdd}
        onCancel={() => setOpenAdd(false)}
        footer={null}
      >
        <Form layout="vertical" form={form}>
          <Form.Item name="name" label="Name" rules={[{ required: true }]}>
            <Input />
          </Form.Item>

          <Form.Item name="street" label="Street" rules={[{ required: true }]}>
            <Input />
          </Form.Item>

          <Form.Item
            name="imageUrl"
            label="Image URL"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="locationId"
            label="Location"
            rules={[{ required: true }]}
          >
            <Select loading={locationsLoading} showSearch>
              {locations.map((loc: Location) => (
                <Select.Option key={loc.id} value={loc.id}>
                  {loc.city}, {loc.country}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <div style={{ display: "flex", justifyContent: "flex-end", gap: 12 }}>
            <Button
              variant="secondary"
              label="Cancel"
              onClick={() => setOpenAdd(false)}
            />
            <Button
              variant="primary"
              label="Create"
              onClick={handleCreateVenue}
            />
          </div>
        </Form>
      </Modal>

      <Modal
        title="Edit Venue"
        open={openEdit}
        onCancel={() => setOpenEdit(false)}
        footer={null}
      >
        <Form layout="vertical" form={editForm}>
          <Form.Item name="name" label="Name" rules={[{ required: true }]}>
            <Input />
          </Form.Item>

          <Form.Item name="street" label="Street" rules={[{ required: true }]}>
            <Input />
          </Form.Item>

          <Form.Item
            name="imageUrl"
            label="Image URL"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="locationId"
            label="Location"
            rules={[{ required: true }]}
          >
            <Select loading={locationsLoading} showSearch>
              {locations.map((loc: Location) => (
                <Select.Option key={loc.id} value={loc.id}>
                  {loc.city}, {loc.country}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <div style={{ display: "flex", justifyContent: "flex-end", gap: 12 }}>
            <Button
              variant="secondary"
              label="Cancel"
              onClick={() => setOpenEdit(false)}
            />
            <Button
              variant="primary"
              label="Save Changes"
              onClick={handleSaveEdit}
            />
          </div>
        </Form>
      </Modal>

      <ConfirmModal
        open={deleteOpen}
        title="Delete Venue"
        description={`Are you sure you want to delete venue "${venueToDelete?.name}"?`}
        confirmText="Delete"
        cancelText="Cancel"
        onConfirm={confirmDelete}
        onCancel={() => setDeleteOpen(false)}
      />
    </div>
  );
}
