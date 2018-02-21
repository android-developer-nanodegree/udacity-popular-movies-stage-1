package com.sielski.marcin.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PopularMovieDetailsFragment extends Fragment {

    private ProgressBar mProgressBar;
    private View mView;
    private PopularMovie mPopularMovie;

    public PopularMovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_popular_movie_details, container,
                false);
        mProgressBar = mView.findViewById(R.id.progress_popular_movie_details);
        return mView;
    }
/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
*/
    public void setPopularMovie(PopularMovie popularMovie) {
        mPopularMovie = popularMovie;
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (PopularMoviesUtils.isNetworkAvailable(context)) {
                Picasso.with(getContext()).load(mPopularMovie.getPosterPath()).into(
                        new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                ((ImageView) mView.findViewById(R.id.details_poster)).
                                        setImageBitmap(bitmap);
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                mProgressBar.setVisibility(View.VISIBLE);
                            }
                        }
                );
                ((TextView) mView.findViewById(R.id.details_overview)).
                        setText(mPopularMovie.getOverview());
                ((TextView) mView.findViewById(R.id.details_vote_average)).
                        setText(mPopularMovie.getVoteAverage());
                ((TextView) mView.findViewById(R.id.details_release_date)).
                        setText(mPopularMovie.getReaseDate());
            }
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
