package com.ittmdb.controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ittmdb.R;

public class MenuActivity extends AppCompatActivity {

    Button btnshowAllMovies;
    Button btnShowWatchedMovies;
    Button btnShowFavMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar customToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        customToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(customToolbar);

        btnshowAllMovies = (Button) findViewById(R.id.all_movies);
        goToActivity(btnshowAllMovies,AllMoviesActivity.class);

        btnShowWatchedMovies = (Button) findViewById(R.id.watched_movies);
        goToActivity(btnShowWatchedMovies,WatchedMoviesActivity.class);

        btnShowFavMovies = (Button) findViewById(R.id.fav_movies);
        goToActivity(btnShowFavMovies,FavMoviesActivity.class);

    }

    void goToActivity(Button btn, final Class activity){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),activity);
                startActivity(i);
            }
        });
    }
}
