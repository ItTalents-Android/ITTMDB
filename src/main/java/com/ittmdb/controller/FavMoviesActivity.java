package com.ittmdb.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ittmdb.R;
import com.ittmdb.controller.adapters.MovieRecyclerAdapter;
import com.ittmdb.model.IMovieDao;
import com.ittmdb.model.Movie;
import com.ittmdb.model.MovieDataSource;

import java.util.ArrayList;

public class FavMoviesActivity extends AppCompatActivity {

    RecyclerView recyclerWatchedMovies;

    ArrayList<Movie> favMovies;

    MovieRecyclerAdapter mra;

    private TextView labelForEmptyList;

    LinearLayout loadDataProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_movies);

        favMovies = new ArrayList<>();

        Toolbar customToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        customToolbar.setTitle("Fav movies");
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
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                finish();
            }
        });

        recyclerWatchedMovies = (RecyclerView) findViewById(R.id.recycler_watched_movies);
        mra = new MovieRecyclerAdapter(this, favMovies,"fav");
        recyclerWatchedMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerWatchedMovies.setAdapter(mra);

        new LoadFavMoviesList().execute();

        labelForEmptyList = (TextView) findViewById(R.id.label_for_empty_list);
        labelForEmptyList.setVisibility(View.GONE);

        loadDataProgress = (LinearLayout) findViewById(R.id.movie_load_progress);
    }

    class LoadFavMoviesList extends AsyncTask<Void,Void,Void> {

        IMovieDao movieDataSource;

        @Override
        protected Void doInBackground(Void... params) {
            movieDataSource = new MovieDataSource();
            favMovies.addAll(movieDataSource.showFavMoviesLIst());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadDataProgress.setVisibility(View.GONE);
            if (mra.getItemCount()<=0)
                labelForEmptyList.setVisibility(View.VISIBLE);
            mra.notifyDataSetChanged();
        }
    }
}
