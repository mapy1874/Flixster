package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

// pure java file, hold a movie
@Parcel
public class Movie {
    int movieId;
    String poster_path;
    String title;
    String overview;
    String backdrop_path;
    String release_date;

    double popularity;
    double vote_average;

    public Movie(JSONObject jsonObject) throws JSONException {
        poster_path = jsonObject.getString("poster_path");
        backdrop_path = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        vote_average = jsonObject.getDouble("vote_average");
        movieId = jsonObject.getInt("id");
        popularity = jsonObject.getDouble("popularity");
        release_date = jsonObject.getString("release_date");
    }

    // empty constructor needed by the Parceler library
    public Movie() {
    }

    public int getMovieId() {
        return movieId;
    }

    // iterating the JSONArray and construct movies for every elt of the array
    public static List<Movie> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            movies.add(new Movie(jsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPoster_path() {
        // %s is the logo for the path comes later
        // change the relative directory into to the hard directory
        return String.format("https://image.tmdb.org/t/p/w342/%s", poster_path);
    }

    public String getBackdrop_path() {
        return String.format("https://image.tmdb.org/t/p/w300/%s", backdrop_path);

    }

    public double getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getReleaseDate() {
        return release_date;
    }
}
