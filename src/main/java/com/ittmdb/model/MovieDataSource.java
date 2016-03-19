package com.ittmdb.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user-23 on 3/17/16.
 */
public class MovieDataSource implements IMovieDao{

    static ArrayList<Movie> allMovies = new ArrayList<>();
    static ArrayList<Movie> watchedmovies = new ArrayList<>();
    static ArrayList<Movie> favMovies = new ArrayList<>();

    @Override
    public Movie setMovie(String title, String rated, String released, String genre, String runtime) {
        return new Movie(title,rated,released,genre,runtime);
    }

    @Override
    public Movie getMovieByTitle(String title) {
        for (Movie m :
                allMovies) {
            if (m.getTitle().equals(title))
                return m;
        }
        return null;
    }

    @Override
    public void addToMoviesDB(Movie movie) {
        allMovies.add(movie);
    }

    @Override
    public ArrayList<Movie> showAllMovies() {
        return allMovies;
    }

    @Override
    public void setIsWatchedMovie(Movie movie,boolean bool) {
        allMovies.get(allMovies.indexOf(movie)).setIsWatchedMovie(bool);
    }

    @Override
    public void setIsFavMovie(Movie movie, boolean bool) {
        allMovies.get(allMovies.indexOf(movie)).setIsFavMovie(bool);
    }

    @Override
    public void setMovieInfo(Movie movie,int year, String posterUrl, String director, String plot) {
        movie.setYear(year);
        movie.setPosterUrl(posterUrl);
        movie.setDirector(director);
        movie.setPlot(plot);
    }

    @Override
    public void addToWatchedList(Movie movie) {
        movie.setIsWatchedMovie(true);
        watchedmovies.add(movie);
    }

    @Override
    public void removeFromWatchList(Movie movie) {
        watchedmovies.remove(movie);
    }

    @Override
    public ArrayList<Movie> showWatchedMoviesLIst() {
        return watchedmovies;
    }

    @Override
    public void addToFavsList(Movie movie) {
        movie.setIsFavMovie(true);
        favMovies.add(movie);
    }

    @Override
    public void removeFromFavsList(Movie movie) {
        favMovies.remove(movie);
    }

    @Override
    public ArrayList<Movie> showFavMoviesLIst() {
        return favMovies;
    }
}
