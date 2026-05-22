import { Modal } from "antd";
import { Button } from "../Button";
import "./confirmModal.scss";

interface ConfirmModalProps {
  open: boolean;
  title?: string;
  description?: string;
  confirmText?: string;
  cancelText?: string;
  onConfirm: () => void;
  onCancel: () => void;
}

export const ConfirmModal = ({
  open,
  title = "Confirm Action",
  description = "Are you sure?",
  confirmText = "Confirm",
  cancelText = "Cancel",
  onConfirm,
  onCancel,
}: ConfirmModalProps) => {
  return (
    <Modal
      open={open}
      onCancel={onCancel}
      footer={null}
      centered
      destroyOnClose
      className="confirm-modal"
    >
      <div className="confirm-modal-content">

        <h2 className="confirm-title">{title}</h2>

        <p className="confirm-description">{description}</p>

        <div className="confirm-actions">
          <Button
            variant="secondary"
            label={cancelText}
            onClick={onCancel}
            className="confirm-cancel"
          />

          <Button
            variant="primary"
            label={confirmText}
            onClick={onConfirm}
            className="confirm-delete"
          />
        </div>
      </div>
    </Modal>
  );
};
