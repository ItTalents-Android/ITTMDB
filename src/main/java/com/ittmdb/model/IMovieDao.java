package com.ittmdb.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user-23 on 3/17/16.
 */
public interface IMovieDao {

    Movie setMovie(String title, String rated, String released, String genre, String runtime);

    Movie getMovieByTitle(String title);

    void addToMoviesDB(Movie movie);

    ArrayList<Movie> showAllMovies();

    void setIsWatchedMovie(Movie movie,boolean bool);

    void setIsFavMovie(Movie movie,boolean bool);

    void setMovieInfo(Movie movie, int year, String posterUrl, String director, String plot);

    void addToWatchedList(Movie movie);

    void removeFromWatchList(Movie movie);

    ArrayList<Movie> showWatchedMoviesLIst();

    void addToFavsList(Movie movie);

    void removeFromFavsList(Movie movie);

    ArrayList<Movie> showFavMoviesLIst();

}
