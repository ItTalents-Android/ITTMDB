package com.ittmdb.controller.adapters;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ittmdb.R;
import com.ittmdb.controller.dialogs.MovieInfoDialog;
import com.ittmdb.controller.dialogs.MovieNotesDialog;
import com.ittmdb.model.IMovieDao;
import com.ittmdb.model.Movie;
import com.ittmdb.model.MovieDataSource;

import java.util.ArrayList;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.CustomViewHolder> {

    private Activity activity;
    private ArrayList<Movie> movies;
    private IMovieDao movieDataSource;

    private ArrayList<Boolean> checkboxFavsStates;
    private ArrayList<Boolean> checkboxWatchedStates;
    private ArrayList<Boolean> toggleStates;

    private String sourceType;

    private TextView labelForEmptyList;

    public MovieRecyclerAdapter(Activity activity, ArrayList<Movie> movies, String sourceType) {
        this.activity = activity;
        this.movies = movies;
        movieDataSource = new MovieDataSource();
        this.sourceType = sourceType;

        labelForEmptyList = (TextView) activity.findViewById(R.id.label_for_empty_list);

        this.checkboxFavsStates = new ArrayList<>();
        this.checkboxWatchedStates = new ArrayList<>();
        this.toggleStates = new ArrayList<>();

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.layout_movie, parent, false);
        return new CustomViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        final Movie movie = movies.get(position);

        holder.txtMovieTitle.setText(movie.getTitle());
        holder.txtMovieRated.setText(" [" + movie.getRated() + "]");
        holder.txtMovieReleased.setText(movie.getReleased());
        holder.txtMovieGenre.setText(movie.getGenre());
        holder.txtMovieRuntime.setText(movie.getRuntime());

        //  save the checkbutton state, and not copy it to other
        if (position == checkboxFavsStates.size()) {
            checkboxFavsStates.add(false);
            checkboxWatchedStates.add(false);
            toggleStates.add(false);
        }

        holder.switchMovieEditMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.linearLayoutMovieEditMode.setVisibility(View.VISIBLE);
                    toggleStates.set(position, true);
                } else {
                    holder.linearLayoutMovieEditMode.setVisibility(View.GONE);
                    toggleStates.set(position, false);
                }
            }
        });

        holder.switchMovieEditMode.setChecked(toggleStates.get(position));

        holder.chbFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxFavsStates.set(position, true);
                    new ModifyMovieFavsListTask("add").execute(position);
                } else {
                    checkboxFavsStates.set(position, false);
                    new ModifyMovieFavsListTask("remove").execute(position);
                }
            }
        });

        holder.chbWatched.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxWatchedStates.set(position, true);
                    new ModifyMovieWatchedListTask("add").execute(position);
                } else {
                    checkboxWatchedStates.set(position, false);
                    new ModifyMovieWatchedListTask("remove").execute(position);
                }
            }
        });

        if (movies.get(position).isWatchedMovie()) {
            checkboxWatchedStates.set(position, true);
        }
        if (movies.get(position).isFavMovie()) {
            checkboxFavsStates.set(position, true);
        }

//        setting the colors
        holder.chbWatched.setChecked(checkboxWatchedStates.get(position));
        if (holder.chbWatched.isChecked()) {
            holder.movieHolder.setBackgroundResource(R.color.colorWatched);
            holder.chbWatched.setText("Remove from Watched List");
        } else {
            holder.chbWatched.setText("Add to Watched List");
        }
        holder.chbFav.setChecked(checkboxFavsStates.get(position));
        if (holder.chbFav.isChecked()) {
            holder.movieHolder.setBackgroundResource(R.color.colorFav);
            holder.chbFav.setText("Remove from Fav List");
        } else {
            holder.chbFav.setText("Add to Fav List");
        }
        if (!holder.chbFav.isChecked() && !holder.chbWatched.isChecked()) {
            holder.movieHolder.setBackgroundResource(R.color.cardViewLight);
        }
        if (holder.chbFav.isChecked() && holder.chbWatched.isChecked())
            holder.movieHolder.setBackgroundResource(R.color.colorFavWatched);

//        setting functionality
        if (sourceType.equals("watched")){
            holder.movieHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MovieInfoDialog(activity,movie.getTitle(),movie.getReleased()).show();
                }
            });
        }
        if (sourceType.equals("fav")){
            holder.movieHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MovieNotesDialog(activity,movie.getTitle()).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return movies.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txtMovieTitle;
        TextView txtMovieReleased;
        TextView txtMovieGenre;
        TextView txtMovieRuntime;
        TextView txtMovieRated;
        CheckBox chbFav;
        CheckBox chbWatched;
        LinearLayout linearLayoutMovieEditMode;
        Switch switchMovieEditMode;
        LinearLayout movieHolder;

        CustomViewHolder(View v) {
            super(v);

            linearLayoutMovieEditMode = (LinearLayout) v.findViewById(R.id.movie_edit_prefs);
            linearLayoutMovieEditMode.setVisibility(View.GONE);
            switchMovieEditMode = (Switch) v.findViewById(R.id.switch_edit_mode);

            movieHolder = (LinearLayout) v.findViewById(R.id.movie_card);

            txtMovieTitle = (TextView) v.findViewById(R.id.movie_title);
            txtMovieReleased = (TextView) v.findViewById(R.id.movie_released_date);
            txtMovieGenre = (TextView) v.findViewById(R.id.movie_genre);
            txtMovieRuntime = (TextView) v.findViewById(R.id.movie_runtime);
            txtMovieRated = (TextView) v.findViewById(R.id.movie_rated);
            chbFav = (CheckBox) v.findViewById(R.id.chb_fav);
            chbWatched = (CheckBox) v.findViewById(R.id.chb_watched);
            if (sourceType.equals("fav"))
                chbWatched.setVisibility(View.GONE);
        }

    }

    class ModifyMovieFavsListTask extends AsyncTask<Integer, Void, Void> {

        private String function;


        ModifyMovieFavsListTask(String function) {
            this.function = function;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            if (function.equals("add")) {
                if (!movieDataSource.showFavMoviesLIst().contains(movies.get(params[0]))) {
                    movies.get(params[0]).setIsFavMovie(true);
                    movieDataSource.setIsFavMovie(movies.get(params[0]), true);
                    movieDataSource.addToFavsList(movies.get(params[0]));
                }
            }
            if (function.equals("remove")) {
                movies.get(params[0]).setIsFavMovie(false);
                movieDataSource.removeFromFavsList(movies.get(params[0]));
                if (sourceType.equals("fav"))
                    movies.remove(movies.get(params[0]));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (movies.size() <= 0)
                labelForEmptyList.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }
    }

    class ModifyMovieWatchedListTask extends AsyncTask<Integer, Void, Void> {

        private String function;

        ModifyMovieWatchedListTask(String function) {
            this.function = function;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            if (function.equals("add")) {
                if (!movieDataSource.showWatchedMoviesLIst().contains(movies.get(params[0]))) {
                    movies.get(params[0]).setIsWatchedMovie(true);
                    movieDataSource.setIsWatchedMovie(movies.get(params[0]), true);
                    movieDataSource.addToWatchedList(movies.get(params[0]));
                }
            }
            if (function.equals("remove")) {
                movies.get(params[0]).setIsWatchedMovie(false);
                movieDataSource.setIsWatchedMovie(movies.get(params[0]), false);
                movieDataSource.removeFromWatchList(movies.get(params[0]));
                if (sourceType.equals("watched"))
                    movies.remove(movies.get(params[0]));

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (movies.size() <= 0)
                labelForEmptyList.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }
    }


}
