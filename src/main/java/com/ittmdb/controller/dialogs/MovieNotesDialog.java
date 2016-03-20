package com.ittmdb.controller.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ittmdb.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieNotesDialog extends Dialog {

    Context context;
    String movieTitle;
    String fileName;
    File movieDir;
    File movieNote;


    EditText edtMovieNote;
    Button btnCancel;
    Button btnSaveMovieNote;

    public MovieNotesDialog(Context context, String title) {
        super(context);
        this.context = context;
        this.movieTitle = title;
    }

    @Override
    public void show() {
        setTitle(movieTitle);
        setContentView(R.layout.dialog_movie_comments);

        fileName = movieTitle.trim().replaceAll("\\s+", "_");

        movieDir = new File(context.getFilesDir().getPath() + File.separator + fileName);

        movieNote = new File(movieDir, fileName + ".note");

        edtMovieNote = (EditText) findViewById(R.id.movie_note);
        btnCancel = (Button) findViewById(R.id.btn_movie_note_cancel);

        new LoadNoteTask().execute(movieTitle);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSaveMovieNote = (Button) findViewById(R.id.btn_movie_note_save);
        btnSaveMovieNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveNoteToFile().execute(edtMovieNote.getText().toString());
            }
        });

        super.show();
    }

    private class SaveNoteToFile extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            movieDir.mkdir();
            boolean success;

            BufferedWriter writer = null;
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(movieNote);
                writer = new BufferedWriter(fileWriter);
                writer.write(params[0]);
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Save aborted", Toast.LENGTH_SHORT).show();
            dismiss();

        }
    }

    private class LoadNoteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            FileReader fileReader = null;
            BufferedReader bufferedReader = null;

            StringBuilder currentNote = new StringBuilder();

            String line;
            try {
                fileReader = new FileReader(movieNote);
                bufferedReader = new BufferedReader(fileReader);
                while ((line = bufferedReader.readLine()) != null) {

                    currentNote.append(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                        fileReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return currentNote.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            edtMovieNote.setText(s);
            edtMovieNote.setSelection(edtMovieNote.getText().length());

        }
    }
}
