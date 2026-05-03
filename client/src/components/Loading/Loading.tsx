import { Skeleton } from "antd";
import "./loading.scss";

export type LoadingProps = {
  rows?: number;
};

export const Loading = ({ rows = 3 }: LoadingProps) => {
  return (
    <div className="loading-container">
      <Skeleton active title={{ width: "60%" }} paragraph={{ rows }} />
    </div>
  );
};
