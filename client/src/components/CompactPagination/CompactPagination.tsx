import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import ArrowForwardRoundedIcon from "@mui/icons-material/ArrowForwardRounded";

import { Button } from "../Button";

import "./compactPagination.scss";

export type CompactPaginationProps = {
  currentPage: number;
  totalElements: number;
  pageSize: number;
  onPageChange: (page: number) => void;
};

export const CompactPagination = ({
  currentPage,
  totalElements,
  pageSize,
  onPageChange,
}: CompactPaginationProps) => {
  const handlePrevPage = () => {
    if (currentPage > 0) {
      onPageChange(currentPage - 1);
    }
  };

  const handleNextPage = () => {
    if ((currentPage + 1) * pageSize < totalElements) {
      onPageChange(currentPage + 1);
    }
  };

  const start = currentPage * pageSize + 1;
  const end = Math.min((currentPage + 1) * pageSize, totalElements);

  const isFirstPage = currentPage === 0;
  const isLastPage = end >= totalElements;

  if (totalElements === 0) {
    return null;
  }

  return (
    <div className="pagination-container">
      <p className="pagination-info">
        Showing{" "}
        <b>
          {start}-{end}
        </b>{" "}
        out of <b>{totalElements}</b>
      </p>
      <div className="pagination-controls">
        <Button
          variant="tertiary"
          onClick={handlePrevPage}
          disabled={isFirstPage}
          aria-label="Previous page"
          icon={<ArrowBackRoundedIcon />}
        />
        <Button
          variant="tertiary"
          onClick={handleNextPage}
          disabled={isLastPage}
          aria-label="Next page"
          icon={<ArrowForwardRoundedIcon />}
        />
      </div>
    </div>
  );
};
