import { useState } from "react";
import { FaPlay } from "react-icons/fa";

import { Loading } from "../Loading";
import type { Photo } from "../../api";

import "./movieMedia.scss";

// Converts YouTube URLs to embeddable format for iframe usage
const getEmbedUrl = (url?: string) => {
  if (!url) return "";

  // Match YouTube URLs in both youtube.com/watch?v= and youtu.be/ formats
  const match = url.match(
    /(?:youtube\.com\/watch\?v=|youtu\.be\/)([a-zA-Z0-9_-]+)/
  );

  // Extract the video ID from the matched URL
  const videoId = match?.[1];

  // Return embeddable YouTube URL with autoplay enabled, or empty string if no video ID found
  return videoId ? `https://www.youtube.com/embed/${videoId}?autoplay=1` : "";
};

export type MovieMediaProps = {
  photos?: Photo[];
  trailerUrl?: string;
};

export const MovieMedia = ({ trailerUrl, photos }: MovieMediaProps) => {
  const [playTrailer, setPlayTrailer] = useState<boolean>(false);
  const [trailerLoaded, setTrailerLoaded] = useState<boolean>(false);
  const [coverLoaded, setCoverLoaded] = useState<boolean>(false);
  const [loadedImages, setLoadedImages] = useState<Record<string, boolean>>({});

  const coverImage = photos?.find((p) => p.isCoverImage) || photos?.[0];
  const embedUrl = getEmbedUrl(trailerUrl);

  const handlePhotoLoad = (id: string) => {
    setLoadedImages((prev) => ({ ...prev, [id]: true }));
  };

  const handlePlayTrailer = () => {
    if (!playTrailer && embedUrl) {
      setPlayTrailer(true);
      setTrailerLoaded(false);
    }
  };

  const handleTrailerLoad = () => {
    setTrailerLoaded(true);
  };

  return (
    <div className="media-container">
      <div
        className={`media-trailer ${embedUrl ? "clickable" : ""}`}
        onClick={embedUrl ? handlePlayTrailer : undefined}
      >
        {playTrailer && embedUrl ? (
          <div className="media-trailer-wrapper">
            {!trailerLoaded && (
              <div className="media-trailer-loader">
                <Loading />
              </div>
            )}
            <iframe
              src={embedUrl}
              title="Movie Trailer"
              allowFullScreen
              allow="autoplay"
              className={`media-trailer-iframe ${
                trailerLoaded ? "visible" : "hidden"
              }`}
              onLoad={handleTrailerLoad}
            />
          </div>
        ) : (
          coverImage && (
            <div className="media-trailer-cover">
              {!coverLoaded && (
                <div className="media-image-loader">
                  <Loading />
                </div>
              )}
              <img
                src={coverImage.url}
                alt="Trailer Cover"
                className={`media-trailer-image ${
                  coverLoaded ? "visible" : "hidden"
                }`}
                onLoad={() => setCoverLoaded(true)}
              />
              {coverLoaded && embedUrl && (
                <div className="play-button">
                  <FaPlay className="play-icon" />
                </div>
              )}
            </div>
          )
        )}
      </div>

      <div className="media-photos">
        {photos
          ?.filter((photo) => photo.id !== coverImage?.id)
          .map((photo) => (
            <div key={photo.id} className="media-photo-wrapper">
              {!loadedImages[photo.id] && (
                <div className="media-image-loader">
                  <Loading />
                </div>
              )}
              <img
                src={photo.url}
                alt="Movie Photo"
                className={`media-photo ${
                  loadedImages[photo.id] ? "visible" : "hidden"
                }`}
                onLoad={() => handlePhotoLoad(photo.id)}
              />
            </div>
          ))}
      </div>
    </div>
  );
};
