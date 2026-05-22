import { useState } from "react";
import { Link } from "react-router-dom";

import { Card } from "../Card";
import { useSimilarMovies } from "../../hooks";
import { Loading } from "../Loading";
import { CompactPagination } from "../CompactPagination";

import "./seeAlsoList.scss";

const PAGE_SIZE = 6;

export type SeeAlsoListProps = {
  movieId?: string;
};

export const SeeAlsoList = ({ movieId }: SeeAlsoListProps) => {
  const [currentPage, setCurrentPage] = useState<number>(0);

  const {
    data: similarMovies,
    isLoading: isSimilarMoviesLoading,
    isError: isSimilarMoviesError,
  } = useSimilarMovies(movieId ?? "", {
    page: currentPage,
    size: PAGE_SIZE,
  });

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  if (isSimilarMoviesLoading) return <Loading />;
  if (isSimilarMoviesError) {
    return (
      <div className="see-also-no-data">Failed to load similar movies.</div>
    );
  }

  if (!similarMovies?.content?.length) {
    return <div className="see-also-no-data">No similar movies found.</div>;
  }

  return (
    <div className="see-also-list-container">
      <div className="see-also-list-content">
        <div className="see-also-list-title">
          <p>See Also</p>
        </div>
        <div className="see-also-list">
          {similarMovies?.content.map((movie) => {
            const coverImage =
              movie.photos.find((photo) => photo.isCoverImage) ||
              movie.photos[0];

            return (
              <Link
                to={`/movies/${movie.id}`}
                key={movie.id}
                className="card-link-wrapper"
                onClick={() => {
                  window.scrollTo(0, 0);
                }}
              >
                <Card
                  key={movie.id}
                  photoUrl={coverImage?.url || ""}
                  title={movie.title}
                  size="small"
                />
              </Link>
            );
          })}
        </div>
        <div className="see-also-list-pagination">
          <CompactPagination
            currentPage={currentPage}
            totalElements={similarMovies?.page.totalElements || 0}
            pageSize={PAGE_SIZE}
            onPageChange={handlePageChange}
          />
        </div>
      </div>
    </div>
  );
};
