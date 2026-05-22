import { Navigate, Outlet } from "react-router-dom";
import AdminSidebar from "../../components/AdminPanel/AdminSidebar";
import { useCurrentUser } from "../../hooks";

export default function AdminLayout() {
  const { data: currentUser, isLoading } = useCurrentUser();
  const isAdmin = !isLoading && !!currentUser && currentUser.role==="ADMIN";
  if (isLoading) {
    return <p>Loading...</p>;
  }

  if (!isAdmin) {
    return <Navigate to="/" replace />;
  }
  return (
    <div style={{ display: "flex", minHeight: "100vh" }}>
      <AdminSidebar />

      <main style={{flex: 1, background: "#F3F4F6", padding: "30px 40px", marginTop: 0 }}>
        <Outlet />
      </main>
    </div>
  );
}
