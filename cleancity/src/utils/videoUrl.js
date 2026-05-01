const BASE_URL = "http://localhost:8080";

export const getVideoUrl = (videoPath) => {
  if (!videoPath) return "";

  if (videoPath.startsWith("http")) {
    return videoPath;
  }

  const cleanPath = videoPath.startsWith("/")
    ? videoPath.substring(1)
    : videoPath;

  return `${BASE_URL}/${cleanPath}`;
};