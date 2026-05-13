import { LoadingOutlined } from "@ant-design/icons";
import { Skeleton, Spin } from "antd";
import type { SpinProps } from "antd";

import "./loading.scss";

export type LoadingProps = {
  rows?: number;
  size?: SpinProps["size"];
};

export const Loading = ({ rows = 3, size }: LoadingProps) => {
  if (size) {
    return (
      <div className="loading-container">
        <Spin indicator={<LoadingOutlined spin />} size={size} />
      </div>
    );
  }

  return (
    <div className="loading-container">
      <Skeleton active title={{ width: "60%" }} paragraph={{ rows }} />
    </div>
  );
};
