import { useState } from "react";
import { useLogout } from "../../hooks";
import { FaChevronDown } from "react-icons/fa6";
import { useNavigate } from "react-router-dom";

import "./profileDropdown.scss";

export type ProfileDropdownProps = {
  username: string;
};

export const ProfileDropdown = ({ username }: ProfileDropdownProps) => {
  const [isDropdownOpen, setDropdownOpen] = useState<boolean>(false);
  const logoutMutation = useLogout();
  const navigate = useNavigate();

  const toggleDropdown = () => {
    setDropdownOpen((prev) => !prev);
  };

  const handleLogout = async () => {
    try {
      await logoutMutation.mutateAsync();
    } catch (error) {
      console.error("Error during logout:", error);
    } finally {
      setDropdownOpen(false);
    }
  };

  return (
    <div className="profile-dropdown-container">
      <button className="profile-dropdown-button" onClick={toggleDropdown}>
        <div className="profile-dropdown-content">
          <div>{username}</div>
          <FaChevronDown
            className={`profile-dropdown-icon ${
              isDropdownOpen ? "profile-dropdown-icon-active" : ""
            }`}
          />
        </div>
      </button>

      {isDropdownOpen && (
        <div className="profile-dropdown">
          <button
            className="profile-dropdown-item"
            onClick={() => {
              navigate("/profile");
              setDropdownOpen(false);
            }}
          >
            Profile
          </button>

          <button
            className="profile-dropdown-item"
            onClick={handleLogout}
            disabled={logoutMutation.isPending}
          >
            {logoutMutation.isPending ? "Logging out..." : "Logout"}
          </button>
        </div>
      )}
    </div>
  );
};
