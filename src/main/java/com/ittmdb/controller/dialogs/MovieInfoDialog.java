package com.ittmdb.controller.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ittmdb.R;
import com.ittmdb.model.IMovieDao;
import com.ittmdb.model.Movie;
import com.ittmdb.model.MovieDataSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class MovieInfoDialog extends Dialog {

    Movie movie;

    Context context;

    TextView movieTitle;
    TextView movieYear;
    TextView movieDirector;
    TextView moviePlot;
    ImageView moviePoster;

    LinearLayout movieInfo;
    LinearLayout rootMovieInfo;
    ProgressBar movieInfoProgressBar;
    ProgressBar posterProgressBar;

    String chosenMovieTitle;
    String releasedYear;

    public MovieInfoDialog(Context context, String movieName, String released) {
        super(context);
        this.context = context;
        chosenMovieTitle = movieName;
        releasedYear = released.substring(released.length() - 4);
        Log.e("movie  Title", chosenMovieTitle + "'");
    }

    @Override
    public void show() {
        setTitle("Movie Info");
        setContentView(R.layout.dialog_single_movie_info);

        rootMovieInfo = (LinearLayout) findViewById(R.id.root_movie_info);
        rootMovieInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        movieInfo = (LinearLayout) findViewById(R.id.movie_info);
        movieInfo.setVisibility(View.GONE);

        movieInfoProgressBar = (ProgressBar) findViewById(R.id.movie_info_progressBar);
        posterProgressBar = (ProgressBar) findViewById(R.id.poster_loader);

        new ShowMovieInfoTask().execute(chosenMovieTitle, releasedYear);

        movieTitle = (TextView) findViewById(R.id.movie_info_title);
        movieYear = (TextView) findViewById(R.id.movie_info_year);
        movieDirector = (TextView) findViewById(R.id.movie_info_director);
        moviePlot = (TextView) findViewById(R.id.movie_info_plot);
        moviePoster = (ImageView) findViewById(R.id.movie_info_poster);
        moviePoster.setVisibility(View.GONE);



        super.show();
    }

    private class ShowMovieInfoTask extends AsyncTask<String, Void, Movie> {

        @Override
        protected Movie doInBackground(String... params) {
            try {
                String formattedTitle = params[0].trim().replaceAll("\\s+", "%20");
                if (formattedTitle.length()< 2 )
                    formattedTitle += "%20";
                String year = params[1];
                URL url = new URL("http://www.omdbapi.com/?t=" + formattedTitle + "&y=" + year + "&plot=short&r=json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                StringBuilder data = new StringBuilder();
                Scanner sc = new Scanner(connection.getInputStream());
                while (sc.hasNextLine()) {
                    data.append(sc.nextLine());
                }

                JSONObject jsonMovie = new JSONObject(data.toString());

                movie = new Movie();
                if (jsonMovie.getString("Response").equals("False")) {
                    return null;
                }
                String title = jsonMovie.getString("Title");
                int movieYear = 0;
                if (jsonMovie.getString("Year").matches("[0-9]*")) {
                    movieYear = Integer.parseInt(jsonMovie.getString("Year"));
                }
                String director = jsonMovie.getString("Director");
                String plot=jsonMovie.getString("Plot");

                movie.setTitle((title.length() <= 0) ? "N/A" : title);
                movie.setYear(movieYear);
                movie.setDirector((director.length() <= 0) ? "N/A" : director);

                movie.setPlot((plot.length() <= 0) ? "N/A" : plot);
                movie.setPosterUrl(jsonMovie.getString("Poster"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {

            if (movie == null){
                setTitle("NO SUCH MOVIE in IMDB");
                return;
            }
            movieInfo.setVisibility(View.VISIBLE);
            movieTitle.setText(movie.getTitle());
            movieYear.setText(String.valueOf(movie.getYear()));
            movieDirector.append(movie.getDirector());
            moviePlot.setText(movie.getPlot());
            if (!movie.getPosterUrl().equals("N/A")) {
                new ImageLoadTask().execute(movie.getPosterUrl());
            } else
                moviePoster.setVisibility(View.VISIBLE);
            movieInfoProgressBar.setVisibility(View.GONE);
        }
    }

    private class ImageLoadTask extends AsyncTask<String, Void, Drawable> {
        @Override
        protected Drawable doInBackground(String... params) {
            Drawable poster = null;
            try {
                URL posterUrl = new URL(params[0]);
                poster = Drawable.createFromStream(posterUrl.openStream(), "src");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return poster;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            moviePoster.setImageDrawable(drawable);
            posterProgressBar.setVisibility(View.GONE);
            moviePoster.setVisibility(View.VISIBLE);
        }
    }
}
