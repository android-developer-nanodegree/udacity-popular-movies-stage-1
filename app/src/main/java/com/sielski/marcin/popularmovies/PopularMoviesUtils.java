package com.sielski.marcin.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class PopularMoviesUtils {
    private final static String THEMOVIEDB_API_BASE_URL = "https://api.themoviedb.org/3/movie";
    private final static String THEMOVIEDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    public final static String POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    private final static String API_KEY = "api_key";
    private final static String THUMBNAIL_SIZE = "w185";
    private final static String POSTER_SIZE = "w342";

    private final static String JSON_RESULTS = "results";
    private final static String JSON_ORIGINAL_TITLE = "original_title";
    private final static String JSON_PORTER_PATH = "poster_path";
    private final static String JSON_OVERVIEW = "overview";
    private final static String JSON_VOTE_AVERAGE = "vote_average";
    private final static String JSON_RELEASE_DATE = "release_date";

    public final static String DETAILS = "details";

    public final static String SORT_CRITERIA = "sort_criteria";

    public final static String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    public static String getTheMoviesDBApiKey(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.key_themoviedb_api_key), "");
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static URL buildApiUrl(Context context, String endpoint) {
        Uri uri = Uri.parse(THEMOVIEDB_API_BASE_URL).buildUpon().appendPath(endpoint).
                appendQueryParameter(API_KEY, getTheMoviesDBApiKey(context)).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static URL buildImageUrl(String size, String path) {
        Uri uri= Uri.parse(THEMOVIEDB_IMAGE_BASE_URL).buildUpon().appendPath(size).
                appendPath(path.substring(1)).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static List<PopularMovie> getPopularMovies(String json) {
        List<PopularMovie> popularMovies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(JSON_RESULTS)) {
                JSONArray jsonResults = jsonObject.getJSONArray(JSON_RESULTS);
                for (int index = 0; index < jsonResults.length(); index ++) {
                    JSONObject jsonPopularMovie = jsonResults.getJSONObject(index);
                    if (jsonPopularMovie.has(JSON_ORIGINAL_TITLE) &&
                            jsonPopularMovie.has(JSON_PORTER_PATH) &&
                            jsonPopularMovie.has(JSON_OVERVIEW) &&
                            jsonPopularMovie.has(JSON_VOTE_AVERAGE) &&
                            jsonPopularMovie.has(JSON_RELEASE_DATE)) {
                        popularMovies.add(new PopularMovie(
                                getPopularMoviePosterPath(THUMBNAIL_SIZE, jsonPopularMovie.optString(JSON_PORTER_PATH)),
                                jsonPopularMovie.optString(JSON_ORIGINAL_TITLE),
                                getPopularMoviePosterPath(POSTER_SIZE, jsonPopularMovie.optString(JSON_PORTER_PATH)),
                                jsonPopularMovie.optString(JSON_OVERVIEW),
                                jsonPopularMovie.optString(JSON_VOTE_AVERAGE),
                                jsonPopularMovie.optString(JSON_RELEASE_DATE)));
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return popularMovies;
    }

    private static String getPopularMoviePosterPath(String size, String posterPath) {
        return buildImageUrl(size, posterPath).toString();
    }
}
