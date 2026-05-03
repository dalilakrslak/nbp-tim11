import { Button } from "../Button";
import "./error.scss";

export type ErrorProps = {
  type?: "connection" | "notFound" | "unauthorized" | "server" | "generic";
  message?: string;
  onRetry?: () => void;
  showRetry?: boolean;
  size?: "small" | "large";
};

export const Error = ({
  type = "connection",
  message,
  onRetry,
  showRetry = true,
  size = "large",
}: ErrorProps) => {
  const getErrorContent = () => {
    if (message) return message;

    switch (type) {
      case "notFound":
        return "The requested movie was not found";
      case "unauthorized":
        return "You don't have permission to access this content";
      case "server":
        return "Our servers are experiencing issues. Please try again later";
      case "connection":
        return "Unable to connect to our servers. Check your internet connection";
      default:
        return "Something went wrong. Please try again";
    }
  };

  const getErrorTitle = () => {
    switch (type) {
      case "notFound":
        return "Movie Not Found";
      case "unauthorized":
        return "Access Denied";
      case "server":
        return "Server Error";
      case "connection":
        return "Connection Error";
      default:
        return "Error";
    }
  };

  const handleAction = () => {
    if (onRetry) {
      onRetry();
    } else {
      window.location.reload();
    }
  };

  return (
    <div className={`error-container error-container--${size}`}>
      <div className="error-content">
        <h3 className="error-title">{getErrorTitle()}</h3>
        <p className="error-message">{getErrorContent()}</p>
        {showRetry && (
          <Button label="Try again" variant="primary" onClick={handleAction} />
        )}
      </div>
    </div>
  );
};
