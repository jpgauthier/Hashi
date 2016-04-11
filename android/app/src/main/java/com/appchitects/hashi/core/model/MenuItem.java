package com.appchitects.hashi.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuItem implements Parcelable {

    public long id;
    public String name;
    public String imagePath;
    public String category;
    public List<Ingredient> ingredients;

    public MenuItem(JSONObject jsonMenuItem) {
        try {
            this.id = jsonMenuItem.getLong("id");
            this.name = jsonMenuItem.getString("name");
            this.imagePath = jsonMenuItem.getString("imagePath");
            this.category = jsonMenuItem.getString("category_name");
            this.ingredients = new ArrayList<>();

            JSONArray ingredientsArray = jsonMenuItem.getJSONArray("ingredients");
            for (int i = 0; i < ingredientsArray.length(); i++) {
                try {
                    JSONObject jsonIngredient = ingredientsArray.getJSONObject(i);
                    Ingredient ingredient = new Ingredient(jsonIngredient);
                    this.ingredients.add(ingredient);
                } catch(JSONException e) {
                    Log.e("MenuItem Ing parser", e.toString());
                }
            }
        } catch (JSONException e) {
            Log.e("MenuItem Parser", e.toString());
        }
    }

    protected MenuItem(Parcel in) {
        id = in.readLong();
        name = in.readString();
        imagePath = in.readString();
        category = in.readString();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<Ingredient>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(imagePath);
        dest.writeString(category);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };
}

// Keep it, augment this class instead and then head to http://www.parcelabler.com
/*public class MenuItem {

    public long id;
    public String name;
    public String imagePath;
    public String category;
    public List<Ingredient> ingredients;

    public MenuItem(JSONObject jsonMenuItem) {

        try {
            this.id = jsonMenuItem.getLong("id");
            this.name = jsonMenuItem.getString("name");
            this.imagePath = jsonMenuItem.getString("imagePath");
            this.category = jsonMenuItem.getString("category_name");
            this.ingredients = new ArrayList<>();

            JSONArray ingredientsArray = jsonMenuItem.getJSONArray("ingredients");
            for (int i = 0; i < ingredientsArray.length(); i++) {
                try {
                    JSONObject jsonIngredient = ingredientsArray.getJSONObject(i);
                    Ingredient ingredient = new Ingredient(jsonIngredient);
                    this.ingredients.add(ingredient);
                } catch(JSONException e) {
                    Log.e("MenuItem Ing parser", e.toString());
                }
            }
        } catch (JSONException e) {
            Log.e("MenuItem Parser", e.toString());
        }
    }
}*/