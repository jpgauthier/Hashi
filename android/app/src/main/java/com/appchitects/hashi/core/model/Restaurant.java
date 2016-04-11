package com.appchitects.hashi.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Parcelable {

    public long id;
    public String name;
    public String logoPath;
    public String imagePath;
    public Double longitude;
    public Double latitude;
    public Double distance;
    public String address;
    public String city;
    public String phone;
    public Integer rating;
    public List<Review> reviews;
    public List<MenuItem> menu;
    public List<Ingredient> ingredientsMatches;
    public List<SideDish> sideDishesMatches;

    public Restaurant(JSONObject jsonRestaurant) {
        try {
            this.id = jsonRestaurant.getLong("id");
            this.name = jsonRestaurant.getString("name");
            this.logoPath = jsonRestaurant.getString("logoPath");
            this.imagePath = jsonRestaurant.getString("imagePath");
            this.longitude = jsonRestaurant.getDouble("longitude");
            this.latitude = jsonRestaurant.getDouble("latitude");
            this.distance = jsonRestaurant.getDouble("distance");
            this.address = jsonRestaurant.getString("address");
            this.city = jsonRestaurant.getString("city_name");
            this.phone = jsonRestaurant.getString("phone");

            JSONArray ingredientsArray = jsonRestaurant.getJSONArray("ingredientsMatches");
            this.ingredientsMatches = new ArrayList<>();
            for (int i = 0; i < ingredientsArray.length(); i++) {
                try {
                    JSONObject jsonIngredient = ingredientsArray.getJSONObject(i);
                    Ingredient ingredient = new Ingredient(jsonIngredient);
                    this.ingredientsMatches.add(ingredient);
                } catch(JSONException e) {
                    Log.e("Restaurant Ing M parser", e.toString());
                }
            }

            JSONArray sideDishesArray = jsonRestaurant.getJSONArray("sideDishesMatches");
            this.sideDishesMatches = new ArrayList<>();
            for (int i = 0; i < sideDishesArray.length(); i++) {
                try {
                    JSONObject jsonSideDish = sideDishesArray.getJSONObject(i);
                    SideDish sideDish = new SideDish(jsonSideDish);
                    this.sideDishesMatches.add(sideDish);
                } catch(JSONException e) {
                    Log.e("Restaurant Sid M parser", e.toString());
                }
            }

            this.rating = jsonRestaurant.getInt("rating");
        } catch (JSONException e) {
            Log.e("Restaurant Parser", e.toString());
        }
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }

    protected Restaurant(Parcel in) {
        id = in.readLong();
        name = in.readString();
        logoPath = in.readString();
        imagePath = in.readString();
        longitude = in.readByte() == 0x00 ? null : in.readDouble();
        latitude = in.readByte() == 0x00 ? null : in.readDouble();
        distance = in.readByte() == 0x00 ? null : in.readDouble();
        address = in.readString();
        city = in.readString();
        phone = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            reviews = new ArrayList<Review>();
            in.readList(reviews, Review.class.getClassLoader());
        } else {
            reviews = null;
        }
        if (in.readByte() == 0x01) {
            menu = new ArrayList<MenuItem>();
            in.readList(menu, MenuItem.class.getClassLoader());
        } else {
            menu = null;
        }
        if (in.readByte() == 0x01) {
            ingredientsMatches = new ArrayList<Ingredient>();
            in.readList(ingredientsMatches, Ingredient.class.getClassLoader());
        } else {
            ingredientsMatches = null;
        }
        if (in.readByte() == 0x01) {
            sideDishesMatches = new ArrayList<SideDish>();
            in.readList(sideDishesMatches, SideDish.class.getClassLoader());
        } else {
            sideDishesMatches = null;
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
        dest.writeString(logoPath);
        dest.writeString(imagePath);
        if (longitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(longitude);
        }
        if (latitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(latitude);
        }
        if (distance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(distance);
        }
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(phone);
        if (rating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(rating);
        }
        if (reviews == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reviews);
        }
        if (menu == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(menu);
        }
        if (ingredientsMatches == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredientsMatches);
        }
        if (sideDishesMatches == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(sideDishesMatches);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}

// Keep it, augment this class instead and then head to http://www.parcelabler.com
/*public class Restaurant {

    public long id;
    public String name;
    public String logoPath;
    public String imagePath;
    public Double longitude;
    public Double latitude;
    public Double distance;
    public String address;
    public String city;
    public Integer rating;
    public List<Review> reviews;
    public List<MenuItem> menu;
    public List<Ingredient> ingredientMatches;
    public List<SideDish> sideDishesMatches;

    public Restaurant(JSONObject jsonRestaurant) {
        try {
            this.id = jsonRestaurant.getLong("id");
            this.name = jsonRestaurant.getString("name");
            this.logoPath = jsonRestaurant.getString("logoPath");
            this.imagePath = jsonRestaurant.getString("imagePath");
            this.longitude = jsonRestaurant.getDouble("longitude");
            this.latitude = jsonRestaurant.getDouble("latitude");
            this.distance = jsonRestaurant.getDouble("distance");
            this.address = jsonRestaurant.getString("address");
            this.city = jsonRestaurant.getString("city_name");
            this.rating = jsonRestaurant.getInt("rating");

            JSONArray ingredientsArray = jsonRestaurant.getJSONArray("ingredientsMatches");
            this.ingredientsMatches = new ArrayList<>();
            for (int i = 0; i < ingredientsArray.length(); i++) {
                try {
                    JSONObject jsonIngredient = ingredientsArray.getJSONObject(i);
                    Ingredient ingredient = new Ingredient(jsonIngredient);
                    this.ingredientsMatches.add(ingredient);
                } catch(JSONException e) {
                    Log.e("Restaurant Ing M parser", e.toString());
                }
            }

            JSONArray sideDishesArray = jsonRestaurant.getJSONArray("sideDishesMatches");
            this.sideDishesMatches = new ArrayList<>();
            for (int i = 0; i < sideDishesArray.length(); i++) {
                try {
                    JSONObject jsonSideDish = sideDishesArray.getJSONObject(i);
                    SideDish sideDish = new SideDish(jsonSideDish);
                    this.sideDishesMatches.add(sideDish);
                } catch(JSONException e) {
                    Log.e("Restaurant Sid M parser", e.toString());
                }
            }


        } catch (JSONException e) {
            Log.e("Restaurant Parser", e.toString());
        }
    }

    public void addReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addMenu(List<MenuItem> menu) {
        this.menu = menu;
    }
}*/
