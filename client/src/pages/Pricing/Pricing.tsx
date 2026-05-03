import { PricingInfo } from "../../components";

import "./pricing.scss";

export const Pricing = () => {
  return (
    <div className="pricing-container">
      <div className="pricing-header">
        <h4>Pricing</h4>
        <p className="pricing-header-text">
          Welcome to our cinema ticket pricing options! We offer three tiers to
          suit everyone’s preferences. Explore our pricing options below and
          treat yourself to a cinematic adventure like never before!
        </p>
      </div>
      <div className="pricing-content">
        <PricingInfo
          title="Regular Seats"
          price={7}
          buttonVariant="secondary"
          features={[
            "Comfortable seating",
            "Affordable pricing",
            "Wide selection",
            "Accessible locations",
            "Suitable for everyone",
          ]}
        />

        <PricingInfo
          title="Love Seats"
          price={24}
          buttonVariant="primary"
          features={[
            "Side-by-side design",
            "Comfortable padding",
            "Adjustable armrests",
            "Cup holders",
            "Reserved for couples",
          ]}
          className="love"
        />

        <PricingInfo
          title="Vip Seats"
          price={10}
          buttonVariant="secondary"
          features={[
            "Enhanced comfort",
            "Priority seating",
            "Prime viewing",
            "Personal space",
            "Luxury extras",
          ]}
        />
      </div>
    </div>
  );
};
