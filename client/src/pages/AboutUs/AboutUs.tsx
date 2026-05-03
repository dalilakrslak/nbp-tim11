import theater_image from "../../assets/img/aboutus-cinema.jpg";

import "./aboutUs.scss";

export const AboutUs = () => {
  return (
    <div className="about-us-container">
      <div className="about-us-content">
        <aside className="about-us-left">
          <h5 className="about-us-left-text">About Our Dream.</h5>
          <h5 className="about-us-left-text">Our History.</h5>
          <h5 className="about-us-left-text">Cinema.</h5>
        </aside>

        <main className="about-us-main">
          <h1 className="about-us-header">About Us</h1>
          <section className="about-us-description">
            <p className="intro-text">
              Welcome to Cinebh, where movie magic comes to life.
            </p>
            <p className="intro-text">
              At Cinebh, we're not just about screening films; we're passionate
              about creating unforgettable cinematic experiences. Since our
              establishment, we've been dedicated to providing our audience with
              top-quality entertainment in a comfortable and welcoming
              environment. <br />
              Our state-of-the-art facilities boast the latest in audiovisual
              technology, ensuring that every movie is presented with stunning
              clarity and immersive sound. From the latest blockbusters to
              timeless classics, our diverse selection of films caters to every
              taste and preference.
            </p>

            <p className="intro-text">
              As a hub for community entertainment, we take pride in being more
              than just a cinema. <br /> Join us at Cinebh and discover why
              we're not just your average movie theater—we're your destination
              for cinematic excellence and entertainment bliss.
            </p>
          </section>
        </main>
      </div>

      <img className="theater-image" src={theater_image} alt="Cinebh theater" />
    </div>
  );
};
