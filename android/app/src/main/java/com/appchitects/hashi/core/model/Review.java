package com.appchitects.hashi.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Review implements Parcelable {

    public String author;
    public String authorImagePath;
    public String text;

    public Review(JSONObject jsonAuthor) {
        try {
            this.author = jsonAuthor.getString("author");
            this.authorImagePath = jsonAuthor.getString("authorImagePath");
            this.text = jsonAuthor.getString("text");
        } catch (JSONException e) {
            Log.e("ReviewParser", e.toString());
        }
    }

    protected Review(Parcel in) {
        author = in.readString();
        authorImagePath = in.readString();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(authorImagePath);
        dest.writeString(text);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}

// Keep it, augment this class instead and then head to http://www.parcelabler.com
/*public class Review {

    public String author;
    public String authorImagePath;
    public String text;

    public Review(JSONObject jsonAuthor) {
        try {
            this.id = jsonAuthor.getLong("id");
            this.author = jsonAuthor.getString("author");
            this.authorImagePath = jsonAuthor.getString("authorImagePath");
            this.text = jsonAuthor.getString("text");
        } catch (JSONException e) {
            Log.e("ReviewParser", e.toString());
        }
    }
}*/
