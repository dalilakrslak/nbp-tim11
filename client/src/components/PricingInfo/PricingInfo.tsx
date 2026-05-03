import { Link } from "react-router-dom";
import CheckIcon from "@mui/icons-material/Check";

import { Button } from "../Button";
import { ButtonProps } from "../Button";

import "./pricingInfo.scss";

export type PricingInfoProps = {
  title: string;
  price: number;
  features: string[];
  className?: string;
  buttonVariant: ButtonProps["variant"];
};

export const PricingInfo = ({
  title,
  price,
  features,
  className = "",
  buttonVariant,
}: PricingInfoProps) => {
  return (
    <div className={`seats-card ${className}`}>
      <div className="seats-info">
        <h6 className="seats-header">{title}</h6>
        <h4 className="price">{price} KM</h4>
        <p>*per ticket</p>
      </div>
      <ul className="features-list">
        {features.map((feature, index) => (
          <li className="feature-item" key={index}>
            <CheckIcon className="check-icon" />
            <p>{feature}</p>
          </li>
        ))}
      </ul>
      <Link to="/" onClick={() => window.scrollTo(0, 0)}>
        <Button
          variant={buttonVariant}
          label="Explore Movies"
          className="explore-movies-button"
        />
      </Link>
    </div>
  );
};
