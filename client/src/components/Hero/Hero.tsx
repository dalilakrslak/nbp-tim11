import { Carousel } from "antd";

import { useLatestMovies } from "../../hooks";
import { HeroItem } from "../HeroItem";

import "./hero.scss";

export const Hero = () => {
  const { data: latestMovies } = useLatestMovies();

  return (
    <div className="hero">
      <div className="carousel-container">
        <Carousel autoplay>
          {latestMovies?.content.map((movie) => (
            <HeroItem key={movie.id} movie={movie} />
          ))}
        </Carousel>
      </div>
    </div>
  );
};
