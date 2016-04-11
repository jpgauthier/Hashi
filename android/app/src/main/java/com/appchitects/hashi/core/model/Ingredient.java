package com.appchitects.hashi.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient implements Parcelable, ISearchable {
    public String name;
    public String alternativeName;
    public String imagePath;

    public Ingredient(JSONObject jsonIngredient) {
        try {
            this.name = jsonIngredient.getString("name");
            this.alternativeName = jsonIngredient.getString("alternativeName");
            this.imagePath = jsonIngredient.getString("imagePath");
        } catch (JSONException e) {
            Log.e("Ingredient parser", e.toString());
        }
    }

    protected Ingredient(Parcel in) {
        name = in.readString();
        alternativeName = in.readString();
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
        dest.writeString(alternativeName);
        dest.writeString(imagePath);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}

// Keep it, augment this class instead and then head to http://www.parcelabler.com
/*public class Ingredient {
    public String name;
    public String alternativeName;
    public String imagePath;

    public Ingredient(JSONObject jsonIngredient) {
        try {
            this.name = jsonIngredient.getString("name");
            this.alternativeName = jsonIngredient.getString("alternativeName");
            this.imagePath = jsonIngredient.getString("imagePath");
        } catch (JSONException e) {
            Log.e("Ingredient parser", e.toString());
        }
    }
}*/
