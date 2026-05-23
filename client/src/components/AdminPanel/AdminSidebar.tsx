import { NavLink } from "react-router-dom";
import "./adminSidebar.scss";
import PersonIcon from "@mui/icons-material/Person";
import MovieIcon from "@mui/icons-material/Movie";
import ApartmentIcon from "@mui/icons-material/Apartment";
import AssessmentIcon from "@mui/icons-material/Assessment";
import EventSeatIcon from "@mui/icons-material/EventSeat";

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

  <NavLink
    to="/admin/screenings"
    className={({ isActive }) =>
      isActive ? "sidebar-link active" : "sidebar-link"
    }
  >
    <EventSeatIcon />
    <span>Screenings</span>
  </NavLink>

  <NavLink
    to="/admin/reports"
    className={({ isActive }) =>
      isActive ? "sidebar-link active" : "sidebar-link"
    }
  >
    <AssessmentIcon />
    <span>Reports</span>
  </NavLink>
</nav>

    </aside>
  );
}
