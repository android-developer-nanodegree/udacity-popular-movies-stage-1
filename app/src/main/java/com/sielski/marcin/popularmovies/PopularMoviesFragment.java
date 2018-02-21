package com.sielski.marcin.popularmovies;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PopularMoviesFragment extends Fragment {

    private PopularMoviesAdapter mPopularMoviesAdapter;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private String mSortCriteria = PopularMoviesUtils.POPULAR;


    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    private final class TheMovieDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String theMovieDBResult = null;
            try {
                theMovieDBResult = PopularMoviesUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return theMovieDBResult;
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (s != null && s.length() != 0) {
                List<PopularMovie> popularMovies = PopularMoviesUtils.getPopularMovies(s);
                mPopularMoviesAdapter = new PopularMoviesAdapter(getActivity(),popularMovies);
                mGridView.setAdapter(mPopularMoviesAdapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (PopularMoviesUtils.isNetworkAvailable(getContext())) {
                            PopularMovie popularMovie = mPopularMoviesAdapter.getItem(i);
                            Intent intent = new Intent(getContext(), PopularMovieDetailsActivity.class);
                            intent.putExtra(PopularMoviesUtils.DETAILS, popularMovie);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(),
                                    getString(R.string.toast_network_unavialable),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =
                inflater.inflate(R.layout.fragment_popular_movies, container, false);
        mGridView = view.findViewById(R.id.grid_popular_movies);
        mProgressBar = view.findViewById(R.id.progress_popular_movies);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mSortCriteria = savedInstanceState.getString(PopularMoviesUtils.SORT_CRITERIA);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(PopularMoviesUtils.SORT_CRITERIA, mSortCriteria);
        super.onSaveInstanceState(outState);
    }

    public void sortMoviesBy(String sortCriteria) {
        if (PopularMoviesUtils.isNetworkAvailable(getContext())) {
            if (sortCriteria != null) {
                mSortCriteria = sortCriteria;
            }
            new TheMovieDBQueryTask().execute(PopularMoviesUtils.buildApiUrl(getContext(),
                    mSortCriteria));
        } else {
            Toast.makeText(getContext(),getString(R.string.toast_network_unavialable),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sortMovies() {
        sortMoviesBy(null);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sortMovies();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(broadcastReceiver,
                new IntentFilter(PopularMoviesUtils.ACTION_NETWORK_CHANGE));
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
}
