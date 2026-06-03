package com.nbp.cinemaapp.dto.response;

public class MovieBulkImportResponse {

    private final int importedMovies;

    public MovieBulkImportResponse(final int importedMovies) {
        this.importedMovies = importedMovies;
    }

    public int getImportedMovies() {
        return importedMovies;
    }
}
