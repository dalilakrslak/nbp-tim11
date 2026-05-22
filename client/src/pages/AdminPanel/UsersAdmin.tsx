import { useEffect, useState } from "react";
import { Modal, Table, Form, Input } from "antd";
import { Button } from "../../components/Button";
import { CompactPagination } from "../../components/CompactPagination";
import { ConfirmModal } from "../../components/ConfirmModal";
import { Pageable } from "../../utils";

import { getUsers, createUser, deleteUser } from "../../api/users";
import type { User } from "../../api/users";

const PAGE_SIZE = 5;

export default function UsersAdmin() {
  const [users, setUsers] = useState<User[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const [openAdd, setOpenAdd] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);

  const [userToDelete, setUserToDelete] = useState<User | null>(null);

  const [form] = Form.useForm();

  const fetchUsers = async (page = currentPage) => {
    const pageable: Pageable = { page, size: PAGE_SIZE };
    const data = await getUsers(pageable);

    setUsers(data.content);
    setTotalElements(data.page.totalElements);
  };

  useEffect(() => {
    fetchUsers();
  }, [currentPage]);

  const handleCreateUser = async () => {
    try {
      const values = await form.validateFields();

      await createUser({
        email: values.email,
        password: values.password,
      });

      setOpenAdd(false);
      form.resetFields();

      const lastPage = Math.ceil((totalElements + 1) / PAGE_SIZE) - 1;

      setCurrentPage(lastPage);
      fetchUsers(lastPage);
    } catch {}
  };

  const confirmDelete = async () => {
    if (!userToDelete) return;

    await deleteUser(userToDelete.id);

    const isLastItemOnPage = users.length === 1 && currentPage > 0;

    setDeleteOpen(false);
    setUserToDelete(null);

    if (isLastItemOnPage) {
      setCurrentPage((p) => p - 1);
    } else {
      fetchUsers();
    }
  };

  const columns = [
    { title: "Email", dataIndex: "email" },
    { title: "Created At", dataIndex: "createdAt" },
    {
      title: "Actions",
      render: (_: any, record: User) => (
        <Button
          variant="secondary"
          label="Delete"
          onClick={() => {
            setUserToDelete(record);
            setDeleteOpen(true);
          }}
        />
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
          alignItems: "center",
          marginBottom: 24,
        }}
      >
        <h1 style={{ margin: 0, fontSize: 30 }}>Users</h1>

        <Button
          variant="primary"
          label="+ Add User"
          onClick={() => {
            form.resetFields();
            setOpenAdd(true);
          }}
        />
      </div>

      <Table
        columns={columns}
        dataSource={users}
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
        title="Add User"
        open={openAdd}
        footer={null}
        onCancel={() => setOpenAdd(false)}
        destroyOnClose
      >
        <Form layout="vertical" form={form}>
          <Form.Item
            label="Email"
            name="email"
            rules={[{ required: true, type: "email" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true }]}
          >
            <Input.Password />
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
              onClick={handleCreateUser}
            />
          </div>
        </Form>
      </Modal>

      <ConfirmModal
        open={deleteOpen}
        title="Delete User"
        description={`Are you sure you want to delete user "${userToDelete?.email}"?`}
        confirmText="Delete"
        cancelText="Cancel"
        onConfirm={confirmDelete}
        onCancel={() => setDeleteOpen(false)}
      />
    </div>
  );
}