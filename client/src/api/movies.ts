import { axiosApp } from "./axiosApp";
import type { Movie, MovieFiltersWithPageable, MovieListItem, PageResponse } from "./types";
import type { Pageable, ResponseType } from "../utils";

export type MovieRating = {
  id: string;
  name: string;
  rating: string;
};

export const getLatestMovies = async (): Promise<ResponseType<Movie>> => {
  const response = await axiosApp.get("/movies/latest");

  return response.data;
};

export const getCurrentlyShowingMovies = async (
  query: MovieFiltersWithPageable
): Promise<ResponseType<Movie>> => {
  const response = await axiosApp.get("/movies/currently-showing", {
    params: { ...query },
  });

  return response.data;
};

export const getUpcomingMovies = async (
  pageable: Pageable
): Promise<ResponseType<Movie>> => {
  const response = await axiosApp.get("/movies/upcoming", {
    params: { ...pageable },
  });

  return response.data;
};

export const getMovieById = async (movieId: string): Promise<Movie> => {
  const response = await axiosApp.get(`/movies/${movieId}`);
  return response.data;
};

export const getSimilarMovies = async (
  movieId: string,
  pageable: Pageable
): Promise<ResponseType<Movie>> => {
  const response = await axiosApp.get(`/movies/${movieId}/similar`, {
    params: { ...pageable },
  });

  return response.data;
};

export const getMovieRatings = async (
  movieId: string
): Promise<MovieRating[]> => {
  const response = await axiosApp.get(`/movies/${movieId}/ratings`);
  return response.data;
};

export const getAdminMovies = async (
  pageable: Pageable
): Promise<PageResponse<MovieListItem>> => {
  const response = await axiosApp.get("/movies", {
    params: { ...pageable },
  });

  return response.data;
};

export const createMovie = async (
  payload: Omit<MovieListItem, "id">
): Promise<MovieListItem> => {
  const response = await axiosApp.post("/movies", payload);
  return response.data;
};

export const updateMovie = async (
  id: string,
  payload: Omit<MovieListItem, "id">
): Promise<MovieListItem> => {
  const response = await axiosApp.put(`/movies/${id}`, payload);
  return response.data;
};

export const deleteMovie = async (id: string): Promise<void> => {
  await axiosApp.delete(`/movies/${id}`);
};