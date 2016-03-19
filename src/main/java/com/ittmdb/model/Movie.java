package com.ittmdb.model;

import java.util.Date;

public class Movie {


    private String title;
    private String rated;
    private String released;
    private String genre;
    private String runtime;
    private boolean isFavMovie;
    private boolean isWatchedMovie;

//    Movie info
    private int year;
    private String posterUrl;
    private String director;
    private String plot;


//    For testing empty constr
    public Movie(){}


    public Movie(String title, String rated, String released, String genre, String runtime) {
        this.title = title;
        this.rated = rated;
        this.released = released;
        this.genre = genre;
        this.runtime = runtime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isWatchedMovie() {
        return isWatchedMovie;
    }

    public void setIsWatchedMovie(boolean isWatchedMovie) {
        this.isWatchedMovie = isWatchedMovie;
    }

    public boolean isFavMovie() {
        return isFavMovie;
    }

    public void setIsFavMovie(boolean isFavMovie) {
        this.isFavMovie = isFavMovie;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getRated() {
        return rated;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (!title.equals(movie.title)) return false;
        if (!released.equals(movie.released)) return false;
        return genre.equals(movie.genre);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + released.hashCode();
        result = 31 * result + genre.hashCode();
        return result;
    }
}
