import { Link } from "react-router-dom";

import logo_white from "../../assets/img/Logo - white.png";

import "./footer.scss";

export const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-frame">
        <img id="logo" src={logo_white} alt="footer logo" />
        <p className="info">
          <Link
            to="/about"
            className="footer-link"
            onClick={() => window.scrollTo(0, 0)}
          >
            ABOUT US
          </Link>
          <span className="separator">|</span>
          <Link
            to="/pricing"
            className="footer-link"
            onClick={() => window.scrollTo(0, 0)}
          >
            TICKETS
          </Link>
        </p>
        <p className="copyright">
          Copyright @Cinebh. Built with love in Sarajevo. All rights reserved.
        </p>
      </div>
    </footer>
  );
};
