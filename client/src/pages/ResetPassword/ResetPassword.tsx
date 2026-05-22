import { useSearchParams } from "react-router-dom";
import { useState } from "react";
import { Authentication } from "../../components/Authentication";
import { ResetPasswordForm } from "../../components/ResetPasswordForm";

export const ResetPassword = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token") || "";
  const [isAuthOpen, setIsAuthOpen] = useState(true);

  const handleClose = () => setIsAuthOpen(false);

  return (
    <Authentication isOpen={isAuthOpen} onClose={handleClose}>
      <ResetPasswordForm token={token} onSuccess={() => setIsAuthOpen(false)} />
    </Authentication>
  );
};
