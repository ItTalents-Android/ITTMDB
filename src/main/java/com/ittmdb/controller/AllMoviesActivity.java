package com.ittmdb.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ittmdb.R;
import com.ittmdb.controller.adapters.MovieRecyclerAdapter;
import com.ittmdb.controller.dialogs.ColorLegendDialog;
import com.ittmdb.model.IMovieDao;
import com.ittmdb.model.Movie;
import com.ittmdb.model.MovieDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class AllMoviesActivity extends AppCompatActivity {


    IMovieDao mDS;

    RecyclerView recyclerViewAllMovies;

    ArrayList<Movie> movies;

    MovieRecyclerAdapter mra;

    private TextView labelForEmptyList;

    LinearLayout loadDataProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);

        Toolbar customToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        customToolbar.setTitle("All movies");
        customToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(customToolbar);
//        Shows the back arrow in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        customToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllMoviesActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });


        movies = new ArrayList<>();

        mDS = new MovieDataSource();

        recyclerViewAllMovies = (RecyclerView) findViewById(R.id.recycler_all_movies);
        String movieURLDataSource = "http://www36.imperiaonline.org/movies.php";

        new LoadMoveListTask().execute(movieURLDataSource);


        mra = new MovieRecyclerAdapter(AllMoviesActivity.this, movies, "all");
        recyclerViewAllMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAllMovies.setAdapter(mra);

        Button btnShowLegend = (Button) findViewById(R.id.btn_color_legend);
        btnShowLegend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorLegendDialog(AllMoviesActivity.this).show();
            }
        });

        labelForEmptyList = (TextView) findViewById(R.id.label_for_empty_list);
        labelForEmptyList.setVisibility(View.GONE);

        loadDataProgress = (LinearLayout) findViewById(R.id.movie_load_progress);
    }

    class LoadMoveListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String address = params[0];
            String response = "";

            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while (sc.hasNextLine()) {
                    body.append(sc.nextLine());
                }
                response = String.valueOf(body);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            loadDataProgress.setVisibility(View.GONE);
            try {
                IMovieDao allMovies = new MovieDataSource();
                JSONArray response = new JSONArray(s);
                IMovieDao movieDataSource = new MovieDataSource();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject movieJson = response.getJSONObject(i);
                    Movie movie = movieDataSource.setMovie(movieJson.getString("Title"), movieJson.getString("Rated"), movieJson.getString("Released"), movieJson.getString("Genre"), movieJson.getString("Runtime"));
                    if (!allMovies.showAllMovies().contains(movie))
                        allMovies.addToMoviesDB(movie);
                }

                Collections.sort(allMovies.showAllMovies(), new Comparator<Movie>() {
                    @Override
                    public int compare(Movie lhs, Movie rhs) {
                        return lhs.getTitle().compareTo(rhs.getTitle());
                    }
                });
                movies.addAll(allMovies.showAllMovies());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mra.getItemCount() <= 0)
                labelForEmptyList.setVisibility(View.VISIBLE);
            mra.notifyDataSetChanged();
        }
    }
}
