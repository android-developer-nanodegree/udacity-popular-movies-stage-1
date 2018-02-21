package com.sielski.marcin.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class PopularMovie implements Parcelable {
    private final String mThumbnailPath;
    private final String mOriginalTitle;
    private final String mPosterPath;
    private final String mOverview;
    private final String mVoteAverage;
    private final String mReleaseDate;

    public PopularMovie(String thumbnailPath, String originalTitle, String posterPath, String overview,
                        String voteAverage, String releaseDate) {
        mThumbnailPath = thumbnailPath;
        mOriginalTitle = originalTitle;
        mPosterPath = posterPath;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
    }

    private PopularMovie(Parcel parcel) {
        mThumbnailPath = parcel.readString();
        mOriginalTitle = parcel.readString();
        mPosterPath = parcel.readString();
        mOverview = parcel.readString();
        mVoteAverage = parcel.readString();
        mReleaseDate = parcel.readString();
    }
    public String getThumbnailPath() {
        return mThumbnailPath;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getReaseDate() {
        return mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mThumbnailPath);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOverview);
        parcel.writeString(mVoteAverage);
        parcel.writeString(mReleaseDate);
    }

    public final static Creator<PopularMovie> CREATOR = new Creator<PopularMovie>() {
        @Override
        public PopularMovie createFromParcel(Parcel parcel) {
            return new PopularMovie(parcel);
        }

        @Override
        public PopularMovie[] newArray(int i) {
            return new PopularMovie[i];
        }
    };
}
