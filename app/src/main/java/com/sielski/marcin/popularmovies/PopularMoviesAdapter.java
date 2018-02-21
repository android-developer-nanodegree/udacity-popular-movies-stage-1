package com.sielski.marcin.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

class PopularMoviesAdapter extends ArrayAdapter<PopularMovie> {
    public PopularMoviesAdapter(@NonNull Context context, @NonNull List<PopularMovie> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PopularMovie popularMovie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.poster_popular_movie, parent, false);
        }
        if (popularMovie != null) {
            ImageView imageView = convertView.findViewById(R.id.image_popular_movie);
            Picasso.with(getContext()).load(popularMovie.getThumbnailPath()).into(imageView);
        }
        return convertView;
    }
}
