import { NavLink } from "react-router-dom";
import { useState, useEffect } from "react";

import { Authentication } from "../Authentication";
import { Button } from "../Button";
import logo from "../../assets/img/Logo.png";
import { ProfileDropdown } from "../ProfileDropdown";
import { useCurrentUser } from "../../hooks";

import "./header.scss";

export const Header = () => {
  const [authDrawerOpen, setAuthDrawerOpen] = useState<boolean>(false);
  const [resetToken, setResetToken] = useState<string | undefined>(undefined);
  const { data: currentUser, isLoading } = useCurrentUser();
  const isAdmin = !isLoading && !!currentUser && currentUser.role==="ADMIN";

  const isAuthenticated = !isLoading && !!currentUser;

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token") || params.get("resetToken");

    if (token) {
      setResetToken(token);
      setAuthDrawerOpen(true);
      window.history.replaceState({}, "", window.location.pathname);
    }
  }, []);

  const handleSignInClick = () => {
    setResetToken(undefined);
    setAuthDrawerOpen(true);
  };

  return (
    <div className="header">
      <div className="header-frame">
        <NavLink to="/" className="logo-link">
          <img className="header-logo" src={logo} alt="logo icon" />
        </NavLink>

        <ul className="header-list">
          {isAdmin && (
            <li>
              <NavLink
                to="/admin"
                className={({ isActive }) =>
                  `link${isActive ? " isActive" : ""}`
                }
              >
                Admin Panel
              </NavLink>
            </li>
          )}
          
          <li>
            <NavLink
              to="/currently-showing"
              className={({ isActive }) => `link${isActive ? " isActive" : ""}`}
            >
              Currently Showing
            </NavLink>
          </li>
          <li>
            <NavLink
              to="/upcoming"
              className={({ isActive }) => `link${isActive ? " isActive" : ""}`}
            >
              Upcoming Movies
            </NavLink>
          </li>
        </ul>

        <div className="header-auth">
          {isLoading ? (
            <div className="loading-user">Loading...</div>
          ) : isAuthenticated ? (
            <ProfileDropdown username={currentUser.email.split("@")[0]} />
          ) : (
            <Button
              label="Sign In"
              variant="navbar"
              onClick={handleSignInClick}
            />
          )}
        </div>

        <Authentication
          isOpen={authDrawerOpen}
          onClose={() => {
            setAuthDrawerOpen(false);
            setResetToken(undefined);
          }}
          resetToken={resetToken}
        />
      </div>
    </div>
  );
};
