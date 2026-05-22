import { NavLink } from "react-router-dom";
import "./adminSidebar.scss";
import PersonIcon from "@mui/icons-material/Person";
import MovieIcon from "@mui/icons-material/Movie";
import ApartmentIcon from "@mui/icons-material/Apartment";

export default function AdminSidebar() {
  return (
    <aside className="admin-sidebar">
      <div className="sidebar-header">
        <div className="sidebar-icon-wrapper">
          <span className="sidebar-logo">🎬</span>
        </div>

        <div className="sidebar-title">Cinebh Admin</div>
      </div>

     <nav className="sidebar-nav">
  <NavLink
    to="/admin/users"
    className={({ isActive }) =>
      isActive ? "sidebar-link active" : "sidebar-link"
    }
  >
    <PersonIcon />
    <span>Users</span>
  </NavLink>

  <NavLink
    to="/admin/movies"
    className={({ isActive }) =>
      isActive ? "sidebar-link active" : "sidebar-link"
    }
  >
    <MovieIcon />
    <span>Movies</span>
  </NavLink>

  <NavLink
    to="/admin/venues"
    className={({ isActive }) =>
      isActive ? "sidebar-link active" : "sidebar-link"
    }
  >
    <ApartmentIcon />
    <span>Venues</span>
  </NavLink>
</nav>

    </aside>
  );
}
