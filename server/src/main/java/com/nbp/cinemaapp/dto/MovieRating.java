package com.nbp.cinemaapp.dto;

public class MovieRating {

    private final String id;
    private final String name;
    private final String rating;

    public MovieRating(final String id, final String name, final String rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public MovieRating(final String sourceName, final String rating) {
        this.id = sourceName.toLowerCase().replaceAll("\\s+", "");
        this.name = sourceName;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }
}
