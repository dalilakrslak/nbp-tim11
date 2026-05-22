import { useState, useEffect } from "react";
import { Modal, Form, Input } from "antd";
import { Button } from "../../components/Button";
import { useCurrentUser } from "../../hooks";
import { useChangePassword } from "../../hooks/useResetPasswordLoggedIn";

import "./userProfile.scss";
import { Navigate } from "react-router-dom";
import { validateFieldValue } from "../../utils/authValidation";

export default function UserProfile() {
  const { data: currentUser, isLoading } = useCurrentUser();
  const isAuthenticated = !isLoading && !!currentUser;

  const [openPasswordModal, setOpenPasswordModal] = useState(false);
  const [openSuccess, setOpenSuccess] = useState(false);
  const [form] = Form.useForm();

  const changePasswordMutation = useChangePassword(
    () => {
      setOpenPasswordModal(false);
      setOpenSuccess(true);
      form.resetFields();
    },
    () => {
      form.setFields([
        {
          name: "oldPassword",
          errors: ["Current password is incorrect"],
        },
      ]);
    }
  );

  // Reset form every time modal opens
  useEffect(() => {
    if (openPasswordModal) {
      form.resetFields();
    }
  }, [openPasswordModal]);

  if (isLoading) {
    return <p>Loading...</p>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  const handleChangePassword = (values: any) => {
    changePasswordMutation.mutate({
      oldPassword: values.oldPassword,
      newPassword: values.newPassword,
    });
  };

  return (
    <div className="profile-page">
      <div className="profile-header">
        <h1>My Profile</h1>
        <p>View your account information and manage your security</p>
      </div>

      <div className="profile-content">
        <div className="profile-card">
          <div className="profile-avatar">
            <span>{currentUser.email.charAt(0).toUpperCase()}</span>
          </div>

          <div className="profile-info">
            <div className="profile-info-item">
              <label>Email</label>
              <p>{currentUser.email}</p>
            </div>
          </div>

          <div className="profile-actions">
            <Button
              label="Change Password"
              variant="primary"
              onClick={() => setOpenPasswordModal(true)}
            />
          </div>
        </div>
      </div>

      {/* Password Modal */}
      <Modal
        title="Change Password"
        open={openPasswordModal}
        onCancel={() => setOpenPasswordModal(false)}
        footer={null}
        destroyOnClose
      >
        <Form layout="vertical" form={form} onFinish={handleChangePassword}>
          <Form.Item
            label="Current Password"
            name="oldPassword"
            rules={[
              { required: true, message: "Please enter your current password" },
            ]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item
            label="New Password"
            name="newPassword"
            rules={[
              {
                validator: (_, value) => {
                  const error = validateFieldValue("password", value);
                  return error ? Promise.reject(error) : Promise.resolve();
                },
              },
            ]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item
            label="Confirm Password"
            name="confirmPassword"
            dependencies={["newPassword"]}
            rules={[
              { required: true, message: "Please confirm your password" },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || value === getFieldValue("newPassword")) {
                    return Promise.resolve();
                  }
                  return Promise.reject("Passwords do not match");
                },
              }),
            ]}
          >
            <Input.Password />
          </Form.Item>

          <div className="modal-actions">
            <Button
              variant="secondary"
              label="Cancel"
              onClick={() => setOpenPasswordModal(false)}
            />
            <Button
              type="submit"
              variant="primary"
              label="Save Changes"
              loading={isLoading}
            />
          </div>
        </Form>
      </Modal>

      {/* Success Modal */}
      <Modal
        title={<div style={{ textAlign: "center" }}>Successfully changed</div>}
        open={openSuccess}
        onCancel={() => setOpenSuccess(false)}
        footer={null}
        centered
      >
        <div style={{ display: "flex", justifyContent: "center" }}>
          <Button
            variant="primary"
            label="OK"
            onClick={() => setOpenSuccess(false)}
          />
        </div>
      </Modal>
    </div>
  );
}
