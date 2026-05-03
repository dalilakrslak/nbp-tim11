import { Link } from "react-router-dom";

import { CompactPagination } from "../CompactPagination";

import "./homepageGridSection.scss";

export type HomepageSectionProps = {
  title: string;
  className?: string;
  children: React.ReactNode;
  itemsPerPage: number;
  currentPage: number;
  totalElements: number;
  onPageChange: (page: number) => void;
  link?: string;
};

export function HomepageGridSection({
  title,
  className = "",
  children,
  itemsPerPage,
  currentPage,
  totalElements,
  onPageChange,
  link,
}: HomepageSectionProps) {
  return (
    <div className={`section-container ${className}`}>
      <div className="section-header">
        <p className="section-title">{title}</p>
        {link && (
          <Link
            to={link}
            className="section-see-all"
            onClick={() => window.scrollTo(0, 0)}
          >
            See All
          </Link>
        )}
      </div>
      <div className="section-content">{children}</div>

      <div className="section-pagination">
        <CompactPagination
          currentPage={currentPage}
          totalElements={totalElements}
          pageSize={itemsPerPage}
          onPageChange={onPageChange}
        />
      </div>
    </div>
  );
}
