package com.appchitects.hashi.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class SideDish implements Parcelable, ISearchable {
    public String name;
    public String imagePath;

    public SideDish(JSONObject jsonSideDish) {
        try {
            this.name = jsonSideDish.getString("name");
            this.imagePath = jsonSideDish.getString("imagePath");
        } catch (JSONException e) {
            Log.e("SideDish parser", e.toString());
        }
    }

    protected SideDish(Parcel in) {
        name = in.readString();
        imagePath = in.readString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imagePath);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SideDish> CREATOR = new Parcelable.Creator<SideDish>() {
        @Override
        public SideDish createFromParcel(Parcel in) {
            return new SideDish(in);
        }

        @Override
        public SideDish[] newArray(int size) {
            return new SideDish[size];
        }
    };

}

// Keep it, augment this class instead and then head to http://www.parcelabler.com
/*public class SideDish {
    public String name;
    public String imagePath;
}*/
