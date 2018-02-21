package com.sielski.marcin.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class PopularMoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        if (PopularMoviesUtils.getTheMoviesDBApiKey(this).length() !=
                getResources().getInteger(R.integer.length_themoviedb_api_key)) {
            startActivity(new Intent(this, PopularMoviesSettingsActivity.class));
            Toast.makeText(this,
                    getString(R.string.toast_themoviedb_api_key), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, PopularMoviesSettingsActivity.class));
                break;
            case R.id.action_sort_movies_by_popularity:
                ((PopularMoviesFragment)getFragmentManager().
                        findFragmentById(R.id.fragment_popular_movies)).
                        sortMoviesBy(PopularMoviesUtils.POPULAR);
                break;
            case R.id.action_sort_movies_by_top_rated:
                ((PopularMoviesFragment)getFragmentManager().
                        findFragmentById(R.id.fragment_popular_movies)).
                        sortMoviesBy(PopularMoviesUtils.TOP_RATED);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
