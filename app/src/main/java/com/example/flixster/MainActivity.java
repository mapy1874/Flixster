package com.example.flixster;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.ComplexMovieAdapter;
import model.Movie;
import okhttp3.Headers;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    List<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movies = new ArrayList<>();
        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        // create the adapter
        final ComplexMovieAdapter complexMovieAdapter= new ComplexMovieAdapter(this, movies);
        // set the adapter on the recyclerview
        rvMovies.setAdapter(complexMovieAdapter);
        // set a layout manager
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
           @Override
           public void onSuccess(int statusCode, Headers headers, JSON json) {
               Log.d(TAG, "onSuccess");
               JSONObject jsonObject =  json.jsonObject;
               try {
                   JSONArray results = jsonObject.getJSONArray("results");
                   Log.i(TAG, "Results"+results.toString());
                   movies.addAll(Movie.fromJSONArray(results));
                   complexMovieAdapter.notifyDataSetChanged();
                   Log.i(TAG, "Movies size:"+movies.size());
               } catch (JSONException e) {
                   Log.e(TAG, "Hit json exception", e);
               }
           }

           @Override
           public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                   Log.d(TAG, "onFailure");
           }
       });

    }
}
